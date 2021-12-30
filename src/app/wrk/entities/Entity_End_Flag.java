/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;

/**
 *
 * @author dingl01
 */
public class Entity_End_Flag extends Entity {

    private int bottom;

    public Entity_End_Flag(Wrk wrk, Entity_Mario mario, int x, int y, int mapType) {
        super(wrk, mario, END_FLAG_ID, mapType);
        this.x = x;
        this.y = y;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("entity_end_flag"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("entity_end_flag_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("entity_end_flag_sky"));
                break;
        }
    }

    @Override
    public void killed() {

    }

    @Override
    public void hitMario() {
    }

    @Override
    public void update() {
        updateAnimation();
        if (velY != 0) {
            y += velY;
            if (y >= bottom) {
                y = bottom;
                velY = 0;
            }
        }
    }


    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

}
