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


def getNextExplodes(req):
    activeBooms = req["gameMap"]["activeBooms"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum))
    for boom in activeBooms:
        row = boom["row"]
        col = boom["col"]
        arr[boom["row"]][boom["col"]] = 1
        if row > 0:
            arr[boom["row"] - 1][boom["col"]] = 1
        if row < rownum - 1:
            arr[boom["row"] + 1][boom["col"]] = 1
        if col > 0:
            arr[boom["row"]][boom["col"] - 1] = 1
        if col < colnum - 1:
            arr[boom["row"]][boom["col"] + 1] = 1
    return arr

# 撞墙返回true
def checkMoveType(req, mapList, moveType):
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
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
        return False

    if next_x < 0 or next_x >= colnum:
        return True
    if next_y < 0 or next_y >= rownum:
        return True
    nextitem = int(mapList[next_y][next_x])
    if nextitem == 0 or nextitem == 2:
        return True
    return False


def checkAction(req, mapList, moveType, releaseBoom):
    move_check = checkMoveType(req, mapList, moveType)
    if move_check:
        return move_check

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

#检查下一次会不会撞上炸弹
def checkBoomAttack(req, nextExplodes, moveType):
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
        return False
    nextitem = nextExplodes[next_y][next_x]
    if nextitem > 0:
        return True
    return False

#检查下一次会不会捡到道具
def checkGetItem(req, mapList, moveType):
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
        return False
    nextitem = int(mapList[next_y][next_x])
    if nextitem == 3:
        return True
    return False



def getMoveType(req, mapList):
    if np.random.randint(0, 100) < 5:
        return "STOP"
    MoveTypeList = ["LEFT", "TOP", "RIGHT", "DOWN"]
    y = int(req["slefLocationY"] / 64)
    x = int(req["slefLocationX"] / 64)
    print(f'getMoveType, x={x}, y={y}')
    goodMoves = []
    for move in MoveTypeList:
        if checkMoveType(req, mapList, move) == False:
            goodMoves.append(move)
    print(goodMoves)

    goodMoves2 = []
    nextExplodes = getNextExplodes(req)
    for move in goodMoves:
        if checkBoomAttack(req, nextExplodes, move) == False:
            goodMoves2.append(move)
    print(goodMoves2)
    if len(goodMoves2) == 0:
        goodMoves2 = goodMoves

    goodMoves3 = []
    for move in goodMoves2:
        if checkGetItem(req, mapList, move) == True:
            goodMoves3.append(move)
    if len(goodMoves3) == 0:
        goodMoves3 = goodMoves2

    goodMoves4 = []
    explodes = getExplodes(req) * -1
    for move in goodMoves3:
        if checkBoomAttack(req, explodes, move) == False:
            goodMoves4.append(move)
    if len(goodMoves4) == 0:
        goodMoves4 = goodMoves3
    idx = np.random.randint(0, len(goodMoves4))
    return goodMoves4[idx]


def getSetBoom(req, mapList, moveType):
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
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

    otheritems = []
    other_x = next_x
    other_y = next_y
    if other_x - 1 >= 0:
        otheritem = mapList[other_y][other_x - 1]
        otheritems.append(otheritem)
    if other_x + 1 < colnum:
        otheritem = mapList[other_y][other_x + 1]
        otheritems.append(otheritem)
    if other_y - 1 >= 0:
        otheritem = mapList[other_y - 1][other_x]
        otheritems.append(otheritem)
    if other_y + 1 < rownum:
        otheritem = mapList[other_y + 1][other_x]
        otheritems.append(otheritem)
    gooditems = []
    for otheritem in otheritems:
        if int(otheritem) == 2:
            gooditems.append(otheritem)
    if len(gooditems) > 0:
        #90%概率放泡泡
        if np.random.randint(0, 10) > 0:
            return True
    #10%概率放泡泡
    if np.random.randint(0, 10) >= 9:
        return True
    return False

def getAction(moveType, setBoom):
    moveVal = 0
    if moveType == "LEFT":
        moveVal = 0
    if moveType == "TOP":
        moveVal = 1
    if moveType == "RIGHT":
        moveVal = 2
    if moveType == "DOWN":
        moveVal = 3
    if moveType == "STOP":
        moveVal = 4
    if setBoom:
        return moveVal * 2 + 1
    else:
        return moveVal * 2


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

