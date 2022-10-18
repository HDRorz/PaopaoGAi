

import numpy as np
import torch
from flask import Flask
from flask import request

from gameData import *
from train import ReplayBuffer, DQN

app = Flask(__name__)

MoveTypeList = ["LEFT", "TOP", "RIGHT", "DOWN", "STOP"]

lr = 2e-3
num_episodes = 500
hidden_dim = 128
gamma = 0.98
epsilon = 0.2
target_update = 10
buffer_size = 10000
minimal_size = 100
batch_size = 64
device = torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu")

np.random.seed(0)
torch.manual_seed(0)
replay_buffer = ReplayBuffer(buffer_size)
state_dim = 17
action_dim = 10
agent = DQN(state_dim, hidden_dim, action_dim, lr, gamma, epsilon,
            target_update, device)

laststate = []
lastaction = 0
lastscore = 0


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


@app.route("/player/action", methods=['GET', 'POST'])
def action():
    reqbody = request.get_json()
    score = reqbody["myScore"]
    maplist = getMapList(reqbody)
    selfpos = getSelf(reqbody)
    booms = getBooms(reqbody)
    explodes = getExplodes(reqbody)
    magicBoxes = getMagicBoxes(reqbody)
    #state = np.concatenate((maplist, selfpos, booms, explodes, magicBoxes))
    state = np.array([maplist, selfpos, booms, explodes, magicBoxes])
    action = agent.take_action(state)
    actionval = int(action)
    moveType = MoveTypeList[actionval % 5]
    releaseBoom = actionval % 2 == 1
    print(f'action:{actionval},moveType={moveType},releaseBoom:{releaseBoom}')

    global laststate
    global lastaction
    global lastscore
    if laststate.__len__() > 0:
        replay_buffer.add(laststate, lastaction, score - lastscore, state, False)
    laststate = state
    lastaction = action
    lastscore = score
    if replay_buffer.size() > minimal_size:
        b_s, b_a, b_r, b_ns, b_d = replay_buffer.sample(batch_size)
        transition_dict = {
            'states': b_s,
            'actions': b_a,
            'next_states': b_ns,
            'rewards': b_r,
            'dones': b_d
        }
        agent.update(transition_dict)
    return {"moveType": moveType, "releaseBoom": releaseBoom}


@app.route("/save")
def save():
    torch.save(agent.q_net.state_dict(), 'checkpoint/dqn_checkpoint_solved.pth')


if __name__ == "__main__":
    app.run(debug=True)


