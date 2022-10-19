/*     */ package com.em.programming.game.thread;
/*     */ 
/*     */ import com.em.programming.game.datastore.NpcUserInfo;
/*     */ import com.em.programming.game.frame.OverJPanel;
/*     */ import com.em.programming.game.main.GameController;
/*     */ import com.em.programming.game.main.GameStart;
/*     */ import com.em.programming.game.model.manager.ElementManager;
/*     */ import com.em.programming.game.model.manager.GameMap;
/*     */ import com.em.programming.game.model.vo.BubbleExplode;
/*     */ import com.em.programming.game.model.vo.MagicBox;
/*     */ import com.em.programming.game.model.vo.MapFragility;
/*     */ import com.em.programming.game.model.vo.Npc;
/*     */ import com.em.programming.game.model.vo.SuperElement;
/*     */ import com.em.programming.game.test.NpcUserManager;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class GameThread extends Thread {
/*     */   private boolean running;
/*     */   
/*     */   private boolean over = false;
/*     */   
/*  28 */   private static int allTime = 0;
/*     */   
/*     */   public void run() {
/*  33 */     allTime = 300000;
/*  35 */     System.out.println("GameThread 启动， 线程号： " + Thread.currentThread().getId());
/*  37 */     while (!this.over) {
/*  38 */       this.running = true;
/*  40 */       loadElement();
/*  43 */       runNpcActions();
/*  46 */       runGame();
/*  48 */       overGame(Boolean.valueOf(this.over));
/*     */     } 
/*  50 */     GameStart.changeJPanel("over");
/*  51 */     System.out.println("GameThread 退出");
/*     */   }
/*     */   
/*     */   private void runNpcActions() {
/*  55 */     Executors.newSingleThreadExecutor().submit(() -> {
/*     */           while (!this.over) {
/*     */             NpcUserManager.getInstance().handleStep();
/*     */             try {
/*     */               sleep(1500L);
/*  63 */             } catch (InterruptedException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void loadElement() {
/*  73 */     ElementManager.getManager().loadMap();
/*     */   }
/*     */   
/*     */   private void overGame(Boolean over) {
/*  82 */     ElementManager.getManager().overGame(over);
/*     */   }
/*     */   
/*     */   private void runGame() {
/*  88 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/*  89 */     System.out.println(gameMap);
/*  91 */     boolean keepInGame = true;
/*  92 */     int keepInGameRunningTimes = 0;
/*  93 */     while (this.running || keepInGame) {
/*     */       try {
/*  96 */         Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
/*  97 */         Set<String> set = map.keySet();
/*  98 */         for (String key : set) {
/*  99 */           List<SuperElement> list = map.get(key);
/* 100 */           for (int i = list.size() - 1; i >= 0; i--) {
/* 101 */             ((SuperElement)list.get(i)).update();
/* 102 */             if (!((SuperElement)list.get(i)).isAlive())
/* 103 */               list.remove(i); 
/*     */           } 
/*     */         } 
/* 107 */         if (this.running) {
/* 114 */           fragilityBoom();
/* 116 */           npcBoom();
/* 118 */           npcMagicBox();
/* 123 */           defeat2();
/* 126 */           allTime -= 60;
/*     */         } else {
/* 129 */           keepInGameRunningTimes++;
/* 130 */           if (keepInGameRunningTimes > 80)
/* 131 */             keepInGame = false; 
/* 133 */           int showTimeLeft = (80 - keepInGameRunningTimes) / 16;
/* 134 */           GameController.setShowMsg("比赛结束（" + showTimeLeft + "）");
/*     */         } 
/*     */         try {
/* 139 */           sleep(60L);
/* 140 */         } catch (InterruptedException e) {
/* 142 */           e.printStackTrace();
/*     */         } 
/* 144 */       } catch (Exception e) {
/* 145 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 149 */     GameMap gameMap2 = ElementManager.getManager().getGameMap();
/* 150 */     System.out.println(gameMap2);
/*     */   }
/*     */   
/*     */   private void defeat2() {
/* 155 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 158 */     List<Npc> aliveNpcs = (List<Npc>)npcList.stream().filter(npc -> !npc.isDead()).collect(Collectors.toList());
/* 160 */     if (aliveNpcs.size() <= 1 || allTime <= 0) {
/* 176 */       String result = npcList.stream().sorted((o1, o2) -> o2.getRankCaculateScore() - o1.getRankCaculateScore()).peek(npc -> {
/*     */             StringBuilder builder = new StringBuilder();
/*     */             builder.append(npc.getNpcUserInfo()).append("比赛结果详情:").append("是否死亡：").append(npc.isDead()).append(", 剩余血值：").append(npc.getScore()).append("，投掷炸弹数：").append(npc.getTotalThrowBoomNum()).append("，综合评分：").append(npc.getRankCaculateScore());
/*     */             System.out.println(builder.toString());
/*     */           }).map(npc -> {
/*     */             NpcUserInfo npcUserInfo = npc.getNpcUserInfo();
/*     */             return npcUserInfo.getNpcName() + "(" + npcUserInfo.getNpcId() + ")";
/* 176 */           }).collect(Collectors.joining(System.lineSeparator()));
/* 178 */       StringBuilder builder = new StringBuilder();
/* 179 */       builder.append("本场排名")
/* 180 */         .append(System.lineSeparator())
/* 181 */         .append("----------------------")
/* 182 */         .append(System.lineSeparator())
/* 183 */         .append(result);
/* 185 */       OverJPanel.updateShowInfo(builder.toString());
/* 186 */       System.out.println(builder.toString());
/* 187 */       this.running = false;
/* 188 */       this.over = true;
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void npcBoom() {
/* 237 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 238 */     List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
/* 239 */     for (int i = 0; i < npcList.size(); i++) {
/* 240 */       for (int j = 0; j < explodeList.size(); j++) {
/* 241 */         if (((SuperElement)explodeList.get(j)).crash((SuperElement)npcList.get(i))) {
/* 242 */           Npc npc = npcList.get(i);
/* 243 */           if (!npc.isisUnstoppable()) {
/* 247 */             BubbleExplode e = (BubbleExplode)explodeList.get(j);
/* 250 */             Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
/* 252 */             if (!boomBelongingNpc.getNpcUserInfo().equals(npc.getNpcUserInfo())) {
/* 253 */               boomBelongingNpc.scoreChange(50);
/* 254 */               System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸到了" + npc.getNpcUserInfo() + ",加" + '2' + "分");
/* 255 */               npc.scoreChange(-50);
/* 256 */               System.out.println(npc.getNpcUserInfo() + "被" + boomBelongingNpc.getNpcUserInfo() + "炸到,扣" + '2' + "分");
/*     */             } else {
/* 258 */               boomBelongingNpc.scoreChange(-100);
/* 259 */               System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸到了自己,扣" + 'd' + "分!");
/*     */             } 
/* 271 */             npc.setUnstoppable(3);
/* 272 */             System.out.println(npc.getNpcUserInfo() + "被炸到，进入保护期");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fragilityBoom() {
/* 280 */     List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
/* 281 */     List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
/* 282 */     for (int i = 0; i < fragility.size(); i++) {
/* 283 */       for (int j = 0; j < explodes.size(); j++) {
/* 284 */         if (((SuperElement)explodes.get(j)).crash(fragility.get(i))) {
/* 285 */           MapFragility mapFragility = (MapFragility)fragility.get(i);
/* 286 */           mapFragility.setDestoried(true);
/* 287 */           BubbleExplode e = (BubbleExplode)explodes.get(j);
/* 288 */           Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
/* 289 */           boomBelongingNpc.scoreChange(10);
/* 290 */           System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸毁障碍物，加 " + '\n' + "分");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void npcMagicBox() {
/* 299 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 300 */     List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
/* 301 */     for (int i = 0; i < npcList.size(); i++) {
/* 302 */       for (int j = magicBoxList.size() - 1; j >= 0; j--) {
/* 303 */         if (((SuperElement)magicBoxList.get(j)).crash((SuperElement)npcList.get(i))) {
/* 304 */           MagicBox magicBox = (MagicBox)magicBoxList.get(j);
/* 305 */           magicBox.setEaten(true);
/* 307 */           Npc npc = npcList.get(i);
/* 308 */           npc.scoreChange(30);
/* 309 */           System.out.println(npc.getNpcUserInfo() + "获取道具，加 " + '\036' + "分");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void linkGame() {}
/*     */   
/*     */   public static int getAllTime() {
/* 321 */     return allTime;
/*     */   }
/*     */ }


/* Location:              E:\otherproject\PaopaoGAi\server2.1\SuperBooman.jar!\com\em\programming\game\thread\GameThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */