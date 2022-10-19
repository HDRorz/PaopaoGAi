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
/*  34 */     this.imgList = imgList;
/*     */   }
/*     */   
/*     */   public void setMoveX(int moveX) {
/*  34 */     this.moveX = moveX;
/*     */   }
/*     */   
/*     */   public void setImgW(int imgW) {
/*  34 */     this.imgW = imgW;
/*     */   }
/*     */   
/*     */   public void setImgH(int imgH) {
/*  34 */     this.imgH = imgH;
/*     */   }
/*     */   
/*     */   public void setNpcUserInfo(NpcUserInfo npcUserInfo) {
/*  34 */     this.npcUserInfo = npcUserInfo;
/*     */   }
/*     */   
/*     */   public void setNpcIndex(int npcIndex) {
/*  34 */     this.npcIndex = npcIndex;
/*     */   }
/*     */   
/*     */   public void setAttack(boolean attack) {
/*  34 */     this.attack = attack;
/*     */   }
/*     */   
/*     */   public void setStep(int step) {
/*  34 */     this.step = step;
/*     */   }
/*     */   
/*     */   public void setDeltaMove(int deltaMove) {
/*  34 */     this.deltaMove = deltaMove;
/*     */   }
/*     */   
/*     */   public void setStopStepCount(AtomicInteger stopStepCount) {
/*  34 */     this.stopStepCount = stopStepCount;
/*     */   }
/*     */   
/*     */   public void setTotalThrowBoomCount(AtomicInteger totalThrowBoomCount) {
/*  34 */     this.totalThrowBoomCount = totalThrowBoomCount;
/*     */   }
/*     */   
/*     */   public void setLastStepRow(int lastStepRow) {
/*  34 */     this.lastStepRow = lastStepRow;
/*     */   }
/*     */   
/*     */   public void setLastStepCol(int lastStepCol) {
/*  34 */     this.lastStepCol = lastStepCol;
/*     */   }
/*     */   
/*     */   public void setPath(Vector<MoveType> path) {
/*  34 */     this.path = path;
/*     */   }
/*     */   
/*  36 */   final String DANGER_MARKER = "-1";
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
/*  36 */     getClass();
/*  36 */     return "-1";
/*     */   }
/*     */   
/*     */   public List<ImageIcon> getImgList() {
/*  38 */     return this.imgList;
/*     */   }
/*     */   
/*     */   public int getMoveX() {
/*  39 */     return this.moveX;
/*     */   }
/*     */   
/*     */   public int getImgW() {
/*  40 */     return this.imgW;
/*     */   }
/*     */   
/*     */   public int getImgH() {
/*  41 */     return this.imgH;
/*     */   }
/*     */   
/*     */   public NpcUserInfo getNpcUserInfo() {
/*  42 */     return this.npcUserInfo;
/*     */   }
/*     */   
/*     */   public int getNpcIndex() {
/*  44 */     return this.npcIndex;
/*     */   }
/*     */   
/*     */   public boolean isAttack() {
/*  45 */     return this.attack;
/*     */   }
/*     */   
/*  50 */   private int step = 0;
/*     */   
/*     */   public int getStep() {
/*  50 */     return this.step;
/*     */   }
/*     */   
/*  53 */   private int deltaMove = 0;
/*     */   
/*     */   public int getDeltaMove() {
/*  53 */     return this.deltaMove;
/*     */   }
/*     */   
/*  56 */   private AtomicInteger stopStepCount = new AtomicInteger(0);
/*     */   
/*     */   public AtomicInteger getStopStepCount() {
/*  56 */     return this.stopStepCount;
/*     */   }
/*     */   
/*  59 */   private AtomicInteger totalThrowBoomCount = new AtomicInteger(0);
/*     */   
/*     */   public AtomicInteger getTotalThrowBoomCount() {
/*  59 */     return this.totalThrowBoomCount;
/*     */   }
/*     */   
/*  62 */   private int lastStepRow = -1;
/*     */   
/*     */   public int getLastStepRow() {
/*  62 */     return this.lastStepRow;
/*     */   }
/*     */   
/*  63 */   private int lastStepCol = -1;
/*     */   
/*     */   private Vector<MoveType> path;
/*     */   
/*     */   public int getLastStepCol() {
/*  63 */     return this.lastStepCol;
/*     */   }
/*     */   
/*     */   public Vector<MoveType> getPath() {
/*  65 */     return this.path;
/*     */   }
/*     */   
/*     */   public Npc(int x, int y, int w, int h, int imgW, int imgH, List<ImageIcon> img, NpcUserInfo npcUserInfo, int npcIndex) {
/*  69 */     super(x, y, w, h);
/*  70 */     this.imgW = imgW;
/*  71 */     this.imgH = imgH;
/*  72 */     this.imgList = new ArrayList<>(img);
/*  73 */     this.path = new Vector<>();
/*  74 */     this.npcUserInfo = npcUserInfo;
/*  76 */     this.moveX = 0;
/*  78 */     this.moveType = "STOP";
/*  79 */     this.npcIndex = npcIndex;
/*  81 */     setSquareType(SquareType.NPC);
/*     */   }
/*     */   
/*     */   public static Npc createNpc(List<String> data, int i, int j, NpcUserInfo npcUserInfo, int npcIndex) {
/*  95 */     List<ImageIcon> imageList = new ArrayList<>(ElementLoader.getElementLoader().getNpcImageList(data.get(0)));
/*  96 */     int x = j * 64;
/*  97 */     int y = i * 64;
/*  98 */     int w = 64;
/*  99 */     int h = 64;
/* 100 */     int imgW = Integer.parseInt(data.get(3));
/* 101 */     int imgH = Integer.parseInt(data.get(4));
/* 102 */     return new Npc(x, y, w, h, imgW, imgH, imageList, npcUserInfo, npcIndex);
/*     */   }
/*     */   
/*     */   public void showElement(Graphics g) {
/* 107 */     if (!this.isShowing)
/*     */       return; 
/* 108 */     g.drawImage(((ImageIcon)this.imgList.get(this.moveX)).getImage(), 
/* 109 */         getX(), getY(), 
/* 110 */         getX() + getW(), getY() + getH(), 0, 0, 
/*     */         
/* 112 */         getImgW(), getImgH(), null);
/* 114 */     g.setFont(new Font("微软雅黑", 1, 24));
/* 115 */     g.setColor(getNpcUserInfo().getShowColor());
/* 116 */     g.drawString(getNpcUserInfo().getNpcName(), getX(), getY());
/* 119 */     String string = this.npcUserInfo + ": " + (isDead() ? "DEAD" : String.valueOf(this.score));
/* 120 */     g.setColor(getNpcUserInfo().getShowColor());
/* 121 */     g.setFont(new Font("微软雅黑", 1, 24));
/* 122 */     g.drawString(string, 0, (this.npcIndex + 1) * 64 / 2);
/*     */   }
/*     */   
/*     */   public void update() {
/* 127 */     if (!this.dead) {
/* 128 */       move();
/* 129 */       addBubble();
/* 130 */       updateImage();
/* 131 */       destroy();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addBubble() {
/* 143 */     List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX() + getW() / 2, getY() + getH() / 2));
/* 144 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 145 */     List<Integer> maplist = GameMap.getIJ(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue());
/* 146 */     if (this.attack && !this.dead && gameMap
/* 147 */       .getBlockSquareType(((Integer)maplist.get(0)).intValue(), ((Integer)maplist.get(1)).intValue()) != SquareType.BUBBLE) {
/* 150 */       List<SuperElement> list = ElementManager.getManager().getElementList("bubble");
/* 151 */       list.add(Bubble.createBubble(((Integer)loc.get(0)).intValue(), ((Integer)loc.get(1)).intValue(), 
/* 152 */             (List<String>)ElementLoader.getElementLoader().getGameInfoMap().get("bubble"), this.npcUserInfo.getNpcId(), 
/* 153 */             getBubblePower()));
/* 154 */       this.attack = false;
/* 155 */       this.bubbleNum++;
/* 157 */       this.totalThrowBoomCount.incrementAndGet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void longStopHandle() {
	if (!"STOP".equals(moveType)) {
      scoreChange(-10);
	}
/* 165 */     int stopSteps = this.stopStepCount.incrementAndGet();
/* 166 */     if (stopSteps > 5) {
/* 167 */       System.out.println(this.npcUserInfo + "长时间停留同一位置不动，扣除100分！");
/* 168 */       scoreChange(-100);
/* 171 */       this.stopStepCount.set(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void move() {
/* 187 */     int tx = getX();
/* 188 */     int ty = getY();
/* 190 */     switch (this.moveType) {
/*     */       case "TOP":
/* 192 */         ty -= this.speed;
/*     */         break;
/*     */       case "LEFT":
/* 195 */         tx -= this.speed;
/*     */         break;
/*     */       case "RIGHT":
/* 198 */         tx += this.speed;
/*     */         break;
/*     */       case "DOWN":
/* 201 */         ty += this.speed;
/*     */         break;
/*     */       case "STOP":
/* 204 */         this.deltaMove = 0;
/*     */         break;
/*     */     } 
/* 210 */     if (this.deltaMove <= 0) {
/* 211 */       System.out.println("本次操作已经走到目标位置，等待下一指令,当前坐标（" + tx + "," + ty + ")");
/* 212 */       setMoveType("STOP");
/*     */       return;
/*     */     } 
/* 217 */     boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
/* 218 */     boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
/* 219 */     boolean det3 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));
/* 220 */     if (det1 && det2 && det3) {
/* 221 */       setX(tx);
/* 222 */       setY(ty);
/* 225 */       this.deltaMove -= this.speed;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
/* 230 */     for (SuperElement se : list) {
/* 231 */       switch (this.moveType) {
/*     */         case "TOP":
/*     */         case "DOWN":
/* 234 */           if (Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound()) || 
/* 235 */             Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound()) || (
/* 236 */             getBottomBound() == se.getBottomBound() && getTopBound() == se.getTopBound()))
/* 237 */             return true; 
/*     */         case "LEFT":
/*     */         case "RIGHT":
/* 242 */           if (Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound()) || 
/* 243 */             Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound()) || (
/* 244 */             getLeftBound() == se.getLeftBound() && getRightBound() == se.getRightBound()))
/* 245 */             return true; 
/*     */       } 
/*     */     } 
/* 252 */     return crashDetection(tx, ty, list);
/*     */   }
/*     */   
/*     */   private void updateImage() {
/* 256 */     switch (this.moveType) {
/*     */       case "STOP":
/* 258 */         this.moveX = 0;
/*     */         break;
/*     */       case "LEFT":
/* 261 */         this.moveX = 1;
/*     */         break;
/*     */       case "RIGHT":
/* 264 */         this.moveX = 2;
/*     */         break;
/*     */       case "TOP":
/* 267 */         this.moveX = 3;
/*     */         break;
/*     */       case "DOWN":
/* 270 */         this.moveX = 0;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean crashDetection(int tx, int ty, List<SuperElement> list) {
/* 284 */     int bias = 1;
/* 285 */     int THRESHOLD = 25;
/* 286 */     Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
/* 287 */     Random random = new Random();
/* 288 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 290 */     for (SuperElement se : list) {
/* 292 */       Rectangle elementRect = new Rectangle(se.getX() + bias, se.getY() + bias, se.getW() - bias, se.getH() - bias);
/* 293 */       if (playerRect.intersects(elementRect)) {
/*     */         int width;
/*     */         int i;
/*     */         int height;
/*     */         int j;
/* 294 */         switch (this.moveType) {
/*     */           case "TOP":
/*     */           case "DOWN":
/* 297 */             width = Math.min(getX() + getW(), se.getX() + se.getW()) - Math.max(getX(), se.getX());
/* 298 */             if (width > THRESHOLD)
/*     */               break; 
/* 300 */             if (getX() < se.getX()) {
/* 301 */               if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), 
/* 302 */                     getTopBound() - 10)))
/*     */                 break; 
/* 304 */               if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getBottomBound() + 10)))
/*     */                 break; 
/* 306 */               for (int k = 0; k < width; k++) {
/* 307 */                 if (random.nextBoolean())
/* 308 */                   setX(getX() - 1); 
/*     */               } 
/*     */               break;
/*     */             } 
/* 311 */             if (this.moveType == "TOP" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getTopBound() - 10)))
/*     */               break; 
/* 313 */             if (this.moveType == "DOWN" && !gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getBottomBound() + 10)))
/*     */               break; 
/* 315 */             for (i = 0; i < width; i++) {
/* 316 */               if (random.nextBoolean())
/* 317 */                 setX(getX() + 1); 
/*     */             } 
/*     */             break;
/*     */           case "LEFT":
/*     */           case "RIGHT":
/* 323 */             height = Math.min(getY() + getH(), se.getY() + se.getH()) - Math.max(getY(), se.getY());
/* 324 */             if (height > THRESHOLD)
/*     */               break; 
/* 326 */             if (getY() < se.getY()) {
/* 327 */               if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getTopBound())))
/*     */                 break; 
/* 329 */               if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getTopBound())))
/*     */                 break; 
/* 331 */               for (int k = 0; k < height; k++) {
/* 332 */                 if (random.nextBoolean())
/* 333 */                   setY(getY() - 1); 
/*     */               } 
/*     */               break;
/*     */             } 
/* 336 */             if (this.moveType == "LEFT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() - 10, getBottomBound())))
/*     */               break; 
/* 338 */             if (this.moveType == "RIGHT" && !gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound() + 10, getBottomBound())))
/*     */               break; 
/* 340 */             for (j = 0; j < height; j++) {
/* 341 */               if (random.nextBoolean())
/* 342 */                 setY(getY() + 1); 
/*     */             } 
/*     */             break;
/*     */         } 
/* 349 */         return false;
/*     */       } 
/*     */     } 
/* 352 */     return true;
/*     */   }
/*     */   
/*     */   public void destroy() {}
/*     */   
/*     */   private void resetDeltaMove(String moveType) {
/* 362 */     if ("STOP".equals(moveType)) {
/* 363 */       this.deltaMove = 0;
/*     */     } else {
/* 365 */       this.deltaMove = 64;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleCommandAction(ControlAction action) {
/* 376 */     System.out.println("当前坐标：(" + getX() + "," + getY() + ")");
/* 377 */     setAttack(action.isReleaseBoom());
/* 379 */     String moveType = action.getMoveType();
/* 380 */     if (moveType == null)
/* 381 */       moveType = "STOP"; 
/* 383 */     setMoveType(moveType);
/* 386 */     resetDeltaMove(moveType);
/* 389 */     List<Integer> rowCol = NpcUserHelper.getIJ(getX(), getY());
/* 390 */     int r = ((Integer)rowCol.get(0)).intValue();
/* 391 */     int c = ((Integer)rowCol.get(1)).intValue();
/* 392 */     if (this.lastStepRow == r && this.lastStepCol == c) {
/* 393 */       longStopHandle(); 
			  } else if (!"STOP".equals(moveType)) {
                scoreChange(10);
			  }
/* 397 */     this.lastStepRow = r;
/* 398 */     this.lastStepCol = c;
/*     */   }
/*     */   
/*     */   public CurrentFullNpcData getCurrentFullNpcData() {
/* 402 */     CurrentFullNpcData data = new CurrentFullNpcData();
/* 403 */     data.setSelfNpcId(this.npcUserInfo.getNpcId());
/* 405 */     data.setSlefLocationX(getX());
/* 406 */     data.setSlefLocationY(getY());
/* 410 */     data.setMyScore(this.score);
/* 412 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/* 413 */     GameMapData gameMapData = GameMapData.convertToGameMapData(gameMap);
/* 414 */     data.setGameMap(gameMapData);
/* 415 */     return data;
/*     */   }
/*     */   
/*     */   public void scoreChange(int delta) {
/* 419 */     this.score += delta;
/*     */   }
/*     */   
/*     */   public int getRankCaculateScore() {
/* 424 */     int alivePart = isDead() ? 0 : 1;
/* 426 */     return 100000 * alivePart + 10 * getScore() + getTotalThrowBoomNum();
/*     */   }
/*     */   
/*     */   public int getTotalThrowBoomNum() {
/* 430 */     return this.totalThrowBoomCount.get();
/*     */   }
/*     */ }


/* Location:              E:\otherproject\PaopaoGAi\server2.1\SuperBooman.jar!\com\em\programming\game\model\vo\Npc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */