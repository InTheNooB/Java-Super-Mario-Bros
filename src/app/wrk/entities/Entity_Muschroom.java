/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Constantes;
import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;
import app.wrk.map.blocks.Block;
import app.wrk.others.Physics;
import app.wrk.others.Sound;

/**
 *
 * @author lione_000
 */
public class Entity_Muschroom extends Entity implements Constantes {

    private boolean grown;
    private double startTime;

    public Entity_Muschroom(Wrk refWrk, Entity_Mario refMario, float x, float y, int dir, int mapType, boolean grown) {
        super(refWrk, refMario, POWERUP_ID, mapType);
        this.x = x;
        this.y = y;
        this.dir = dir;
        maxXVelocity = 2;
        maxYVelocity = 30;
        velXAcceleration = 2;
        velYAcceleration = 8;
        velX = 0f;
        velY = 0f;
        onGround = true;
        width = 32;
        height = 32;
        this.grown = grown;
        blockContent = true;
        startTime = 0;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("entity_muschroom"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("entity_muschroom_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("entity_muschroom_sky"));
                break;
        }
    }

    @Override
    public void killed() {
        hitMario();
    }

    @Override
    public void hitMario() {
        //Set to normal
        if (refMario.getCurrentState() == MARIO_STATE_SMALL) {
            Sound.playSound(SOUND_POWERUP_CONSUMED);
            refMario.changeState(MARIO_STATE_NORMAL);
        } else {
            Sound.playSound(SOUND_1UP);
        }
        dead = true;
    }

    @Override
    public void update() {
        if (grown) {
            move();
            updateAnimation();
        } else {
            if (startTime != 0) {
                if ((System.nanoTime() - startTime) / 10000000 >= POWERUP_GROWING_TIME) {
                    grown = true;
                } else {
                    y += velY;
                }
            } else {
                startTime = System.nanoTime();
                velY = -35 / POWERUP_GROWING_TIME;
                dir = refMario.getDir();
            }
        }
    }

    public void collision(int newX, int newY) {
        //Test if these new poses have collision with blocks
        boolean collBlockX = false;
        boolean collBlockY = false;
        for (Block b : refWrk.getBlocks()) {
            //X Collision
            if (!collBlockX) {
                if (Physics.collision(newX, y, width, height, b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                    //collision !
                    collBlockX = true;
                }
            }
            //Y Collisison
            if (!collBlockY) {
                if (Physics.collision(x, newY, width, height, b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                    //collision !
                    collBlockY = true;
                    //Test for "onGround"
                    if (velY > 0) {
                        onGround = true;
                    }
                    velY = 0;
                }
            }
        }

        //Test for collision with mario
        if (!refMario.isInvincible()) {
            if (Physics.collision(refMario.getX(), refMario.getY(), refMario.getWidth(), refMario.getHeight(), newX, newY, width, height)) {
                //Collision with mario
                //If mario isn't on ground
                //If mario's Y is slightly lower then the goomba's head
                if (!refMario.isOnGround()) {
                    if (refMario.getY() + refMario.getHeight() - 10 < y) {
                        killed();
                    } else {
                        hitMario();
                    }
                } else {
                    hitMario();
                }
            }
        }

        //If no collision, then set new poses
        if (!collBlockX) {
            x = newX;
        } else {
            if (dir == LEFT) {
                dir = RIGHT;
            } else {
                dir = LEFT;
            }
        }

        if (!collBlockY) {
            y = newY;
        }
    }

    public boolean isGrown() {
        return grown;
    }

    public void setGrown(boolean grown) {
        this.grown = grown;
    }

}
