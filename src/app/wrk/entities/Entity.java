/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Constantes;
import app.wrk.Wrk;
import app.wrk.map.blocks.Block;
import app.wrk.animation.AnimationSet;
import app.wrk.others.Physics;
import java.awt.Image;

/**
 *
 * @author lione_000
 */
public abstract class Entity implements Constantes {

    //Other
    protected Entity_Mario refMario;
    protected Wrk refWrk;
    protected boolean dead;
    protected int id;
    protected boolean blockContent;

    //Mouvmeent
    protected int dir;
    protected float x;
    protected float y;
    protected float velX;
    protected float velY;

    protected float maxXVelocity;
    protected float maxXAirVelocity;
    protected int maxYVelocity;
    protected float velXAcceleration;
    protected float velYAcceleration;
    protected float gravity;
    protected boolean onGround;

    protected int mapType;

    //Size
    protected int width;
    protected int height;

    //Animation
    protected AnimationSet animation;
    protected boolean dying;
    protected float deathAnimationTime;
    protected float deathTime;

    //Animation
    protected Entity(Wrk wrk, int mapType) {
        refWrk = wrk;
        dead = false;
        deathAnimationTime = 0.5f;
        blockContent = false;
        this.mapType = mapType;
        setupAnimation();
    }

    protected Entity(Wrk wrk, Entity_Mario mario, int id, int mapType) {
        velX = 0;
        velY = 0;
        refWrk = wrk;
        refMario = mario;
        dead = false;
        dying = false;
        this.id = id;
        deathAnimationTime = 0.5f;
        blockContent = false;
        this.mapType = mapType;
        setupAnimation();
    }

    public abstract void setupAnimation();

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

        //Add X slow effect
        if (velX > 0) {
            velX -= GROUND_SLOW_EFFECT;
        } else if (velX < 0) {
            velX += GROUND_SLOW_EFFECT;
        }

        //Calculate new poses
        newX += velX;
        newY += velY;
        collision(newX, newY);
    }

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
                    //Test for "onGround"
                    if (velY > 0) {
                        onGround = true;
                    }
                    velY = 0;
                }
            }
        }
        //Test X for collision with others entity
        for (Entity e : refWrk.getEntities()) {
            if (e != this && id != POWERUP_ID) {
                //X Collision
                if (!collBlockX) {
                    if (e.getId() != POWERUP_ID && !e.isDying() && !e.isDead()) {
                        if (Physics.collision(newX, y, width, height, e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                            //collision !
                            velX = -velX;
                            collBlockX = true;
                        }
                    }
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
                    if (refMario.getY() + refMario.getHeight() - 20 < y) {
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

    public void killed() {
        deathTime = refWrk.getTimer().getCurrentTimePassed();
    }

    public void hitMario() {
        refMario.hit();
    }

    public void updateAnimation() {
        animation.updateAnimation();
    }

    public void switchDir() {
        if (dir == LEFT) {
            dir = RIGHT;
        } else {
            dir = LEFT;
        }

        velX = -velX;
    }

    public void update() {
        move();
        updateAnimation();
    }

    public Image getSpriteToDraw() {
        return animation.getSpriteToDraw();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }


    public float getMaxXVelocity() {
        return maxXVelocity;
    }

    public void setMaxXVelocity(float maxXVelocity) {
        this.maxXVelocity = maxXVelocity;
    }

    public float getVelXAcceleration() {
        return velXAcceleration;
    }

    public void setVelXAcceleration(float velXAcceleration) {
        this.velXAcceleration = velXAcceleration;
    }

    public float getVelYAcceleration() {
        return velYAcceleration;
    }

    public void setVelYAcceleration(float velYAcceleration) {
        this.velYAcceleration = velYAcceleration;
    }

    public int getMaxYVelocity() {
        return maxYVelocity;
    }

    public void setMaxYVelocity(int maxYVelocity) {
        this.maxYVelocity = maxYVelocity;
    }

    public void setVelYAcceleration(int velYAcceleration) {
        this.velYAcceleration = velYAcceleration;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public Wrk getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    public Entity_Mario getRefMario() {
        return refMario;
    }

    public void setRefMario(Entity_Mario refMario) {
        this.refMario = refMario;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AnimationSet getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationSet animation) {
        this.animation = animation;
    }

    public boolean isBlockContent() {
        return blockContent;
    }

    public void setBlockContent(boolean blockContent) {
        this.blockContent = blockContent;
    }

    public float getDeathAnimationTime() {
        return deathAnimationTime;
    }

    public void setDeathAnimationTime(float deathAnimationTime) {
        this.deathAnimationTime = deathAnimationTime;
    }

    public float getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(float deathTime) {
        this.deathTime = deathTime;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

}
