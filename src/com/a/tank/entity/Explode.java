package com.a.tank.entity;

import java.awt.*;

public class Explode {
    public int x, y;
    public boolean live = true;

    // 爆炸的半径序列，模拟由小变大再变小
    private int[] diameter = {4, 10, 20, 35, 50, 65, 40, 25, 10, 2};
    private int step = 0; // 当前播放到哪一帧

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (!live) return;

        if (step >= diameter.length) {
            live = false;
            return;
        }

        g.setColor(Color.ORANGE);
        // 居中绘制圆圈
//        System.out.println("正在绘制爆炸帧：" + step);
        g.fillOval(x - diameter[step]/2, y - diameter[step]/2, diameter[step], diameter[step]);

        step++; // 每重绘一次，演进到下一帧
    }
}