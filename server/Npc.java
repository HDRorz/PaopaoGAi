package com.em.programming.game.model.vo;

import com.em.programming.game.consts.SquareType;
import com.em.programming.game.control.ControlAction;
import com.em.programming.game.control.CurrentFullNpcData;
import com.em.programming.game.control.GameMapData;
import com.em.programming.game.datastore.NpcUserInfo;
import com.em.programming.game.model.loader.ElementLoader;
import com.em.programming.game.model.manager.ElementManager;
import com.em.programming.game.model.manager.GameMap;
import com.em.programming.game.model.manager.MoveType;
import com.em.programming.game.test.NpcUserHelper;
import com.em.programming.game.util.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.ImageIcon;

public class Npc extends Character {
  public void setImgList(List<ImageIcon> imgList) {
    this.imgList = imgList;
  }
  
  public void setMoveX(int moveX) {
    this.moveX = moveX;
  }
  
  public void setImgW(int imgW) {
    this.imgW = imgW;
  }
  
  public void setImgH(int imgH) {
    this.imgH = imgH;
  }
  
  public void setNpcUserInfo(NpcUserInfo npcUserInfo) {
    this.npcUserInfo = npcUserInfo;
  }
  
  public void setNpcIndex(int npcIndex) {
    this.npcIndex = npcIndex;
  }
  
  public void setAttack(boolean attack) {
    this.attack = attack;
  }
  
  public void setStep(int step) {
    this.step = step;
  }
  
  public void setStopStepCount(AtomicInteger stopStepCount) {
    this.stopStepCount = stopStepCount;
  }
  
  public void setLastStepRow(int lastStepRow) {
    this.lastStepRow = lastStepRow;
  }
  
  public void setLastStepCol(int lastStepCol) {
    this.lastStepCol = lastStepCol;
  }
  
  public void setPath(Vector<MoveType> path) {
    this.path = path;
  }
  
  final String DANGER_MARKER = "-1";
  
  private List<ImageIcon> imgList;
  
  private int moveX;
  
  private int imgW;
  
  private int imgH;
  
  private NpcUserInfo npcUserInfo;
  
  private int npcIndex;
  
  private boolean attack;
  
  public String getDANGER_MARKER() {
    getClass();
    return "-1";
  }
  
  public List<ImageIcon> getImgList() {
    return this.imgList;
  }
  
  public int getMoveX() {
    return this.moveX;
  }
  
  public int getImgW() {
    return this.imgW;
  }
  
  public int getImgH() {
    return this.imgH;
  }
  
  public NpcUserInfo getNpcUserInfo() {
    return this.npcUserInfo;
  }
  
  public int getNpcIndex() {
    return this.npcIndex;
  }
  
  public boolean isAttack() {
    return this.attack;
  }
  
  private int step = 0;
  
  public int getStep() {
    return this.step;
  }
  
  private AtomicInteger stopStepCount = new AtomicInteger(0);
  
  public AtomicInteger getStopStepCount() {
    return this.stopStepCount;
  }
  
  private int lastStepRow = -1;
  
  public int getLastStepRow() {
    return this.lastStepRow;
  }
  
  private int lastStepCol = -1;
  
  private Vector<MoveType> path;
  
  public int getLastStepCol() {
    return this.lastStepCol;
  }
  
  public Vector<MoveType> getPath() {
    return this.path;
  }
  
  public Npc(int x, int y, int w, int h, int imgW, int imgH, List<ImageIcon> img, NpcUserInfo npcUserInfo, int npcIndex) {
    super(x, y, w, h);
    this.imgW = imgW;
    this.imgH = imgH;
    this.imgList = new ArrayList<>(img);
    this.path = new Vector<>();
    this.npcUserInfo = npcUserInfo;
    this.moveX = 0;
    this.moveType = "STOP";
    this.npcIndex = npcIndex;
    setSquareType(SquareType.NPC);
  }
  
  public static Npc createNpc(List<String> data, int i, int j, NpcUserInfo npcUserInfo, int npcIndex) {
    List<ImageIcon> imageList = new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
    int x = j * 64;
    int y = i * 64;
    int w = 64;
    int h = 64;
    int imgW = Integer.parseInt(data.get(3));
    int imgH = Integer.parseInt(data.get(4));
    return new Npc(x, y, w, h, imgW, imgH, imageList, npcUserInfo, npcIndex);
  }
  
  public void showElement(Graphics g) {
    if (!this.isShowing)
      return; 
    g.drawImage(((ImageIcon)this.imgList.get(this.moveX)).getImage(), 
        getX(), getY(), 
        getX() + getW(), getY() + getH(), 0, 0, 
        
        getImgW(), getImgH(), null);
    String string = this.npcUserInfo + ": " + this.score;
    g.setColor(new Color(234, 86, 34));
    g.setFont(new Font("微软雅黑", 1, 24));
    g.drawString(string, 0, (this.npcIndex + 1) * 64 / 2);
  }
  
  public void update() {
    if (!this.dead) {
      move();
      addBubble();
      updateImage();
      destroy();
    } 
  }
  
