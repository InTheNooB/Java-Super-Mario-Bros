/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Wrk;
import app.wrk.map.blocks.Block;
import app.wrk.animation.AnimationSet;
import app.wrk.others.Physics;

/**
 *
 * @author lione
 */
public class Entity_Fireball extends Entity {

    private int animationLeft;
    private int animationRight;
    private int animationExplode;
    private int currentBounces;

    public Entity_Fireball(Wrk wrk) {
        super(wrk, MAP_TYPE_NORMAL);
    }

    public Entity_Fireball(Wrk wrk, Entity_Mario refMario, int x, int y, int dir) {
        super(wrk, refMario, FIREBALL_ID, MAP_TYPE_NORMAL);
        refMario.setFireballAmount(refMario.getFireballAmount() + 1);
        this.width = 16;
        this.height = 16;
        this.x = x;
        this.y = y;
        this.dir = dir;
        velX = 0;
        velY = 0;
        maxXVelocity = 5;
        maxYVelocity = 30;
        velXAcceleration = 2;
        velYAcceleration = 5;
        deathAnimationTime = 0.3f;
        deathTime = 0;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        animationRight = animation.importAnimation("entity_fireball_right");
        animationLeft = animation.importAnimation("entity_fireball_left");
        animationExplode = animation.importAnimation("entity_fireball_explode");

        if (dir == LEFT) {
            animation.setCurrentCategorie(animationLeft);
        } else {
            animation.setCurrentCategorie(animationRight);
        }
    }

    @Override
    public void move() {
        if (dying) {
            return;
        }
        //Test for "onGround"
        float newX = x, newY = y;
        if (velY < 0) {
            onGround = false;
        }

        //Set Velocities
        if (dir == 1) {
            velX -= velXAcceleration;
        } else {
            velX += velXAcceleration;
        }

        //Clamp velocities
        if (velX > maxXVelocity) {
            velX = maxXVelocity;
        } else if (velX < -maxXVelocity) {
            velX = -maxXVelocity;
        }
        if (velY > maxYVelocity) {
            velY = maxYVelocity;
        } else if (velY < -maxYVelocity) {
            velY = -maxYVelocity;
        }

        //Add gravity
        velY += GRAVITY;

        //Calculate new poses
        newX += velX;
        newY += velY;
        collision(newX, newY);
    }

    public void update() {
        if (!dying) {
            move();
        } else {
            animation.setCurrentCategorie(animationExplode);
            if (deathTime != 0) {
                if ((System.nanoTime() - deathTime) / 10000000 >= 7) {
                    dead = true;
                }
            } else {
                deathTime = System.nanoTime();
                velX = 0;
                velY = 0;
            }
        }

        updateAnimation();
    }

    @Override
    public void collision(float newX, float newY) {

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
                }
            }
        }
        //Test X for collision with others entity
        for (Entity e : refWrk.getEntities()) {
            if (e != this && !e.isDead()) {
                //X Collision
                if (!collBlockX) {
                    if (Physics.collision(newX, y, width, height, e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                        //collision !
                        killed();
                        e.setDead(true);
                    }
                }
            }
        }

        //If no collision, then set new poses
        if (!collBlockX) {
            x = newX;
        } else {
            killed();
        }

        if (!collBlockY) {
            y = newY;
        } else {
            velY = -FIREBALL_BOUNCE_VELOCITY;
            currentBounces++;
            if (currentBounces >= FIREBALL_BOUNCES_AMOUNT) {
                killed();
            }
        }
    }

    @Override
    public void killed() {
        dying = true;
        refMario.setFireballAmount(refMario.getFireballAmount() - 1);
        if (refMario.getFireballAmount() < 0) {
            refMario.setFireballAmount(0);
        }
    }

    public int getAnimationLeft() {
        return animationLeft;
    }

    public void setAnimationLeft(int animationLeft) {
        this.animationLeft = animationLeft;
    }

    public int getAnimationRight() {
        return animationRight;
    }

    public void setAnimationRight(int animationRight) {
        this.animationRight = animationRight;
    }

    public int getCurrentBounces() {
        return currentBounces;
    }

    public void setCurrentBounces(int currentBounces) {
        this.currentBounces = currentBounces;
    }

}
