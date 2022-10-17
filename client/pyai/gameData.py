import numpy as np


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
    y = int(req["slefLocationY"] / 64) - 1
    x = int(req["slefLocationX"] / 64) + 1
    arr[y][x] = 1
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
        arr[explode["row"]][explode["col"]] = 1
        if row > 0:
            arr[explode["row"] - 1][explode["col"]] = 1
        if row < rownum - 1:
            arr[explode["row"] + 1][explode["col"]] = 1
        if col > 0:
            arr[explode["row"]][explode["col"] - 1] = 1
        if col < colnum - 1:
            arr[explode["row"]][explode["col"] + 1] = 1
    return arr


def getMagicBoxes(req):
    activeMagicBoxes = req["gameMap"]["activeMagicBoxes"]
    rownum = req["gameMap"]["mapRows"]
    colnum = req["gameMap"]["mapCols"]
    arr = np.zeros((rownum, colnum), dtype=int)
    for magicBox in activeMagicBoxes:
        arr[magicBox["row"]][magicBox["col"]] = 1
    return arr

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

