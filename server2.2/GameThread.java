/*     */ package com.em.programming.game.thread;
/*     */ 
/*     */ import com.em.programming.game.datastore.ConfigData;
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
/*     */ import com.em.programming.game.stat.GameShowMsgQueue;
/*     */ import com.em.programming.game.stat.NpcScoreHelper;
/*     */ import com.em.programming.game.stat.ShowDataManager;
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
/*  33 */   private static int allTime = 0;
/*     */   
/*     */   public void run() {
/*  38 */     allTime = 300000;
/*  40 */     System.out.println("GameThread 启动， 线程号： " + Thread.currentThread().getId());
/*  42 */     GameShowMsgQueue.put("本场比赛开始!");
/*  43 */     ShowDataManager.getInstance().setCurrentGameName(ConfigData.getInstance().getGameInfo());
/*  45 */     while (!this.over) {
/*  46 */       this.running = true;
/*  48 */       loadElement();
/*  51 */       runNpcActions();
/*  54 */       runGame();
/*  56 */       overGame(Boolean.valueOf(this.over));
/*     */     } 
/*  58 */     GameStart.changeJPanel("over");
/*  59 */     System.out.println("GameThread 退出");
/*     */   }
/*     */   
/*     */   private void runNpcActions() {
/*  63 */     Executors.newSingleThreadExecutor().submit(() -> {
/*     */           while (!this.over) {
/*     */             NpcUserManager.getInstance().handleStep();
/*     */             try {
/*     */               sleep(1500L);
/*  71 */             } catch (InterruptedException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void loadElement() {
/*  81 */     ElementManager.getManager().loadMap();
/*     */   }
/*     */   
/*     */   private void overGame(Boolean over) {
/*  90 */     ElementManager.getManager().overGame(over);
/*     */   }
/*     */   
/*     */   private void runGame() {
/*  96 */     GameMap gameMap = ElementManager.getManager().getGameMap();
/*  97 */     System.out.println(gameMap);
/*  99 */     boolean keepInGame = true;
/* 100 */     int keepInGameRunningTimes = 0;
/* 101 */     while (this.running || keepInGame) {
/*     */       try {
/* 104 */         Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
/* 105 */         Set<String> set = map.keySet();
/* 106 */         for (String key : set) {
/* 107 */           List<SuperElement> list = map.get(key);
/* 108 */           for (int i = list.size() - 1; i >= 0; i--) {
/* 109 */             ((SuperElement)list.get(i)).update();
/* 110 */             if (!((SuperElement)list.get(i)).isAlive())
/* 111 */               list.remove(i); 
/*     */           } 
/*     */         } 
/* 115 */         if (this.running) {
/* 122 */           fragilityBoom();
/* 124 */           npcBoom();
/* 126 */           npcMagicBox();
/* 131 */           defeat2();
/* 134 */           allTime -= 60;
/*     */         } else {
/* 137 */           keepInGameRunningTimes++;
/* 138 */           if (keepInGameRunningTimes > 80)
/* 139 */             keepInGame = false; 
/* 141 */           int showTimeLeft = (80 - keepInGameRunningTimes) / 16;
/* 142 */           GameController.setShowMsg("比赛结束（" + showTimeLeft + "）");
/*     */         } 
/*     */         try {
/* 147 */           sleep(60L);
/* 148 */         } catch (InterruptedException e) {
/* 150 */           e.printStackTrace();
/*     */         } 
/* 152 */       } catch (Exception e) {
/* 153 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 157 */     GameMap gameMap2 = ElementManager.getManager().getGameMap();
/* 158 */     System.out.println(gameMap2);
/*     */   }
/*     */   
/*     */   private void defeat2() {
/* 163 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 166 */     List<Npc> aliveNpcs = (List<Npc>)npcList.stream().filter(npc -> !npc.isDead()).collect(Collectors.toList());
/* 168 */     if (aliveNpcs.size() <= 1 || allTime <= 0) {
/* 184 */       String result = npcList.stream().sorted((o1, o2) -> o2.getRankCaculateScore() - o1.getRankCaculateScore()).peek(npc -> {
/*     */             StringBuilder builder = new StringBuilder();
/*     */             builder.append(npc.getNpcUserInfo()).append("比赛结果详情:").append("是否死亡：").append(npc.isDead()).append(", 剩余血值：").append(npc.getScore()).append("，投掷炸弹数：").append(npc.getTotalThrowBoomNum()).append("，综合评分：").append(npc.getRankCaculateScore());
/*     */             System.out.println(builder.toString());
/*     */           }).map(npc -> {
/*     */             NpcUserInfo npcUserInfo = npc.getNpcUserInfo();
/*     */             return npcUserInfo.getNpcName() + "(" + npcUserInfo.getNpcId() + ")";
/* 184 */           }).collect(Collectors.joining(System.lineSeparator()));
/* 186 */       StringBuilder builder = new StringBuilder();
/* 187 */       builder.append("本场排名")
/* 188 */         .append(System.lineSeparator())
/* 189 */         .append("----------------------")
/* 190 */         .append(System.lineSeparator())
/* 191 */         .append(result);
/* 193 */       OverJPanel.updateShowInfo(builder.toString());
/* 194 */       System.out.println(builder.toString());
/* 195 */       this.running = false;
/* 196 */       this.over = true;
/* 198 */       GameShowMsgQueue.put("本场比赛结束!");
/* 199 */       GameShowMsgQueue.put(builder.toString(), false);
/* 201 */       GameShowMsgQueue.putSpecialShow(null, "Game Over");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void npcBoom() {
/* 208 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 209 */     List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
/* 210 */     for (int i = 0; i < npcList.size(); i++) {
/* 211 */       for (int j = 0; j < explodeList.size(); j++) {
/* 212 */         if (((SuperElement)explodeList.get(j)).crash((SuperElement)npcList.get(i))) {
/* 213 */           Npc npc = npcList.get(i);
/* 214 */           if (!npc.isisUnstoppable()) {
/* 218 */             BubbleExplode e = (BubbleExplode)explodeList.get(j);
/* 219 */             Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
/* 221 */             NpcScoreHelper.npcKills(boomBelongingNpc, npc);
/* 229 */             npc.setUnstoppable(3);
/* 231 */             String msg = npc.getNpcUserInfo() + "被炸到，进入保护期";
/* 232 */             System.out.println(msg);
/* 233 */             GameShowMsgQueue.put(msg, false);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fragilityBoom() {
/* 241 */     List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
/* 242 */     List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
/* 243 */     for (int i = 0; i < fragility.size(); i++) {
/* 244 */       for (int j = 0; j < explodes.size(); j++) {
/* 245 */         if (((SuperElement)explodes.get(j)).crash(fragility.get(i))) {
/* 246 */           MapFragility mapFragility = (MapFragility)fragility.get(i);
/* 247 */           mapFragility.setDestoried(true);
/* 248 */           BubbleExplode e = (BubbleExplode)explodes.get(j);
/* 249 */           Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
/* 250 */           NpcScoreHelper.bombObstacle(boomBelongingNpc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void npcMagicBox() {
/* 259 */     List<Npc> npcList = ElementManager.getManager().getNpcs();
/* 260 */     List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
/* 261 */     for (int i = 0; i < npcList.size(); i++) {
/* 262 */       for (int j = magicBoxList.size() - 1; j >= 0; j--) {
/* 263 */         if (((SuperElement)magicBoxList.get(j)).crash((SuperElement)npcList.get(i))) {
/* 264 */           MagicBox magicBox = (MagicBox)magicBoxList.get(j);
/* 265 */           magicBox.setEaten(true);
/* 266 */           NpcScoreHelper.pickMagicBox(npcList.get(i));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getAllTime() {
/* 273 */     return allTime;
/*     */   }
/*     */ }


/* Location:              E:\otherproject\PaopaoGAi\server2.2\SuperBooman.jar!\com\em\programming\game\thread\GameThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */