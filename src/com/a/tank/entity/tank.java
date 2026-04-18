package com.a.tank.entity;

import com.a.tank.enums.Direction;
import com.sun.source.tree.DirectiveTree;

import java.awt.*;

import static com.a.tank.enums.Direction.*;

public class tank {
    public int x;
    public int y;
    public float speed;
    public Direction dir;
    public boolean robot; // 区分阵营
    public boolean live;
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
    private void move() {
        switch (dir) {
            case UP:    y -= speed; break;
            case DOWN:  y += speed; break;
            case LEFT:  x -= speed; break;
            case RIGHT: x += speed; break;
        }
        if (y < 0 || y > 600) {
            y = (y < 0) ? 0 : 600;
        }
    }
    //坦克制造子弹
    public Bullet fire(){
//        System.out.print("开火");
        return new Bullet(x+13, y + 13, this.dir);
    }
    public Rectangle getRect(){
        return new Rectangle(x, y, 30, 30);
    }
}
