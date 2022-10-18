package com.em.programming.game.thread;

import com.em.programming.game.frame.OverJPanel;
import com.em.programming.game.main.GameController;
import com.em.programming.game.main.GameStart;
import com.em.programming.game.model.manager.ElementManager;
import com.em.programming.game.model.manager.GameMap;
import com.em.programming.game.model.vo.BubbleExplode;
import com.em.programming.game.model.vo.MagicBox;
import com.em.programming.game.model.vo.MapFragility;
import com.em.programming.game.model.vo.Npc;
import com.em.programming.game.model.vo.SuperElement;
import com.em.programming.game.test.NpcUserManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GameThread extends Thread {
  private boolean running;
  
  private boolean over = false;
  
  private static int sleepTime = 20;
  
  private static int allTime = 300000;
  
  public void run() {
    while (!this.over) {
      this.running = true;
      loadElement();
      runNpcActions();
      runGame();
      overGame(Boolean.valueOf(this.over));
    } 
    GameStart.changeJPanel("over");
  }
  
  private void runNpcActions() {
    Executors.newSingleThreadExecutor().submit(() -> {
          while (!this.over) {
            NpcUserManager.getInstance().handleStep();
            try {
              sleep(1000L);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } 
          } 
        });
  }
  
  private void loadElement() {
    ElementManager.getManager().loadMap();
  }
  
  private void overGame(Boolean over) {
    ElementManager.getManager().overGame(over);
  }
  
  private void runGame() {
    GameMap gameMap = ElementManager.getManager().getGameMap();
    System.out.println(gameMap);
    boolean keepInGame = true;
    int keepInGameRunningTimes = 0;
    while (this.running || keepInGame) {
      try {
        Map<String, List<SuperElement>> map = ElementManager.getManager().getMap();
        Set<String> set = map.keySet();
        for (String key : set) {
          List<SuperElement> list = map.get(key);
          for (int i = list.size() - 1; i >= 0; i--) {
            ((SuperElement)list.get(i)).update();
            if (!((SuperElement)list.get(i)).isAlive())
              list.remove(i); 
          } 
        } 
        if (this.running) {
          fragilityBoom();
          npcBoom();
          npcMagicBox();
          defeat2();
        } else {
          keepInGameRunningTimes++;
          if (keepInGameRunningTimes > 500)
            keepInGame = false; 
          int showTimeLeft = (500 - keepInGameRunningTimes) / 50;
          GameController.setShowMsg("比赛结束（"+ showTimeLeft + "）");
        } 
        allTime -= sleepTime;
        try {
          sleep(20L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } 
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
    GameMap gameMap2 = ElementManager.getManager().getGameMap();
    System.out.println(gameMap2);
  }
  
  private void defeat2() {
    boolean allDead = true;
    int surviveP = 0;
    int winner = 2;
    List<Npc> npcList = ElementManager.getManager().getNpcs();
    List<Npc> aliveNpcs = (List<Npc>)npcList.stream().filter(npc -> !npc.isDead()).collect(Collectors.toList());
    if (aliveNpcs.size() <= 1 || allTime <= 0) {
      OverJPanel.getResult().setText(((Npc)aliveNpcs.get(0)).getNpcUserInfo() + "获胜");
      this.running = false;
      this.over = true;
      return;
    } 
  }
  
  private void npcBoom() {
    List<Npc> npcList = ElementManager.getManager().getNpcs();
    List<SuperElement> explodeList = ElementManager.getManager().getElementList("explode");
    for (int i = 0; i < npcList.size(); i++) {
      for (int j = 0; j < explodeList.size(); j++) {
        if (((SuperElement)explodeList.get(j)).crash((SuperElement)npcList.get(i))) {
          Npc npc = npcList.get(i);
          if (!npc.isisUnstoppable()) {
            BubbleExplode e = (BubbleExplode)explodeList.get(j);
            Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
            if (!boomBelongingNpc.getNpcUserInfo().equals(npc.getNpcUserInfo())) {
              boomBelongingNpc.scoreChange(50);
              System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸到了"+ npc.getNpcUserInfo() + ",加" + "50" + "分");
              npc.scoreChange(-50);
              System.out.println(npc.getNpcUserInfo() + "被" + boomBelongingNpc.getNpcUserInfo() + "炸到,扣"+ "50" + "分");
            } else {
              boomBelongingNpc.scoreChange(-100);
              System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸到了自己,扣"+ "100" + "分");
            } 
            npc.setUnstoppable(3);
            System.out.println(npc.getNpcUserInfo() + "被炸到，进入保护期");
          } 
        } 
      } 
    } 
  }
  
  private void fragilityBoom() {
    List<SuperElement> explodes = ElementManager.getManager().getElementList("explode");
    List<SuperElement> fragility = ElementManager.getManager().getElementList("fragility");
    for (int i = 0; i < fragility.size(); i++) {
      for (int j = 0; j < explodes.size(); j++) {
        if (((SuperElement)explodes.get(j)).crash(fragility.get(i))) {
          MapFragility mapFragility = (MapFragility)fragility.get(i);
          mapFragility.setDestoried(true);
          BubbleExplode e = (BubbleExplode)explodes.get(j);
          Npc boomBelongingNpc = ElementManager.getManager().getNpc(e.getNpcId());
          boomBelongingNpc.scoreChange(10);
          System.out.println(boomBelongingNpc.getNpcUserInfo() + "炸毁障碍物，加" + "10" + "分");
        } 
      } 
    } 
  }
  
  private void npcMagicBox() {
    List<Npc> npcList = ElementManager.getManager().getNpcs();
    List<SuperElement> magicBoxList = ElementManager.getManager().getElementList("magicBox");
    for (int i = 0; i < npcList.size(); i++) {
      for (int j = magicBoxList.size() - 1; j >= 0; j--) {
        if (((SuperElement)magicBoxList.get(j)).crash((SuperElement)npcList.get(i))) {
          MagicBox magicBox = (MagicBox)magicBoxList.get(j);
          magicBox.setEaten(true);
          Npc npc = npcList.get(i);
          npc.scoreChange(30);
          System.out.println(npc.getNpcUserInfo() + "获取道具，加" + "30" + "分");
        } 
      } 
    } 
  }
  
  public void linkGame() {}
  
  public static int getAllTime() {
    return allTime;
  }
}
