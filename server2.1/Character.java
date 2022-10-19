/*     */ package com.em.programming.game.model.vo;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ 
/*     */ public class Character extends SuperElement {
/*     */   protected boolean dead;
/*     */   
/*     */   protected String moveType;
/*     */   
/*     */   protected int speed;
/*     */   
/*     */   protected int speedItemCount;
/*     */   
/*     */   protected int changeDirectionCount;
/*     */   
/*     */   protected int stopitemCount;
/*     */   
/*     */   protected int bubblePower;
/*     */   
/*     */   protected int bubbleNum;
/*     */   
/*     */   protected int bubbleLargest;
/*     */   
/*     */   public int score;
/*     */   
/*     */   protected int heathPoint;
/*     */   
/*     */   protected boolean isUnstoppable;
/*     */   
/*     */   protected int unstoppableCount;
/*     */   
/*     */   protected boolean isShowing;
/*     */   
/*     */   public Character(int x, int y, int w, int h) {
/*  37 */     super(x, y, w, h);
/*  38 */     this.moveType = "STOP";
/*  39 */     this.speedItemCount = 0;
/*  40 */     this.changeDirectionCount = 0;
/*  41 */     this.stopitemCount = 0;
/*  42 */     this.bubblePower = 1;
/*  43 */     this.bubbleNum = 0;
/*  44 */     this.bubbleLargest = 1;
/*  45 */     this.heathPoint = 1;
/*  46 */     this.isUnstoppable = false;
/*  47 */     this.unstoppableCount = 0;
/*  48 */     this.isShowing = true;
/*  49 */     this.speed = 4;
/*  50 */     this.score = 100;
/*  51 */     this.dead = false;
/*     */   }
/*     */   
/*     */   public void setHealthPoint(int change) {
/*  55 */     if (change < 0) {
/*  57 */       if (this.isUnstoppable)
/*     */         return; 
/*  58 */       setUnstoppable(3);
/*     */     } 
/*  60 */     this.heathPoint += change;
/*     */   }
/*     */   
/*     */   public void changeSpeed(double times, int lastTime) {
/*  74 */     this.speed = (int)(this.speed * times);
/*  75 */     Timer timer = new Timer(true);
/*  76 */     this.speedItemCount++;
/*  77 */     TimerTask task = new TimerTask() {
/*     */         public void run() {
/*  80 */           Character.this.speedItemCount--;
/*  81 */           if (Character.this.speedItemCount == 0)
/*  82 */             Character.this.speed = 4; 
/*     */         }
/*     */       };
/*  86 */     timer.schedule(task, (lastTime * 1000));
/*     */   }
/*     */   
/*     */   public void changeDirection(int lastTime) {
/*  91 */     this.speed = -this.speed;
/*  92 */     Timer timer = new Timer(true);
/*  93 */     this.changeDirectionCount++;
/*  94 */     TimerTask task = new TimerTask() {
/*     */         public void run() {
/*  97 */           Character.this.changeDirectionCount--;
/*  98 */           if (Character.this.changeDirectionCount == 0)
/*  99 */             Character.this.speed = 4; 
/*     */         }
/*     */       };
/* 104 */     timer.schedule(task, (lastTime * 1000));
/*     */   }
/*     */   
/*     */   public void setUnstoppable(int lastTime) {
/* 152 */     this.isUnstoppable = true;
/* 153 */     this.unstoppableCount++;
/* 154 */     unstoppableChangeImg(lastTime);
/* 155 */     Timer timer = new Timer(true);
/* 156 */     TimerTask task = new TimerTask() {
/*     */         public void run() {
/* 159 */           Character.this.unstoppableCount--;
/* 160 */           if (Character.this.unstoppableCount == 0)
/* 160 */             Character.this.isUnstoppable = false; 
/*     */         }
/*     */       };
/* 163 */     timer.schedule(task, (lastTime * 1000));
/*     */   }
/*     */   
/*     */   public void unstoppableChangeImg(int lastTime) {
/* 167 */     Timer timer = new Timer();
/* 168 */     final int times = lastTime * 1000 / 100;
/* 169 */     TimerTask task1 = new TimerTask() {
/* 170 */         int count = 0;
/*     */         
/*     */         public void run() {
/* 173 */           Character.this.isShowing = false;
/* 174 */           this.count++;
/* 175 */           if (this.count == times / 5) {
/* 176 */             Character.this.isShowing = true;
/* 177 */             cancel();
/*     */           } 
/*     */         }
/*     */       };
/* 181 */     TimerTask task2 = new TimerTask() {
/* 182 */         int count = 0;
/*     */         
/*     */         public void run() {
/* 185 */           Character.this.isShowing = true;
/* 186 */           this.count++;
/* 187 */           if (this.count == times)
/* 187 */             cancel(); 
/*     */         }
/*     */       };
/* 190 */     timer.scheduleAtFixedRate(task1, 0L, 500L);
/* 191 */     timer.scheduleAtFixedRate(task2, 0L, 100L);
/*     */   }
/*     */   
/*     */   public void bubbleAddPower() {
/* 195 */     this.bubblePower++;
/*     */   }
/*     */   
/*     */   public void showElement(Graphics g) {}
/*     */   
/*     */   public void move() {}
/*     */   
/*     */   public void destroy() {}
/*     */   
/*     */   public boolean isDead() {
/* 209 */     return this.dead;
/*     */   }
/*     */   
/*     */   public void setDead(boolean dead) {
/* 213 */     this.dead = dead;
/*     */   }
/*     */   
/*     */   public String getMoveType() {
/* 217 */     return this.moveType;
/*     */   }
/*     */   
/*     */   public void setMoveType(String moveType) {
/* 221 */     this.moveType = moveType;
/*     */   }
/*     */   
/*     */   public int getSpeed() {
/* 225 */     return this.speed;
/*     */   }
/*     */   
/*     */   public void setSpeed(int speed) {
/* 229 */     this.speed = speed;
/*     */   }
/*     */   
/*     */   public int getSpeedItemCount() {
/* 233 */     return this.speedItemCount;
/*     */   }
/*     */   
/*     */   public void setSpeedItemCount(int speedItemCount) {
/* 237 */     this.speedItemCount = speedItemCount;
/*     */   }
/*     */   
/*     */   public int getBubblePower() {
/* 241 */     return this.bubblePower;
/*     */   }
/*     */   
/*     */   public void setBubblePower(int bubblePower) {
/* 245 */     this.bubblePower = bubblePower;
/*     */   }
/*     */   
/*     */   public int getBubbleNum() {
/* 249 */     return this.bubbleNum;
/*     */   }
/*     */   
/*     */   public void setBubbleNum(int bubbleNum) {
/* 253 */     this.bubbleNum = bubbleNum;
/*     */   }
/*     */   
/*     */   public int getBubbleLargest() {
/* 257 */     return this.bubbleLargest;
/*     */   }
/*     */   
/*     */   public void setBubbleLargest(int bubbleLargest) {
/* 261 */     this.bubbleLargest = bubbleLargest;
/*     */   }
/*     */   
/*     */   public int getScore() {
/* 265 */     return this.score;
/*     */   }
/*     */   
/*     */   public void setScore(int score) {
/* 269 */     this.score = score;
/*     */   }
/*     */   
/*     */   public int getChangeDirectionCount() {
/* 275 */     return this.changeDirectionCount;
/*     */   }
/*     */   
/*     */   public void setChangeDirectionCount(int changeDirectionCount) {
/* 279 */     this.changeDirectionCount = changeDirectionCount;
/*     */   }
/*     */   
/*     */   public int getStopitemCount() {
/* 283 */     return this.stopitemCount;
/*     */   }
/*     */   
/*     */   public void setStopitemCount(int stopitemCount) {
/* 287 */     this.stopitemCount = stopitemCount;
/*     */   }
/*     */   
/*     */   public int getHeathPoint() {
/* 291 */     return this.heathPoint;
/*     */   }
/*     */   
/*     */   public boolean isisUnstoppable() {
/* 295 */     return this.isUnstoppable;
/*     */   }
/*     */   
/*     */   public void setisUnstoppable(boolean unstoppable) {
/* 299 */     this.isUnstoppable = unstoppable;
/*     */   }
/*     */ }


/* Location:              E:\otherproject\PaopaoGAi\server2.1\SuperBooman.jar!\com\em\programming\game\model\vo\Character.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */