package com.em.programming.game.model.vo;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public class Character extends SuperElement {
  protected boolean dead;
  
  protected String moveType;
  
  protected int speed;
  
  protected int speedItemCount;
  
  protected int changeDirectionCount;
  
  protected int stopitemCount;
  
  protected int bubblePower;
  
  protected int bubbleNum;
  
  protected int bubbleLargest;
  
  public int score;
  
  protected int heathPoint;
  
  protected boolean isUnstoppable;
  
  protected int unstoppableCount;
  
  protected boolean isShowing;
  
  public Character(int x, int y, int w, int h) {
    super(x, y, w, h);
    this.moveType = "STOP";
    this.speedItemCount = 0;
    this.changeDirectionCount = 0;
    this.stopitemCount = 0;
    this.bubblePower = 1;
    this.bubbleNum = 0;
    this.bubbleLargest = 1;
    this.heathPoint = 1;
    this.isUnstoppable = false;
    this.unstoppableCount = 0;
    this.isShowing = true;
    this.speed = 4;
    this.score = 100;
    this.dead = false;
  }
  
  public void setHealthPoint(int change) {
    if (change < 0) {
      if (this.isUnstoppable)
        return; 
      setUnstoppable(3);
    } 
    this.heathPoint += change;
  }
  
  public void changeSpeed(double times, int lastTime) {
    this.speed = (int)(this.speed * times);
    Timer timer = new Timer(true);
    this.speedItemCount++;
    TimerTask task = new TimerTask() {
        public void run() {
          Character.this.speedItemCount--;
          if (Character.this.speedItemCount == 0)
            Character.this.speed = 4; 
        }
      };
    timer.schedule(task, (lastTime * 1000));
  }
  
  public void changeDirection(int lastTime) {
    this.speed = -this.speed;
    Timer timer = new Timer(true);
    this.changeDirectionCount++;
    TimerTask task = new TimerTask() {
        public void run() {
          Character.this.changeDirectionCount--;
          if (Character.this.changeDirectionCount == 0)
            Character.this.speed = 4; 
        }
      };
    timer.schedule(task, (lastTime * 1000));
  }
  
  public void setUnstoppable(int lastTime) {
    this.isUnstoppable = true;
    this.unstoppableCount++;
    unstoppableChangeImg(lastTime);
    Timer timer = new Timer(true);
    TimerTask task = new TimerTask() {
        public void run() {
          Character.this.unstoppableCount--;
          if (Character.this.unstoppableCount == 0)
            Character.this.isUnstoppable = false; 
        }
      };
    timer.schedule(task, (lastTime * 1000));
  }
  
  public void unstoppableChangeImg(int lastTime) {
    Timer timer = new Timer();
    final int times = lastTime * 1000 / 100;
    TimerTask task1 = new TimerTask() {
        int count = 0;
        
        public void run() {
          Character.this.isShowing = false;
          this.count++;
          if (this.count == times / 5) {
            Character.this.isShowing = true;
            cancel();
          } 
        }
      };
    TimerTask task2 = new TimerTask() {
        int count = 0;
        
        public void run() {
          Character.this.isShowing = true;
          this.count++;
          if (this.count == times)
            cancel(); 
        }
      };
    timer.scheduleAtFixedRate(task1, 0L, 500L);
    timer.scheduleAtFixedRate(task2, 0L, 100L);
  }
  
  public void bubbleAddPower() {
    this.bubblePower++;
  }
  
  public void showElement(Graphics g) {}
  
  public void move() {}
  
  public void destroy() {}
  
  public boolean isDead() {
    return this.dead;
  }
  
  public void setDead(boolean dead) {
    this.dead = dead;
  }
  
  public String getMoveType() {
    return this.moveType;
  }
  
  public void setMoveType(String moveType) {
    this.moveType = moveType;
  }
  
  public int getSpeed() {
    return this.speed;
  }
  
  public void setSpeed(int speed) {
    this.speed = speed;
  }
  
  public int getSpeedItemCount() {
    return this.speedItemCount;
  }
  
  public void setSpeedItemCount(int speedItemCount) {
    this.speedItemCount = speedItemCount;
  }
  
  public int getBubblePower() {
    return this.bubblePower;
  }
  
  public void setBubblePower(int bubblePower) {
    this.bubblePower = bubblePower;
  }
  
  public int getBubbleNum() {
    return this.bubbleNum;
  }
  
  public void setBubbleNum(int bubbleNum) {
    this.bubbleNum = bubbleNum;
  }
  
  public int getBubbleLargest() {
    return this.bubbleLargest;
  }
  
  public void setBubbleLargest(int bubbleLargest) {
    this.bubbleLargest = bubbleLargest;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public int getChangeDirectionCount() {
    return this.changeDirectionCount;
  }
  
  public void setChangeDirectionCount(int changeDirectionCount) {
    this.changeDirectionCount = changeDirectionCount;
  }
  
  public int getStopitemCount() {
    return this.stopitemCount;
  }
  
  public void setStopitemCount(int stopitemCount) {
    this.stopitemCount = stopitemCount;
  }
  
  public int getHeathPoint() {
    return this.heathPoint;
  }
  
  public boolean isisUnstoppable() {
    return this.isUnstoppable;
  }
  
  public void setisUnstoppable(boolean unstoppable) {
    this.isUnstoppable = unstoppable;
  }
}
