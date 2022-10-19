/*     */ package com.em.programming.game.model.vo;
/*     */ 
/*     */ import com.em.programming.game.consts.SquareType;
/*     */ import com.em.programming.game.control.ControlAction;
/*     */ import com.em.programming.game.control.CurrentFullNpcData;
/*     */ import com.em.programming.game.control.GameMapData;
/*     */ import com.em.programming.game.datastore.NpcUserInfo;
/*     */ import com.em.programming.game.model.loader.ElementLoader;
/*     */ import com.em.programming.game.model.manager.ElementManager;
/*     */ import com.em.programming.game.model.manager.GameMap;
/*     */ import com.em.programming.game.model.manager.MoveType;
/*     */ import com.em.programming.game.stat.GameShowMsgQueue;
/*     */ import com.em.programming.game.stat.NpcScoreHelper;
/*     */ import com.em.programming.game.test.NpcUserHelper;
/*     */ import com.em.programming.game.util.Utils;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.swing.ImageIcon;
/*     */ 
/*     */ public class Npc extends Character {
/*     */   public void setImgList(List<ImageIcon> imgList) {
/*  36 */     this.imgList = imgList;
/*     */   }
/*     */   
/*     */   public void setMoveX(int moveX) {
/*  36 */     this.moveX = moveX;
/*     */   }
/*     */   
/*     */   public void setImgW(int imgW) {
/*  36 */     this.imgW = imgW;
/*     */   }
/*     */   
/*     */   public void setImgH(int imgH) {
/*  36 */     this.imgH = imgH;
/*     */   }
/*     */   
/*     */   public void setNpcUserInfo(NpcUserInfo npcUserInfo) {
/*  36 */     this.npcUserInfo = npcUserInfo;
/*     */   }
/*     */   
/*     */   public void setNpcIndex(int npcIndex) {
/*  36 */     this.npcIndex = npcIndex;
/*     */   }
/*     */   
/*     */   public void setAttack(boolean attack) {
/*  36 */     this.attack = attack;
/*     */   }
/*     */   
/*     */   public void setStep(int step) {
/*  36 */     this.step = step;
/*     */   }
/*     */   
/*     */   public void setDeltaMove(int deltaMove) {
/*  36 */     this.deltaMove = deltaMove;
/*     */   }
/*     */   
/*     */   public void setStopStepCount(AtomicInteger stopStepCount) {
/*  36 */     this.stopStepCount = stopStepCount;
/*     */   }
/*     */   
/*     */   public void setNoThrowBombCount(AtomicInteger noThrowBombCount) {
/*  36 */     this.noThrowBombCount = noThrowBombCount;
/*     */   }
/*     */   
/*     */   public void setTotalThrowBoomCount(AtomicInteger totalThrowBoomCount) {
/*  36 */     this.totalThrowBoomCount = totalThrowBoomCount;
/*     */   }
/*     */   
/*     */   public void setLastStepRow(int lastStepRow) {
/*  36 */     this.lastStepRow = lastStepRow;
/*     */   }
/*     */   
/*     */   public void setLastStepCol(int lastStepCol) {
/*  36 */     this.lastStepCol = lastStepCol;
/*     */   }
/*     */   
/*     */   public void setPath(Vector<MoveType> path) {
/*  36 */     this.path = path;
/*     */   }
/*     */   
/*  38 */   final String DANGER_MARKER = "-1";
/*     */   
/*     */   private List<ImageIcon> imgList;
/*     */   
/*     */   private int moveX;
/*     */   
/*     */   private int imgW;
/*     */   
/*     */   private int imgH;
/*     */   
/*     */   private NpcUserInfo npcUserInfo;
/*     */   
/*     */   private int npcIndex;
/*     */   
/*     */   private boolean attack;
/*     */   
/*     */   public String getDANGER_MARKER() {
/*  38 */     getClass();
/*  38 */     return "-1";
/*     */   }
/*     */   
/*     */   public List<ImageIcon> getImgList() {
/*  40 */     return this.imgList;
/*     */   }
/*     */   
/*     */   public int getMoveX() {
/*  41 */     return this.moveX;
/*     */   }
/*     */   
/*     */   public int getImgW() {
/*  42 */     return this.imgW;
/*     */   }
/*     */   
/*     */   public int getImgH() {
/*  43 */     return this.imgH;
/*     */   }
/*     */   
/*     */   public NpcUserInfo getNpcUserInfo() {
/*  44 */     return this.npcUserInfo;
/*     */   }
/*     */   
/*     */   public int getNpcIndex() {
/*  46 */     return this.npcIndex;
/*     */   }
/*     */   
/*     */   public boolean isAttack() {
/*  47 */     return this.attack;
/*     */   }
/*     */   
/*  52 */   private int step = 0;
/*     */   
/*     */   public int getStep() {
/*  52 */     return this.step;
/*     */   }
/*     */   
/*  55 */   private int deltaMove = 0;
/*     */   
/*     */   public int getDeltaMove() {
/*  55 */     return this.deltaMove;
/*     */   }
/*     */   
/*  58 */   private AtomicInteger stopStepCount = new AtomicInteger(0);
/*     */   
/*     */   public AtomicInteger getStopStepCount() {
/*  58 */     return this.stopStepCount;
/*     */   }
/*     */   
/*  60 */   private AtomicInteger noThrowBombCount = new AtomicInteger(0);
/*     */   
/*     */   public AtomicInteger getNoThrowBombCount() {
/*  60 */     return this.noThrowBombCount;
/*     */   }
/*     */   
/*  63 */   private AtomicInteger totalThrowBoomCount = new AtomicInteger(0);
/*     */   
/*     */   public AtomicInteger getTotalThrowBoomCount() {
/*  63 */     return this.totalThrowBoomCount;
/*     */   }
/*     */   
/*  66 */   private int lastStepRow = -1;
/*     */   
/*     */   public int getLastStepRow() {
/*  66 */     return this.lastStepRow;
/*     */   }
/*     */   
/*  67 */   private int lastStepCol = -1;
/*     */   
/*     */   private Vector<MoveType> path;
/*     */   
/*     */   public int getLastStepCol() {
/*  67 */     return this.lastStepCol;
/*     */   }
/*     */   
/*     */   public Vector<MoveType> getPath() {
/*  69 */     return this.path;
/*     */   }
/*     */   
/*     */   public Npc(int x, int y, int w, int h, int imgW, int imgH, List<ImageIcon> img, NpcUserInfo npcUserInfo, int npcIndex) {
/*  73 */     super(x, y, w, h);
/*  74 */     this.imgW = imgW;
/*  75 */     this.imgH = imgH;
/*  76 */     this.imgList = new ArrayList<>(img);
/*  77 */     this.path = new Vector<>();
/*  78 */     this.npcUserInfo = npcUserInfo;
/*  80 */     this.moveX = 0;
/*  82 */     this.moveType = "STOP";
/*  83 */     this.npcIndex = npcIndex;
/*  85 */     setSquareType(SquareType.NPC);
/*     */   }
/*     */   
/*     */   public static Npc createNpc(List<String> data, int i, int j, NpcUserInfo npcUserInfo, int npcIndex) {
/*  99 */     List<ImageIcon> imageList = new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
/* 100 */     int x = j * 64;
/* 101 */     int y = i * 64;
/* 102 */     int w = 64;
/* 103 */     int h = 64;
/* 104 */     int imgW = Integer.parseInt(data.get(3));
/* 105 */     int imgH = Integer.parseInt(data.get(4));
/* 106 */     return new Npc(x, y, w, h, imgW, imgH, imageList, npcUserInfo, npcIndex);
/*     */   }
/*     */   
/*     */   public void showElement(Graphics g) {
/* 111 */     if (!this.isShowing)
/*     */       return; 
/* 112 */     g.drawImage(((ImageIcon)this.imgList.get(this.moveX)).getImage(), 
/* 113 */         getX(), getY(), 
/* 114 */         getX() + getW(), getY() + getH(), 0, 0, 
/*     */         
/* 116 */         getImgW(), getImgH(), null);
/* 118 */     g.setFont(new Font("微软雅黑", 1, 24));
/* 119 */     g.setColor(getNpcUserInfo().getShowColor());
/* 120 */     g.drawString(getNpcUserInfo().getNpcName(), getX(), getY());
/* 123 */     String string = this.npcUserInfo + ": " + (isDead() ? "DEAD" : String.valueOf(this.score));
/* 124 */     g.setColor(getNpcUserInfo().getShowColor());
/* 125 */     g.setFont(new Font("微软雅黑", 1, 24));
/* 126 */     g.drawString(string, 0, (this.npcIndex + 1) * 64 / 2);
/*     */   }
/*     */   
/*     */   public void update() {
/* 131 */     if (!this.dead) {
/* 132 */       move();
/* 133 */       addBubble();
/* 134 */       updateImage();
/* 135 */       destroy();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addBubble() {
/* 147 */     List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX() + getW() / 2, getY() + getH() / 2));
/* 148 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 149 */     List<Integer> maplist = GameMap.getIJ(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue());
/* 150 */     if (this.attack && !this.dead && gameMap
/* 151 */       .getBlockSquareType(((Integer)maplist.get(0)).intValue(), ((Integer)maplist.get(1)).intValue()) != SquareType.BUBBLE) {
/* 154 */       List<SuperElement> list = ElementManager.getManager().getElementList("bubble");
/* 155 */       list.add(Bubble.createBubble(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue(), 
/* 156 */             (List<String>)ElementLoader.getElementLoader().getGameInfoMap().get("bubble"), this.npcUserInfo.getNpcId(), 
/* 157 */             getBubblePower()));
/* 158 */       this.attack = false;
/* 159 */       this.bubbleNum++;
/* 162 */       this.noThrowBombCount.set(0);
/* 164 */       this.totalThrowBoomCount.incrementAndGet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void longStopHandle() {
	if (!"STOP".equals(moveType)) {
      scoreChange(-10);
	}
/* 172 */     int stopSteps = this.stopStepCount.incrementAndGet();
/* 173 */     if (stopSteps > 5) {
/* 174 */       NpcScoreHelper.punishLongStay(this);
/* 177 */       this.stopStepCount.set(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void longNoBombHandle() {
/* 188 */     int noThrowBombSteps = this.noThrowBombCount.incrementAndGet();
/* 189 */     if (noThrowBombSteps > 10) {
/* 190 */       NpcScoreHelper.punishLongNoBoom(this);
/* 192 */       this.noThrowBombCount.set(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDead(boolean isDead) {
/* 202 */     super.setDead(isDead);
/* 204 */     if (isDead) {
/* 205 */       setX(-100);
/* 206 */       setY(-100);
/* 208 */       String msg = getNpcUserInfo() + "被淘汰";
/* 209 */       System.out.println(msg);
/* 211 */       GameShowMsgQueue.put(msg);
/* 213 */       GameShowMsgQueue.putSpecialShow(this, "淘汰");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void move() {
/* 219 */     int tx = getX();
/* 220 */     int ty = getY();
/* 222 */     switch (this.moveType) {
/*     */       case "TOP":
/* 224 */         ty -= this.speed;
/*     */         break;
/*     */       case "LEFT":
/* 227 */         tx -= this.speed;
/*     */         break;
/*     */       case "RIGHT":
/* 230 */         tx += this.speed;
/*     */         break;
/*     */       case "DOWN":
/* 233 */         ty += this.speed;
/*     */         break;
/*     */       case "STOP":
/* 236 */         this.deltaMove = 0;
/*     */         break;
/*     */     } 
/* 242 */     if (this.deltaMove <= 0) {
/* 243 */       System.out.println("本次操作已经走到目标位置，等待下一指令,当前坐标（" + tx + "," + ty + ")");
/* 244 */       setMoveType("STOP");
/*     */       return;
/*     */     } 
/* 249 */     boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
/* 250 */     boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
/* 251 */     boolean det3 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));
/* 252 */     if (det1 && det2 && det3) {
/* 253 */       setX(tx);
/* 254 */       setY(ty);
/* 257 */       this.deltaMove -= this.speed;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
/* 262 */     for (SuperElement se : list) {
/* 263 */       switch (this.moveType) {
/*     */         case "TOP":
/*     */         case "DOWN":
/* 266 */           if (Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound()) || 
/* 267 */             Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound()) || (
/* 268 */             getBottomBound() == se.getBottomBound() && getTopBound() == se.getTopBound()))
/* 269 */             return true; 
/*     */         case "LEFT":
/*     */         case "RIGHT":
/* 274 */           if (Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound()) || 
/* 275 */             Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound()) || (
/* 276 */             getLeftBound() == se.getLeftBound() && getRightBound() == se.getRightBound()))
/* 277 */             return true; 
/*     */       } 
/*     */     } 
/* 284 */     return crashDetection(tx, ty, list);
/*     */   }
/*     */   
/*     */   private void updateImage() {
/* 288 */     switch (this.moveType) {
/*     */       case "STOP":
/* 290 */         this.moveX = 0;
/*     */         break;
/*     */       case "LEFT":
/* 293 */         this.moveX = 1;
/*     */         break;
/*     */       case "RIGHT":
/* 296 */         this.moveX = 2;
/*     */         break;
/*     */       case "TOP":
/* 299 */         this.moveX = 3;
/*     */         break;
/*     */       case "DOWN":
/* 302 */         this.moveX = 0;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean crashDetection(int tx, int ty, List<SuperElement> list) {
/* 316 */     int bias = 1;
/* 317 */     int THRESHOLD = 25;
/* 318 */     Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
/* 319 */     Random random = new Random();
/* 320 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 322 */     for (SuperElement se : list) {
/* 324 */       Rectangle elementRect = new Rectangle(se.getX() + bias, se.getY() + bias, se.getW() - bias, se.getH() - bias);
/* 325 */       if (playerRect.intersects(elementRect)) {
/*     */         int width;
/*     */         int i;
/*     */         int height;
/*     */         int j;
/* 326 */         switch (this.moveType) {
/*     */           case "TOP":
/*     */           case "DOWN":
/* 329 */             width = Math.min(getX() + getW(), se.getX() + se.getW()) - Math.max(getX(), se.getX());
/* 330 */             if (width > THRESHOLD)
/*     */               break; 
/* 332 */             if (getX() < se.getX()) {
/* 333 */               if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), 
/* 334 */                     getTopBound() - 10)))
/*     */                 break; 
/* 336 */               if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getBottomBound() + 10)))
/*     */                 break; 
/* 338 */               for (int k = 0; k < width; k++) {
/* 339 */                 if (random.nextBoolean())
/* 340 */                   setX(getX() - 1); 
/*     */               } 
/*     */               break;
/*     */             } 
/* 343 */             if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), 
/* 344 */                   getTopBound() - 10)))
/*     */               break; 
/* 346 */             if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getBottomBound() + 10)))
/*     */               break; 
/* 348 */             for (i = 0; i < width; i++) {
/* 349 */               if (random.nextBoolean())
/* 350 */                 setX(getX() + 1); 
/*     */             } 
/*     */             break;
/*     */           case "LEFT":
/*     */           case "RIGHT":
/* 356 */             height = Math.min(getY() + getH(), se.getY() + se.getH()) - Math.max(getY(), se.getY());
/* 357 */             if (height > THRESHOLD)
/*     */               break; 
/* 359 */             if (getY() < se.getY()) {
/* 360 */               if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getTopBound())))
/*     */                 break; 
/* 362 */               if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getTopBound())))
/*     */                 break; 
/* 364 */               for (int k = 0; k < height; k++) {
/* 365 */                 if (random.nextBoolean())
/* 366 */                   setY(getY() - 1); 
/*     */               } 
/*     */               break;
/*     */             } 
/* 369 */             if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getBottomBound())))
/*     */               break; 
/* 371 */             if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getBottomBound())))
/*     */               break; 
/* 373 */             for (j = 0; j < height; j++) {
/* 374 */               if (random.nextBoolean())
/* 375 */                 setY(getY() + 1); 
/*     */             } 
/*     */             break;
/*     */         } 
/* 382 */         return false;
/*     */       } 
/*     */     } 
/* 385 */     return true;
/*     */   }
/*     */   
/*     */   public void destroy() {}
/*     */   
/*     */   private void resetDeltaMove(String moveType) {
/* 395 */     if ("STOP".equals(moveType)) {
/* 396 */       this.deltaMove = 0;
/*     */     } else {
/* 398 */       this.deltaMove = 64;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleCommandAction(ControlAction action) {
/* 409 */     System.out.println("当前坐标：(" + getX() + "," + getY() + ")");
/* 410 */     setAttack(action.isReleaseBoom());
/* 412 */     String moveType = action.getMoveType();
/* 413 */     if (moveType == null)
/* 414 */       moveType = "STOP"; 
/* 416 */     setMoveType(moveType);
/* 419 */     resetDeltaMove(moveType);
/* 422 */     List<Integer> rowCol = NpcUserHelper.getIJ(getX(), getY());
/* 423 */     int r = ((Integer)rowCol.get(0)).intValue();
/* 424 */     int c = ((Integer)rowCol.get(1)).intValue();
/* 425 */     if (this.lastStepRow == r && this.lastStepCol == c) {
/* 426 */       longStopHandle(); 
				} else if (!"STOP".equals(moveType)) {
                scoreChange(10);
			  }
/* 430 */     longNoBombHandle();
/* 433 */     this.lastStepRow = r;
/* 434 */     this.lastStepCol = c;
/*     */   }
/*     */   
/*     */   public CurrentFullNpcData getCurrentFullNpcData() {
/* 438 */     CurrentFullNpcData data = new CurrentFullNpcData();
/* 439 */     data.setSelfNpcId(this.npcUserInfo.getNpcId());
/* 441 */     data.setSlefLocationX(getX());
/* 442 */     data.setSlefLocationY(getY());
/* 446 */     data.setMyScore(this.score);
/* 448 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 449 */     GameMapData gameMapData = GameMapData.convertToGameMapData(gameMap);
/* 450 */     data.setGameMap(gameMapData);
/* 451 */     return data;
/*     */   }
/*     */   
/*     */   public void scoreChange(int delta) {
/* 455 */     this.score += delta;
/*     */   }
/*     */   
/*     */   public int getRankCaculateScore() {
/* 460 */     int alivePart = isDead() ? 0 : 1;
/* 462 */     return 100000 * alivePart + 10 * getScore() + getTotalThrowBoomNum();
/*     */   }
/*     */   
/*     */   public int getTotalThrowBoomNum() {
/* 466 */     return this.totalThrowBoomCount.get();
/*     */   }
/*     */ }


/* Location:              E:\otherproject\PaopaoGAi\server2.2\SuperBooman.jar!\com\em\programming\game\model\vo\Npc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */