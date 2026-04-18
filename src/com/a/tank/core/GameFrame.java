package com.a.tank.core;

import com.a.tank.entity.Bullet;
import com.a.tank.entity.tank;
import com.a.tank.entity.wall;
import com.a.tank.enums.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//import static com.a.tank.enums.Direction;
import static com.a.tank.enums.Direction.UP;
import static com.a.tank.enums.Direction.DOWN;
import static com.a.tank.enums.Direction.LEFT;
import static com.a.tank.enums.Direction.RIGHT;

public class GameFrame extends JFrame {


        tank mytank = new tank(400, 550, 3, UP, false, true);
        java.util.List<Bullet> bullets = new java.util.ArrayList<>(); //子弹数组
        java.util.List<tank> enemies = new java.util.ArrayList<>(); //敌方数组
        public static java.util.List<wall> walls = new java.util.ArrayList<>(); //随机生成墙面数组
        //第一版墙生成方案：随机生成
//        public void creatwall(){ //界面随机生成+显示墙
//            walls.clear();
//            java.util.Random r = new java.util.Random(); //生成随机数
//            for(int i = 0; i <800; i+= 30){
//                for(int j = 100; j < 500; j+= 30){
//                    if(r.nextInt(100) < 15){
//                        walls.add(new wall(i, j));
//                    }
//                }
//            }
//
//        }
        //第二版：线性墙面
        public void creatwall(){
            walls.clear();
            java.util.Random r = new java.util.Random();
            for(int n = 0; n < 15; n++){

                //随机起点（网格化坐标，必须是30的倍数）
                int startX = r.nextInt(25)*30;
                int startY = r.nextInt((12)+4) * 30;
                int length = r.nextInt(6)+3;
                boolean horizontal = r.nextBoolean(); //随机方向0横1竖
                for (int i = 0; i < length; i++) {
                    int currX = horizontal ? startX + (i * 30) : startX;
                    int currY = horizontal ? startY : startY + (i * 30);

                    // 越界检查
                    if (currX < 770 && currY < 550) {
                        walls.add(new wall(currX, currY));
                    }
                }
            }
            walls.removeIf(w -> new Rectangle(300, 400, 200, 200).intersects(w.getRect()));
            for (tank e : enemies) {
                // 给敌人也留出 60x60 的安全区（比坦克略大，防止卡住）
                Rectangle enemyArea = new Rectangle(e.x - 15, e.y - 15, 60, 60);
                walls.removeIf(w -> enemyArea.intersects(w.getRect()));
            }
        }
        public void launch() {
            for(int i = 0; i <5; i++){
                enemies.add(new tank(110 + i *80, 200, 5,DOWN, true, true)); //并行排列，方向随机
            }
            creatwall();
//            System.out.println("初始化敌人数：" + enemies.size());
            setTitle("坦克大战");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);//否则关闭窗口后后台进程还在跑
            setLocationRelativeTo(null); //居中显示
            //键盘监听
//            this.addKeyListener(new KeyAdapter() {
//                public void keyPressed(KeyEvent e) {
//                    int key = e.getKeyCode();
//                    switch(key){
//                        case KeyEvent.VK_W:
//                            mytank.y -= mytank.speed;
//                            mytank.dir = UP;
//                            break;
//                        case KeyEvent.VK_S:
//                            mytank.y += mytank.speed;
//                            mytank.dir = Direction.DOWN;
//                            break;
//                        case KeyEvent.VK_A:
//                            mytank.x -= mytank.speed;
//                            mytank.dir = Direction.LEFT;
//                            break;
//                        case KeyEvent.VK_D:
//                            mytank.x += mytank.speed;
//                            mytank.dir = Direction.RIGHT;
//                            break;
//                        case KeyEvent.VK_J:
//                                Bullet b = mytank.fire();
////                        bullets.add(mytank.fire());
//                                if(b != null){
//                                    bullets.add(b);
////                                    System.out.print("当前子弹数：" + bullets.size());
//                                }
//
//                    }
////                    if (key == KeyEvent.VK_W) mytank.y -= mytank.speed;
////                    if (key == KeyEvent.VK_S) mytank.y += mytank.speed;
////                    if (key == KeyEvent.VK_A) mytank.x -= mytank.speed;
////                    if (key == KeyEvent.VK_D) mytank.x += mytank.speed;
//                    //修改坐标后强制刷新屏幕
////                    repaint();
//                    //显示子弹
//                    //创建子弹数组
//                    //设置键盘监听，按J发射子弹
//
//                }
//
//            });
            // GameFrame.java
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    setMainTankDir(e, true); // 按下时设为 true
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    setMainTankDir(e, false); // 松开时设为 false
                }

                private void setMainTankDir(KeyEvent e, boolean state) {
                    int key = e.getKeyCode();
                    switch (key) {
                        case KeyEvent.VK_W: mytank.bU = state; mytank.dir = UP; break;
                        case KeyEvent.VK_S: mytank.bD = state; mytank.dir = DOWN; break;
                        case KeyEvent.VK_A: mytank.bL = state; mytank.dir = LEFT; break;
                        case KeyEvent.VK_D: mytank.bR = state; mytank.dir = RIGHT; break;
                        case KeyEvent.VK_J: if(state) bullets.add(mytank.fire()); break;
                    }
                }
            });
            setVisible(true);
            new Thread(() -> {
                while (true) {
                    if(mytank.live) mytank.move();

                    // 遍历所有机器人，统一移动
                    for(tank e : enemies) {
                        if(e.live) e.move();
                    }

                    repaint();
                    try { Thread.sleep(20); } catch (Exception e) {}
                }
            }).start();

        }
        //优化方案：创建隐形画布
        Image offScreenImage = null;
        public void paint(Graphics g){
//            super.paint(g);
            if(offScreenImage == null){
                //创建和窗口等大的图片
                offScreenImage = this.createImage(800,600);
            }
            Graphics goff = offScreenImage.getGraphics();
            goff.setColor(Color.BLACK);
            goff.fillRect(0,0,2000,2000);
            if(mytank != null){
                mytank.draw(goff);
            }
            for(int k = 0; k <walls.size(); k++){
                walls.get(k).draw(goff);
            }
            for(int i = 0; i < bullets.size(); i++){
                Bullet b = bullets.get(i);
                if(!b.live) continue;
                if(b.robot){
                    if(mytank.live && b.getRect().intersects(mytank.getRect())){
                        b.live = false;
                        mytank.live =false;
                        System.out.println("你被击中了！");
                    }
                }else{
                    for(int j = 0; j < enemies.size(); j++){
                        tank e = enemies.get(j);
                        if(e.live && b.getRect().intersects(e.getRect())){
                            b.live = false;
                            e.live = false;
                            System.out.println("击中敌人！");
                            break;
                        }
                    }
                }
                for(int j = 0; j < walls.size(); j++){
                    if(b.getRect().intersects(walls.get(j).getRect())){
                        b.live = false;
                        break;
                    }
                }
            }
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                if (b.live) {
                    b.draw(goff);
                } else {
                    bullets.remove(i);
                    i--;
                }
            }
            //更新：检验当前子弹是否为敌人子弹，如果是，则检验是都撞到玩家
            // 绘制并清理子弹的正确逻辑

            for(int i = 0; i < enemies.size(); i++){
                tank it = enemies.get(i);
                if(it.live){
                    it.draw(goff);
                    if(it.live && new java.util.Random().nextInt(100) > 98){
                        Bullet b = it.fire();
                        if(b != null){
                            bullets.add(b);
                        }
//                        it.draw(goff);
                    }
                }else{
                    enemies.remove(i);
                    i--;
                }

                //敌人随机开火
                //产生一个随机数，随机定数修改概率值，代表敌人开枪的概率

            }

            g.drawImage(offScreenImage, 0, 0, null);
            goff.dispose();
            //渲染子弹
        }
}

