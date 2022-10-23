

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
gamma = 0.9
epsilon = 0.5
target_update = 10
buffer_size = 199990
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

laststateDict = {}
lastactionDict = {}
lastscoreDict = {}


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


@app.route("/player/action", methods=['GET', 'POST'])
def action():
    global laststateDict
    global lastactionDict
    global lastscoreDict
    reqbody = request.get_json()
    selfid =str(reqbody["selfNpcId"])
    score = reqbody["myScore"]
    laststate = laststateDict.get(selfid, [])
    lastaction = lastactionDict.get(selfid, 0)
    lastscore = lastscoreDict.get(selfid, 0)

    maplist = getMapList(reqbody)
    selfpos = getSelf(reqbody)
    booms = getBooms(reqbody)
    explodes = getExplodes(reqbody)
    magicBoxes = getMagicBoxes(reqbody)
    npcs = getNpcs(reqbody)
    #state = np.concatenate((maplist, selfpos, booms, explodes, magicBoxes))
    #cur_state = np.array([maplist, selfpos, booms, explodes, magicBoxes, npcs])
    state = getState(reqbody)

    if_rand = True
    needCheck = True
    moveType = ""
    releaseBoom = True
    action = 0
    actionval = 0
    global epsilon
    if np.random.random() < epsilon:
        moveType = getMoveType(reqbody, maplist)
        if np.random.randint(0, 10) > 8:
            moveType = MoveTypeList[np.random.randint(0, 5)]
        releaseBoom = getSetBoom(reqbody, maplist, moveType)
        action = getAction(moveType, releaseBoom)
        actionval = action
    else:
        while if_rand and needCheck:
            if_rand, action = agent.take_action(state)
            actionval = int(action)
            moveType = MoveTypeList[actionval % 5]
            releaseBoom = actionval % 2 == 1
            if if_rand:
                needCheck = checkAction(reqbody, maplist, moveType, releaseBoom)
    print(f'action:{actionval},moveType={moveType},releaseBoom:{releaseBoom}')

    #超过其他任意一人时，算作done
    if_first = False
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
    laststateDict[selfid] = state
    lastactionDict[selfid] = action
    lastscoreDict[selfid] = score
    return {"moveType": moveType, "releaseBoom": releaseBoom}


@app.route("/clear")
def clear():
    global laststateDict
    global lastactionDict
    global lastscoreDict
    laststateDict = {}
    lastactionDict = {}
    lastscoreDict = {}
    if replay_buffer.size() > 1000:
        train_times = replay_buffer.size() / 100
        if train_times > 100:
            train_times = 100
        for i in range(train_times):
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
    global epsilon
    epsilon = float(value)
    agent.epsilon = float(value)
    return str(type(agent.epsilon))



if __name__ == "__main__":
    app.run(debug=True)


