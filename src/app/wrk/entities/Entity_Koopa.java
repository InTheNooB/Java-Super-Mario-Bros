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
import app.wrk.others.Sound;

/**
 *
 * @author lione_000
 */
public class Entity_Koopa extends Entity implements Constantes {

    private boolean shellMode;
    private int animationLeft;
    private int animationRight;
    private int animationShell;

    public Entity_Koopa(Wrk wrk, Entity_Mario mario, int x, int y, int dir, int mapType) {
        super(wrk, mario, -1, mapType);
        this.x = x;
        this.y = y;
        this.dir = dir;
        shellMode = false;
        maxXVelocity = 2;
        maxYVelocity = 30;
        velXAcceleration = 2;
        velYAcceleration = 8;
        velX = 0f;
        velY = 0f;
        onGround = true;
        width = 32;
        height = 48;
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animationLeft = animation.importAnimation("entity_koopa_left");
                animationRight = animation.importAnimation("entity_koopa_right");
                animationShell = animation.importAnimation("entity_koopa_shell");
                break;
            case MAP_TYPE_UNDERGROUND:
                animationLeft = animation.importAnimation("entity_koopa_left_underground");
                animationRight = animation.importAnimation("entity_koopa_right_underground");
                animationShell = animation.importAnimation("entity_koopa_shell_underground");
                break;
            case MAP_TYPE_SKY:
                animationLeft = animation.importAnimation("entity_koopa_left_sky");
                animationRight = animation.importAnimation("entity_koopa_right_sky");
                animationShell = animation.importAnimation("entity_koopa_shell_sky");
                break;
        }

        if (dir == LEFT) {
            animation.setCurrentCategorie(animationLeft);
        } else {
            animation.setCurrentCategorie(animationRight);
        }
    }

    private void setShellMode() {
        shellMode = true;

        width = 32;
        height = 28;

        velX = 0;

        refMario.bumpUp();
        Sound.playSound(SOUND_BUMP);
    }

    @Override
    public void killed() {
        dead = true;
        Sound.playSound(SOUND_BUMP);
        refMario.bumpUp();
    }

    @Override
    public void updateAnimation() {
        //Shell animation
        if (shellMode) {
            animation.setCurrentCategorie(animationShell);
        } else {
            if (dir == LEFT) {
                animation.setCurrentCategorie(animationLeft);
            } else {
                animation.setCurrentCategorie(animationRight);
            }
        }
        animation.updateAnimation();
    }

    @Override
    public void move() {
        //Test for "onGround"
        float newX = x, newY = y;
        if (velY < 0) {
            onGround = false;
        }

        if (shellMode) {
            //Set Velocities
            if (velX != 0) {
                if (dir == LEFT) {
                    velX = -SHELLMODE_VELX;
                } else {
                    velX = SHELLMODE_VELX;
                }
            }
        } else {
            //Set Velocities
            if (dir == LEFT) {
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
            //Add X slow effect
            if (velX > 0) {
                velX -= 1;
            } else if (velX < 0) {
                velX += 1;
            }
        }

        //Add gravity
        velY += GRAVITY;

        //Calculate new poses
        newX += velX;
        newY += velY;

        collision(newX, newY);
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
                    if (shellMode) {
                        if (!b.isBreakable()) {
                            collBlockX = true;
                            Sound.playSound(SOUND_SHELL_BOUNCE);
                        } else {
                            b.setBroken(true);
                            if (b.getId() == LETTER_BLOCK_MISTERY_FULL_MUSCHROOM) {
                                //PowerUp Muschroom
                                Sound.playSound(SOUND_POWERUP_APPEARS);
                            } else if (b.getId() == LETTER_BLOCK_MISTERY_FULL_COIN) {
                                //PowerUp Coin
                                Sound.playSound(SOUND_COIN);
                            } else if (b.getId() == LETTER_BLOCK_BRICK) {
                                //Brick
                                Sound.playSound(SOUND_BREAK_BRICK);
                            }
                        }
                    } else {
                        collBlockX = true;
                    }

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
            if (e != this) {
                //X Collision
                if (!collBlockX) {
                    if (Physics.collision(newX, y, width, height, e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                        //collision !
                        if (e.getId() != POWERUP_ID) {
                            if (!shellMode) {
                                collBlockX = true;
                            } else {
                                if (velX != 0) {
                                    e.setDead(true);
                                    Sound.playSound(SOUND_BUMP);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Test for collision with mario
        if (!refMario.isInvincible()) {
            if (Physics.collision(refMario.getX(), refMario.getY(), refMario.getWidth(), refMario.getHeight(), newX, newY, width, height)) {
                //Collision with mario
                //If shellmode isn't on, mario can be hit be going threw it
                //And mario can put it in ShellMode by jumping on it
                //If it's on, then mario can throw it if it's not moving
                //Mario can be hit if it's moving
                //Mario can kill it if coming from top
                if (!shellMode) {
                    //Test if mario jumped on it
                    if (!refMario.isOnGround() && refMario.getVelY() > 0) {
                        //Then mario jumped on it
                        setShellMode();
                    } else {
                        //Then mario went threw it
                        refMario.hit();
                    }
                } else {
                    //ShellMode on
                    if (velX == 0) {
                        //Shell not moving
                        if (refMario.getY() + refMario.getHeight() > y) {
                            if (refMario.getDir() == LEFT) {
                                velX = -SHELLMODE_VELX;
                            } else {
                                velX = SHELLMODE_VELX;
                            }
                            dir = refMario.getDir();
                            // Prevent mario from getting hit
                            newX += velX * 2;
                        }
                    } else {
                        //Shell is moving
                        //Test if mario jumped on it
                        if (!refMario.isOnGround() && refMario.getVelY() > 0) {
                            velX = 0;
                            refMario.bumpUp();
                        } else {
                            refMario.hit();
                        }
                    }
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

    public boolean isShellMode() {
        return shellMode;
    }

    public void setShellMode(boolean shellMode) {
        this.shellMode = shellMode;
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

    public int getAnimationShell() {
        return animationShell;
    }

    public void setAnimationShell(int animationShell) {
        this.animationShell = animationShell;
    }

}
