

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
epsilon = 0.95
target_update = 10
buffer_size = 10000
minimal_size = 100
batch_size = 64
device = torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu")

np.random.seed(0)
torch.manual_seed(0)
replay_buffer = ReplayBuffer(buffer_size)
replay_buffer.load()

state_dim = 17
action_dim = 10
agent = DQN(state_dim, hidden_dim, action_dim, lr, gamma, epsilon,
            target_update, device)
agent.load()

laststate = []
lastaction = 0
lastscore = 0


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


@app.route("/player/action", methods=['GET', 'POST'])
def action():
    global laststate
    global lastaction
    global lastscore
    reqbody = request.get_json()
    score = reqbody["myScore"]
    maplist = getMapList(reqbody)
    selfpos = getSelf(reqbody)
    booms = getBooms(reqbody)
    explodes = getExplodes(reqbody)
    magicBoxes = getMagicBoxes(reqbody)
    npcs = getNpcs(reqbody)
    #state = np.concatenate((maplist, selfpos, booms, explodes, magicBoxes))
    cur_state = np.array([maplist, selfpos, booms, explodes, magicBoxes, npcs])
    state = []
    if len(laststate) > 0:
        state = laststate[1:]
        state.append(cur_state)
    else:
        state = [cur_state, cur_state, cur_state,  cur_state, cur_state]

    if_rand = True
    needCheck = True
    moveType = ""
    releaseBoom = True
    while if_rand and needCheck:
        if_rand, action = agent.take_action(state)
        actionval = int(action)
        moveType = MoveTypeList[actionval % 5]
        releaseBoom = actionval % 2 == 1
        if if_rand:
            needCheck = checkMove(reqbody, maplist, moveType)
    print(f'action:{actionval},moveType={moveType},releaseBoom:{releaseBoom}')

    #超过其他任意一人时，算作done
    if_first = False
    selfid = reqbody["selfNpcId"]
    activeNpcs = reqbody["gameMap"]["activeNpcs"]
    other_scores = []
    for npc in activeNpcs:
        npcId = npc["npcId"]
        if npcId != selfid:
            other_scores.append(npc["score"])
    other_min_scores = np.min(other_scores)
    if score > other_min_scores:
        if_first = True

    if len(laststate) > 0:
        replay_buffer.add(laststate, lastaction, score - lastscore, state, if_first)
    laststate = state
    lastaction = action
    lastscore = score
    return {"moveType": moveType, "releaseBoom": releaseBoom}


@app.route("/clear")
def clear():
    global laststate
    global lastaction
    global lastscore
    laststate = []
    lastaction = 0
    lastscore = 0
    for i in range(100):
        b_s, b_a, b_r, b_ns, b_d = replay_buffer.sample(batch_size)
        transition_dict = {
            'states': b_s,
            'actions': b_a,
            'next_states': b_ns,
            'rewards': b_r,
            'dones': b_d
        }
        agent.update(transition_dict)
    return "ok"


@app.route("/save")
def save():
    replay_buffer.save()
    agent.save()
    return "ok"

@app.route("/setRandRate/<value>")
def setRandRate(value):
    agent.epsilon = float(value)
    return str(type(agent.epsilon))



if __name__ == "__main__":
    app.run(debug=True)