  public void addBubble() {
    List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX() + getW() / 2, getY() + getH() / 2));
    GameMap gameMap = ElementManager.getManager().getGameMap();
    List<Integer> maplist = GameMap.getIJ(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue());
    if (this.attack && !this.dead && gameMap
      .getBlockSquareType(((Integer)maplist.get(0)).intValue(), ((Integer)maplist.get(1)).intValue()) != SquareType.BUBBLE) {
      List<SuperElement> list = ElementManager.getManager().getElementList("bubble");
      list.add(Bubble.createBubble(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue(), 
            (List<String>)ElementLoader.getElementLoader().getGameInfoMap().get("bubble"), this.npcUserInfo.getNpcId(), 
            getBubblePower()));
      this.attack = false;
      this.bubbleNum++;
    } 
  }
  
  public void longStopHandle() {
    int stopSteps = this.stopStepCount.incrementAndGet();
    if (stopSteps > 5) {
      System.out.println(this.npcUserInfo + "长时间停留同一位置不动，扣除100分！");
      scoreChange(-100);
      this.stopStepCount.set(0);
    } 
  }
  
  public void move() {
    int tx = getX();
    int ty = getY();
    switch (this.moveType) {
      case "TOP":
        ty -= this.speed;
        break;
      case "LEFT":
        tx -= this.speed;
        break;
      case "RIGHT":
        tx += this.speed;
        break;
      case "DOWN":
        ty += this.speed;
        break;
    } 
    boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
    boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
    boolean det3 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));
    if (det1 && det2 && det3) {
      setX(tx);
      setY(ty);
    } 
  }
  
  private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
    for (SuperElement se : list) {
      switch (this.moveType) {
        case "TOP":
        case "DOWN":
          if (Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound()) || 
            Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound()) || (
            getBottomBound() == se.getBottomBound() && getTopBound() == se.getTopBound()))
            return true; 
        case "LEFT":
        case "RIGHT":
          if (Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound()) || 
            Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound()) || (
            getLeftBound() == se.getLeftBound() && getRightBound() == se.getRightBound()))
            return true; 
      } 
    } 
    return crashDetection(tx, ty, list);
  }
  
  private void updateImage() {
    switch (this.moveType) {
      case "STOP":
        this.moveX = 0;
        break;
      case "LEFT":
        this.moveX = 1;
        break;
      case "RIGHT":
        this.moveX = 2;
        break;
      case "TOP":
        this.moveX = 3;
        break;
      case "DOWN":
        this.moveX = 0;
        break;
    } 
  }
  
  private boolean crashDetection(int tx, int ty, List<SuperElement> list) {
    int bias = 1;
    int THRESHOLD = 25;
    Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
    Random random = new Random();
    GameMap gameMap = ElementManager.getManager().getGameMap();
    for (SuperElement se : list) {
      Rectangle elementRect = new Rectangle(se.getX() + bias, se.getY() + bias, se.getW() - bias, se.getH() - bias);
      if (playerRect.intersects(elementRect)) {
        int width;
        int i;
        int height;
        int j;
        switch (this.moveType) {
          case "TOP":
          case "DOWN":
            width = Math.min(getX() + getW(), se.getX() + se.getW()) - Math.max(getX(), se.getX());
            if (width > THRESHOLD)
              break; 
            if (getX() < se.getX()) {
              if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), 
                    getTopBound() - 10)))
                break; 
              if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getBottomBound() + 10)))
                break; 
              for (int k = 0; k < width; k++) {
                if (random.nextBoolean())
                  setX(getX() - 1); 
              } 
              break;
            } 
            if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getTopBound() - 10)))
              break; 
            if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getBottomBound() + 10)))
              break; 
            for (i = 0; i < width; i++) {
              if (random.nextBoolean())
                setX(getX() + 1); 
            } 
            break;
          case "LEFT":
          case "RIGHT":
            height = Math.min(getY() + getH(), se.getY() + se.getH()) - Math.max(getY(), se.getY());
            if (height > THRESHOLD)
              break; 
            if (getY() < se.getY()) {
              if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getTopBound())))
                break; 
              if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getTopBound())))
                break; 
              for (int k = 0; k < height; k++) {
                if (random.nextBoolean())
                  setY(getY() - 1); 
              } 
              break;
            } 
            if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getBottomBound())))
              break; 
            if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getBottomBound())))
              break; 
            for (j = 0; j < height; j++) {
              if (random.nextBoolean())
                setY(getY() + 1); 
            } 
            break;
        } 
        return false;
      } 
    } 
    return true;
  }
  
  public void destroy() {}
  
  public void handleCommandAction(ControlAction action) {
    setAttack(action.isReleaseBoom());
    String moveType = action.getMoveType();
    if (moveType == null)
      moveType = "STOP"; 
    setMoveType(moveType);
    List<Integer> rowCol = NpcUserHelper.getIJ(getX(), getY());
    int r = ((Integer)rowCol.get(0)).intValue();
    int c = ((Integer)rowCol.get(1)).intValue();
    if (this.lastStepRow == r && this.lastStepCol == c)
      longStopHandle(); 
    this.lastStepRow = r;
    this.lastStepCol = c;
  }
  
  public CurrentFullNpcData getCurrentFullNpcData() {
    CurrentFullNpcData data = new CurrentFullNpcData();
    data.setSelfNpcId(this.npcUserInfo.getNpcId());
    data.setSlefLocationX(getX());
    data.setSlefLocationY(getY());
    data.setMyScore(this.score);
    GameMap gameMap = ElementManager.getManager().getGameMap();
    GameMapData gameMapData = GameMapData.convertToGameMapData(gameMap);
    data.setGameMap(gameMapData);
    return data;
  }
  
  public void scoreChange(int delta) {
    this.score += delta;
  }
}
