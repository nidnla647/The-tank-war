package com.a.tank.core;

import com.a.tank.entity.Bullet;
import com.a.tank.entity.Explode;
import com.a.tank.entity.tank;
import com.a.tank.entity.wall;

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

        private boolean gameOver = false;
        private boolean gameWin = false;

        tank mytank = new tank(400, 550, 3, UP, false, true);
        java.util.List<Bullet> bullets = new java.util.ArrayList<>(); //子弹数组
        java.util.List<tank> enemies = new java.util.ArrayList<>(); //敌方数组
        public static java.util.List<wall> walls = new java.util.ArrayList<>(); //随机生成墙面数组
        java.util.List<Explode> explodes = new java.util.ArrayList<>(); //存放爆炸特效
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
                    int key = e.getKeyCode();
                    if(gameOver && key == KeyEvent.VK_ENTER) {
                        restart();
                    }
                    setMainTankDir(e, true); // 按下时设为 true
                }
                private void restart() {
                    mytank = new tank(400, 550, 3, UP, false, true);
                    enemies.clear();
                    walls.clear();
                    explodes.clear();

                    for(int i = 0; i <5; i++){
                        enemies.add(new tank(110 + i *80, 200, 5,DOWN, true, true));
                    }
                    creatwall();

                    gameOver = false;
                    gameWin = false;
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
    public void paint(Graphics g) {
        // 1. 确保离屏画布存在
        if (offScreenImage == null) {
            offScreenImage = this.createImage(800, 600);
        }

        // 2. 获取离屏画布的画笔
        Graphics goff = offScreenImage.getGraphics();

        // 3. 擦除背景（必须是第一步，否则会有残影）
        goff.setColor(Color.BLACK);
        goff.fillRect(0, 0, 800, 600);

        // 4. 绘制所有游戏物体（全部画在 goff 上）

        // 画墙
        for (int k = 0; k < walls.size(); k++) {
            walls.get(k).draw(goff);
        }

        // 画玩家坦克
        if (mytank != null && mytank.live) {
            mytank.draw(goff);
        }

        // 处理子弹逻辑（碰撞检测 + 绘制）
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if (!b.live) {
                bullets.remove(i);
                i--;
                continue;
            }

            // 子弹碰撞检测
            if (b.robot) { // 敌人子弹打玩家
                if (mytank.live && b.getRect().intersects(mytank.getRect())) {
                    b.live = false;
                    mytank.live = false;
                    explodes.add(new Explode(mytank.x + 15, mytank.y + 15));
                }
            } else { // 玩家子弹打敌人
                for (int j = 0; j < enemies.size(); j++) {
                    tank e = enemies.get(j);
                    if (e.live && b.getRect().intersects(e.getRect())) {
                        b.live = false;
                        e.live = false;
                        explodes.add(new Explode(e.x + 15, e.y + 15));
                        break;
                    }
                }
            }

            // 子弹打墙
            for (wall w : walls) {
                if (b.getRect().intersects(w.getRect())) {
                    b.live = false;
                    break;
                }
            }

            b.draw(goff); // 绘制子弹
        }

        // 绘制敌人
        for (int i = 0; i < enemies.size(); i++) {
            tank it = enemies.get(i);
            if (it.live) {
                it.draw(goff);
                // 敌人随机开火
                if (new java.util.Random().nextInt(100) > 98) {
                    Bullet b = it.fire();
                    if (b != null) bullets.add(b);
                }
            } else {
                enemies.remove(i);
                i--;
            }
        }

        // 绘制爆炸效果
        for (int p = 0; p < explodes.size(); p++) {
            Explode exp = explodes.get(p);
            if (exp.live) {
                exp.draw(goff);
            } else {
                explodes.remove(p);
                p--;
            }
        }

        // 5. 判断游戏胜负状态
        if (!mytank.live) {
            gameOver = true;
        }
        if (enemies.isEmpty() && mytank.live) {
            gameWin = true;
            gameOver = true;
        }

        // 6. 绘制结算界面（必须在 g.drawImage 之前画在 goff 上）
        if (gameOver) {
            // 半透明遮罩
            goff.setColor(new Color(0, 0, 0, 150));
            goff.fillRect(0, 0, 800, 600);

            goff.setFont(new Font("微软雅黑", Font.BOLD, 60));
            if (gameWin) {
                goff.setColor(Color.YELLOW);
                goff.drawString("YOU WIN!", 250, 300);
            } else {
                goff.setColor(Color.RED);
                goff.drawString("GAME OVER", 220, 300);
            }

            goff.setFont(new Font("微软雅黑", Font.PLAIN, 20));
            goff.setColor(Color.WHITE);
            goff.drawString("Press ENTER to Restart", 280, 400);
        }

        // 7. 【关键】最后一步：将整张画好的离屏画布一次性画到真实的屏幕 g 上
        g.drawImage(offScreenImage, 0, 0, null);

        // 8. 释放画笔
        goff.dispose();
    }
//        if(enemies.size() == 0)
}

