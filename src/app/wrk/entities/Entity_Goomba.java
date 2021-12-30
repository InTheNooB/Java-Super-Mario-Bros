/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Constantes;
import static app.wrk.Constantes.SOUND_BUMP;
import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;
import app.wrk.others.Sound;

/**
 *
 * @author lione_000
 */
public class Entity_Goomba extends Entity implements Constantes {

    private int animationAlive;
    private int animationDead;

    public Entity_Goomba(Wrk refWrk, Entity_Mario refMario, int x, int y, int dir, int mapType) {
        super(refWrk, refMario, -1, mapType);
        this.x = x;
        this.y = y;
        this.dir = dir;
        maxXVelocity = 1.3f;
        maxYVelocity = 30;
        velXAcceleration = 2;
        velYAcceleration = 8;
        velX = 0f;
        velY = 0f;
        onGround = true;
        width = 32;
        height = 32;
    }

    @Override
    public void killed() {
        refMario.bumpUp();
        dying = true;
        deathTime = refWrk.getTimer().getCurrentTimePassed();
        Sound.playSound(SOUND_BUMP);
    }

    @Override
    public void updateAnimation() {
        //Dead animation
        if (dying) {
            animation.setCurrentCategorie(animationDead);
            if (refWrk.getTimer().getCurrentTimePassed() - deathTime >= deathAnimationTime * 1000) {
                dead = true;
            }
        } else {
            animation.updateAnimation();
        }
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animationAlive = animation.importAnimation("entity_goomba_alive");
                animationDead = animation.importAnimation("entity_goomba_dead");
                break;
            case MAP_TYPE_UNDERGROUND:
                animationAlive = animation.importAnimation("entity_goomba_alive_underground");
                animationDead = animation.importAnimation("entity_goomba_dead_underground");
                break;
            case MAP_TYPE_SKY:
                animationAlive = animation.importAnimation("entity_goomba_alive_underground");
                animationDead = animation.importAnimation("entity_goomba_dead_underground");
                break;
        }
        animation.setCurrentCategorie(animationAlive);
    }

    public float getDeathAnimationTime() {
        return deathAnimationTime;
    }

    public void setDeathAnimationTime(float deathAnimationTime) {
        this.deathAnimationTime = deathAnimationTime;
    }

}
