package com.a.tank.entity;

import com.a.tank.enums.Direction;

import java.awt.*;

import static java.nio.file.Files.move;

public class Bullet {
    public int x, y;
    public Direction dir;
    public int speed = 1;
    public boolean live = true; // 判断子弹是否出界

    public Bullet(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void draw(Graphics g){
        if(!live) return;
        g.setColor(Color.WHITE);
        g.fillOval(x,y,5, 5); //画出小圆点
        move();
    }
    //子弹移动
    private void move(){
        switch(dir){
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
        }
        if(x < 0 || y < 0 || x > 800 || y > 600) {
            live = false;
        }
    }
    //碰撞检测：防止子弹直接穿过坦克(碰撞箱)
//    public void hitTank(tank t){
//        Rectangle bulletRect = new Rectangle(this.x, this.y, 5,5);
//        Rectangle bulletRect2 = new Rectangle(t.x, t.y, 30,30);
//        //判断两碰撞箱是否重叠
//        if(bulletRect.intersects(bulletRect2)){
//            this.live = false;
//            t.live = false;
//        }
//
//    }

    public Rectangle getRect() {
        return new Rectangle(x, y, 5, 5);
    }
}
