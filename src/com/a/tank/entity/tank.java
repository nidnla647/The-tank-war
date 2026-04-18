package com.a.tank.entity;

import com.a.tank.core.GameFrame; // 记得引入
import com.a.tank.entity.wall;
import com.a.tank.core.GameFrame;
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
//            move();
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
        return new Rectangle(x+2, y+2, 26, 26);
    }
    //敌人随机行走函数实现
    private java.util.Random r = new java.util.Random();
    private int moveCount = 0; // 控制移动速度（频率）
    private int step = 0;      // 控制转向频率（步数）

    public void move() {
        if(!robot){

            int preX = x;
            int preY = y;

            if(bL) x -=  speed;
            if(bR) x += speed;
            if(bU) y -= speed;
            if(bD) y += speed;
            //更新：玩家碰墙逻辑
            if(isCollideWithWall(x, y)){
                x = preX;
                y = preY;
            }
        }else{
            //AI碰墙返回逻辑
            moveCount++;
            if (moveCount < 2) return;
            moveCount = 0;
            int nextX = x, nextY = y;  //预判断算法，先看看能不能走，再迈步子
            switch (dir) {
                case UP:    nextY -= speed; break;
                case DOWN:  nextY += speed; break;
                case LEFT:  nextX -= speed; break;
                case RIGHT: nextX += speed; break;
            }
            if(isCollideWithWall(nextX, nextY) || nextY < 0 || nextX > 770 || nextY < 30 || nextY > 570){
                this.dir = Direction.values()[r.nextInt(4)]; //撞墙了就随机选择一个方向继续前进
                step = 0;
            }else{
                this.x = nextX;
                this.y = nextY;
            }
            step++;
            if (step > 100) {
                this.dir = Direction.values()[r.nextInt(4)];
                step = 0;
            }
        }
        if(x < 0) x = 0;
        if(y < 30) y = 30;
        if(x> 770) x = 770;
        if(y > 570) y = 570;
    }
    private boolean isCollideWithWall(int nextX, int nextY){
        Rectangle nextRect = new Rectangle(nextX+2, nextY+2, 26, 26);
        for(int i = 0; i < GameFrame.walls.size(); i++){
            wall w = GameFrame.walls.get(i);
            if(nextRect.intersects(w.getRect())){
//                System.out.println("撞击发生坐标: " + nextX + "," + nextY);
                return true;
            }
        }
        return false;
    }
    
}
