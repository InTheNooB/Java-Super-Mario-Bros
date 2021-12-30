/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import static app.wrk.Constantes.SOUND_1UP;
import static app.wrk.Constantes.SOUND_POWERUP_CONSUMED;
import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;
import app.wrk.others.Sound;

/**
 *
 * @author lione
 */
public class Entity_Flower extends Entity {

    private boolean grown;
    private double startTime;

    public Entity_Flower(Wrk wrk, Entity_Mario mario, float x, float y, int mapType) {
        super(wrk, mario, POWERUP_ID, mapType);
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        grown = false;
        blockContent = true;
        startTime = 0;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("entity_flower"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("entity_flower_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("entity_flower_sky"));
                break;
        }
    }

    public void update() {
        if (grown) {
            move();
        } else {
            if (startTime != 0) {
                if ((System.nanoTime() - startTime) / 10000000 >= POWERUP_GROWING_TIME) {
                    grown = true;
                } else {
                    y += velY;
                }
            } else {
                startTime = System.nanoTime();
                velY = -32 / POWERUP_GROWING_TIME;
                dir = refMario.getDir();
            }
        }
            updateAnimation();
    }

    @Override
    public void killed() {
        hitMario();
    }

    @Override
    public void hitMario() {
        //Set to fire
        if (refMario.getCurrentState() == MARIO_STATE_SMALL || refMario.getCurrentState() == MARIO_STATE_NORMAL) {
            Sound.playSound(SOUND_POWERUP_CONSUMED);
        } else {
            Sound.playSound(SOUND_1UP);
        }
        refMario.changeState(MARIO_STATE_FIRE);
        dead = true;
    }

}
