import numpy as np


def getState(req):
    maplist = req["gameMap"]["mapList"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum, 6))
    x = 0
    y = 0
    for maprow in maplist:
        for cell in maprow:
            arr[y][x][0] = int(cell[:1])
            x = x + 1
        y = y + 1
        x = 0

    self_y = int(req["slefLocationY"] / 64)
    self_x = int(req["slefLocationX"] / 64)
    arr[self_y][self_x][1] = 100

    activeBooms = req["gameMap"]["activeBooms"]
    for boom in activeBooms:
        arr[boom["row"]][boom["col"]][2] = 1

    activeExplodes = req["gameMap"]["activeExplodes"]
    for explode in activeExplodes:
        row = explode["row"]
        col = explode["col"]
        arr[explode["row"]][explode["col"]][3] = -100
        if row > 0:
            arr[explode["row"] - 1][explode["col"]][3] = -100
        if row < rownum - 1:
            arr[explode["row"] + 1][explode["col"]][3] = -100
        if col > 0:
            arr[explode["row"]][explode["col"] - 1][3] = -100
        if col < colnum - 1:
            arr[explode["row"]][explode["col"] + 1][3] = -100

    activeMagicBoxes = req["gameMap"]["activeMagicBoxes"]
    for magicBox in activeMagicBoxes:
        arr[magicBox["row"]][magicBox["col"]][4] = 30

    selfid = req["selfNpcId"]
    activeNpcs = req["gameMap"]["activeNpcs"]
    for npc in activeNpcs:
        npcId = npc["npcId"]
        npcrow = npc["row"]
        npccol = npc["col"]
        if npcId != selfid \
                and npcrow >= 0\
                and npcrow < rownum\
                and npccol >= 0\
                and npccol < colnum:
            arr[npcrow][npccol][5] = 100
    return arr


def getMapList(req):
    maplist = req["gameMap"]["mapList"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    x = 0
    y = 0
    for maprow in maplist:
        for cell in maprow:
            arr[y][x] = int(cell[:1])
            x = x + 1
        y = y + 1
        x = 0
    return arr


def getSelf(req):
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    y = int(req["slefLocationY"] / 64)
    x = int(req["slefLocationX"] / 64)
    arr[y][x] = 100
    return arr


def getBooms(req):
    activeBooms = req["gameMap"]["activeBooms"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    for boom in activeBooms:
        arr[boom["row"]][boom["col"]] = 1
    return arr


def getExplodes(req):
    activeExplodes = req["gameMap"]["activeExplodes"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    for explode in activeExplodes:
        row = explode["row"]
        col = explode["col"]
        arr[explode["row"]][explode["col"]] = -100
        if row > 0:
            arr[explode["row"] - 1][explode["col"]] = -100
        if row < rownum - 1:
            arr[explode["row"] + 1][explode["col"]] = -100
        if col > 0:
            arr[explode["row"]][explode["col"] - 1] = -100
        if col < colnum - 1:
            arr[explode["row"]][explode["col"] + 1] = -100
    return arr


def getMagicBoxes(req):
    activeMagicBoxes = req["gameMap"]["activeMagicBoxes"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    for magicBox in activeMagicBoxes:
        arr[magicBox["row"]][magicBox["col"]] = 30
    return arr


def getNpcs(req):
    selfid = req["selfNpcId"]
    activeNpcs = req["gameMap"]["activeNpcs"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    for npc in activeNpcs:
        npcId = npc["npcId"]
        row = npc["row"]
        col = npc["col"]
        if npcId != selfid \
                and row >= 0\
                and row < rownum\
                and col >= 0\
                and col < colnum:
            arr[npc["row"]][npc["col"]] = 100
    return arr


def checkAction(req, mapList, moveType, releaseBoom):
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    y = int(req["slefLocationY"] / 64)
    x = int(req["slefLocationX"] / 64)
    next_x = x
    next_y = y
    if moveType == "LEFT":
        next_x = x - 1
    if moveType == "TOP":
        next_y = y - 1
    if moveType == "RIGHT":
        next_x = x + 1
    if moveType == "DOWN":
        next_y = y + 1
    if moveType == "STOP":
        # 1/2概率不允许停顿
        if np.random.randint(0, 2) == 0:
            return True
        return False

    if releaseBoom:
        # 2/3概率不允许发放泡泡
        if np.random.randint(0, 2) > 0:
            return True
        return False

    if next_x < 0 or next_x >= colnum:
        return True
    if next_y < 0 or next_y >= rownum:
        return True
    nextitem = mapList[next_y][next_x]
    if nextitem == 0 or nextitem == 2:
        return True
    return False

#
# def getBooms(req):
#     activeBooms = req["gameMap"]["activeBooms"]
#     asize = len(activeBooms)
#     arr = np.zeros((asize, 2))
#     x = 0
#     for boom in activeBooms:
#         arr[x][0] = boom["col"]
#         arr[x][1] = boom["row"]
#     return arr
#
#
# def getExplodes(req):
#     activeExplodes = req["gameMap"]["activeExplodes"]
#     asize = len(activeExplodes)
#     arr = np.zeros((asize, 2))
#     x = 0
#     for explode in activeExplodes:
#         arr[x][0] = explode["col"]
#         arr[x][1] = explode["row"]
#     return arr
#
#
# def getMagicBoxes(req):
#     activeMagicBoxes = req["gameMap"]["activeMagicBoxes"]
#     asize = len(activeMagicBoxes)
#     arr = np.zeros((asize, 2))
#     x = 0
#     for magicBox in activeMagicBoxes:
#         arr[x][0] = magicBox["col"]
#         arr[x][1] = magicBox["row"]
#     return arr

