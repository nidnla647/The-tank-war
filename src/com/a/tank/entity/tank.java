package com.a.tank.entity;

import com.a.tank.enums.Direction;
import com.sun.source.tree.DirectiveTree;

import java.awt.*;

import static com.a.tank.enums.Direction.*;

public class tank {
    public boolean bL, bU, bR, bD;

    public int x;
    public int y;
    public float speed;
    public Direction dir;
    public boolean robot; // 区分阵营
    public boolean live = true;
    public tank(int x, int y, float speed, Direction dir, boolean robot, boolean live) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.dir = dir;
        this.robot = robot;
        this.live = live;
    }

    //绘制方法 窗口触发重绘-》产生画笔-》窗口调用-》坦克拿着这支笔在窗口上画出自己
//    public void draw(@org.jetbrains.annotations.NotNull Graphics g){
//        g.setColor(Color.RED); //设置颜色
//        g.fillRect(this.x, this.y, 10, 30); //坐标 长宽
//        g.fillRect(this.x+10, this.y + 5, 10, 20);
//        g.fillRect(this.x+20, y, 10, 30);
//    }
    //第二版优化：让坦克头朝向键盘点击的位置
    public void draw(Graphics g){
        if(!live) return;
//        System.out.println("初始化敌人数：" + enemies.size());
//        System.out.println("机器人坐标: " + x + "," + y);
        g.setColor(robot ? Color.RED : Color.GREEN);
        //画出固定主体
        g.fillRect(this.x, this.y, 10, 30); //坐标 长宽
        g.fillRect(this.x+10, this.y + 5, 10, 20);
        g.fillRect(this.x+20, y, 10, 30);
        if(robot){
            move();
        }
        //根据方向画炮口
        g.setColor(Color.WHITE);
        switch (dir) {
            case UP:    g.fillRect(x + 13, y - 5, 4, 10); break;
            case DOWN:  g.fillRect(x + 13, y + 25, 4, 10); break;
            case LEFT:  g.fillRect(x - 5, y + 13, 10, 4); break;
            case RIGHT: g.fillRect(x + 25, y + 13, 10, 4); break;
        }

    }
//    private void move() {
//        switch (dir) {
//            case UP:    y -= speed; break;
//            case DOWN:  y += speed; break;
//            case LEFT:  x -= speed; break;
//            case RIGHT: x += speed; break;
//        }
//        if (y < 0 || y > 600) {
//            y = (y < 0) ? 0 : 600;
//        }
//    }
    //坦克制造子弹
    public Bullet fire(){
//        System.out.print("开火");
        return new Bullet(x+13, y + 13, this.dir, this.robot);
    }
    public Rectangle getRect(){
        return new Rectangle(x, y, 30, 30);
    }
    //敌人随机行走函数实现
    private java.util.Random r = new java.util.Random();
//    private int moveCount = 0; // 控制移动速度（频率）
    private int step = 0;      // 控制转向频率（步数）

    public void move() {
        if(bL) x -=  speed;
        if(bR) x += speed;
        if(bU) y -= speed;
        if(bD) y += speed;

        if (robot) {
            // --- 1. 速度控制：决定坦克跑多快 ---
//            moveCount++;
//            if (moveCount < 12) { // 数值越大，动作越迟缓
//                return;
//            }
//            moveCount = 0;

            // --- 2. 位移执行 ---
            switch (dir) {
                case UP:    y -= speed; break;
                case DOWN:  y += speed; break;
                case LEFT:  x -= speed; break;
                case RIGHT: x += speed; break;
            }

            // --- 3. 转向控制：决定坦克跑多远才转弯 ---
            step++;
            // 关键：把这个值从 20 或 40 大幅调高到 100 甚至更多
            // 这样它必须真正“位移” 100 次才会考虑转弯，看起来就是在跑长线
            if (step > 100) {
                Direction[] dirs = Direction.values();
                this.dir = dirs[r.nextInt(dirs.length)];
                step = 0;
            }

            // --- 4. 边界强制转向 ---
            if (x < 0 || x > 770 || y < 30 || y > 570) {
                x = Math.max(0, Math.min(x, 770));
                y = Math.max(30, Math.min(y, 570));
                step = 101; // 撞墙了，必须立刻换方向
            }
        }
    }
}
