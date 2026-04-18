package com.a.tank.entity;

import java.awt.*;

public class wall {
    public int x, y;
    public int width = 30, height = 30;
    public wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, width, height);
    }
    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
}
