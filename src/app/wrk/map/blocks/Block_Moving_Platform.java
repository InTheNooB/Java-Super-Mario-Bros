/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map.blocks;

import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;

/**
 *
 * @author lione
 */
public class Block_Moving_Platform extends Block {

    private int startY;
    private int stopY;

    public Block_Moving_Platform(Wrk refWrk, int x, int y, boolean breakable, char id, int width, int height) {
        super(refWrk, x, y, breakable, id, width, height, 0);
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        animation.setCurrentCategorie(animation.importAnimation("block_moving_platform"));
    }

    public void update() {
        y -= MOVING_PLATFORM_SPEED;
        if (y <= stopY) {
            y = startY;
        }
//        System.out.println(y);
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getStopY() {
        return stopY;
    }

    public void setStopY(int stopY) {
        this.stopY = stopY;
    }

}
