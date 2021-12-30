/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import static app.wrk.Constantes.POWERUP_ID;
import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;
import app.wrk.animation.Timer;
import app.wrk.others.Physics;
import app.wrk.others.Sound;

/**
 *
 * @author lione_000
 */
public class Entity_Coin extends Entity {

    private int ttl;
    private float creationTime;
    private boolean created;
    private boolean inBlock;

    public Entity_Coin(Wrk wrk, Entity_Mario mario, float x, float y, int mapType, boolean inBlock) {
        super(wrk, mario, POWERUP_ID, mapType);
        this.x = x;
        this.y = y;
        setupVelocity();
        ttl = 1000;
        width = 16;
        height = 32;
        dead = false;
        created = false;
        blockContent = true;
        this.inBlock = inBlock;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("entity_coin"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("entity_coin_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("entity_coin_sky"));
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
    public void move() {
        velY += GRAVITY;
        y += velY;
    }

    public void collision() {
        //Test for collision with mario
        if (Physics.collision(refMario.getX(), refMario.getY(), refMario.getWidth(), refMario.getHeight(), x, y, width, height)) {
            if (!refMario.isNoClip()) {
                dead = true;
                Sound.playSound(SOUND_COIN);
            }
        }
    }

    @Override
    public void update() {
        updateAnimation();
        if (inBlock) {
            if (!created) {
                created = true;
                creationTime = refWrk.getTimer().getCurrentTimePassed();
            }
        } else {
            collision();
        }

        if (created) {
            move();
            updateTimer();
        }

    }

    private void updateTimer() {
        if (Timer.getCurrentTimePassed() - creationTime > ttl) {
            dead = true;
        }
    }

    private void setupVelocity() {
        velX = 0;
        velY = -10;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public float getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(float creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean isInBlock() {
        return inBlock;
    }

    public void setInBlock(boolean inBlock) {
        this.inBlock = inBlock;
    }

}
