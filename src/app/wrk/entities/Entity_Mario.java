/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.entities;

import app.wrk.Constantes;
import app.wrk.others.Physics;
import app.wrk.Wrk;
import app.wrk.map.blocks.Block;
import app.wrk.animation.AnimationSet;
import app.wrk.others.Sound;

/**
 *
 * @author lione_000
 */
public class Entity_Mario extends Entity implements Constantes {

    //State
    private int currentState;

    //Movements
    private boolean moveTop;
    private boolean moveRight;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean crouching;
    private boolean noClip;
    private boolean goingToEndCastle;

    //Invincible Time
    private boolean invincible;
    private float invincibleTime;
    private float invincibleStartTime;
    private float invincibleAnimationChangeTime;
    private float lastTimeInvincibleAnimationChange;
    private boolean invisible;

    //Fireballs
    private float fireballCooldownLastUpdate;
    private float fireballAmount;

    private boolean onFlag;

    private int animationSmallStill;
    private int animationSmallLeft;
    private int animationSmallRight;
    private int animationSmallJump;
    private int animationSmallFlag;
    private int animationNormalStill;
    private int animationNormalLeft;
    private int animationNormalRight;
    private int animationNormalJump;
    private int animationNormalCrouch;
    private int animationNormalFlag;
    private int animationFireStill;
    private int animationFireLeft;
    private int animationFireRight;
    private int animationFireJump;
    private int animationFireCrouch;
    private int animationFireFlag;

    public Entity_Mario(Wrk refWrk) {
        super(refWrk, MAP_TYPE_NORMAL);
        mouvementSetup();
        statesSetup();
        setupInvincible();
    }

    @Override
    public void setupAnimation() {
        animation = new AnimationSet();
        animationSmallStill = animation.importAnimation("entity_mario_small_still");
        animationSmallLeft = animation.importAnimation("entity_mario_small_left");
        animationSmallRight = animation.importAnimation("entity_mario_small_right");
        animationSmallJump = animation.importAnimation("entity_mario_small_jump");
        animationSmallFlag = animation.importAnimation("entity_mario_small_flag");
        animationNormalStill = animation.importAnimation("entity_mario_normal_still");
        animationNormalLeft = animation.importAnimation("entity_mario_normal_left");
        animationNormalRight = animation.importAnimation("entity_mario_normal_right");
        animationNormalJump = animation.importAnimation("entity_mario_normal_jump");
        animationNormalCrouch = animation.importAnimation("entity_mario_normal_crouch");
        animationNormalFlag = animation.importAnimation("entity_mario_normal_flag");
        animationFireStill = animation.importAnimation("entity_mario_fire_still");
        animationFireLeft = animation.importAnimation("entity_mario_fire_left");
        animationFireRight = animation.importAnimation("entity_mario_fire_right");
        animationFireJump = animation.importAnimation("entity_mario_fire_jump");
        animationFireCrouch = animation.importAnimation("entity_mario_fire_crouch");
        animationFireFlag = animation.importAnimation("entity_mario_fire_flag");
        animation.setCurrentCategorie(animationSmallRight);
    }

    @Override
    public void update() {
        move();
        updateAnimation();
        if (invincible) {
            updateInvincible();
        }

    }

    private void setupInvincible() {
        invincible = false;
        invincibleAnimationChangeTime = 0.25f;
        lastTimeInvincibleAnimationChange = 0;
        invisible = false;
    }

    public void becomeInvincible() {
        this.invincibleTime = MARIO_HIT_INVINCIBLE_DURATION;
        invincibleStartTime = refWrk.getTimer().getCurrentTimePassed();
        invincible = true;
    }

    private void updateInvincible() {
        if (refWrk.getTimer().getCurrentTimePassed() - invincibleStartTime >= invincibleTime * 1000) {
            invincible = false;
            invisible = false;
        }
    }

    private void statesSetup() {
        currentState = MARIO_STATE_NORMAL;
    }

    public void mouvementSetup() {
        maxXVelocity = 4f;
        maxXAirVelocity = 2f;
        maxYVelocity = 10;
        velXAcceleration = 0.15f;
        velYAcceleration = 8.5f;
        velX = 0f;
        velY = 0f;
        fireballCooldownLastUpdate = 0;
        onGround = false;
        crouching = false;
        onFlag = false;
        noClip = false;
    }

    private void noClipMove() {
        if (moveLeft) {
            x -= NO_CLIP_SPEED;
        }
        if (moveRight) {
            x += NO_CLIP_SPEED;
        }
        if (moveDown) {
            y += NO_CLIP_SPEED;
        }
        if (moveTop) {
            y -= NO_CLIP_SPEED;
        }
    }

    @Override
    public void move() {
        if (noClip) {
            noClipMove();
            return;
        }
        if (onFlag) {
            y += END_FLAG_FALLING_SPEED;
            for (Block b : refWrk.getBlocks()) {
                if (b.getId() == LETTER_BLOCK_CONCRETE) {
                    if (Physics.collision(x, y, width, height, b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                        goingToEndCastle = true;
                    }
                }
            }
            return;
        }

        //Test for "onGround"
        if (velY < 0) {
            onGround = false;
        }

        //Calculate new velocities
        if (currentState != 0) {
            if (moveDown) {
                crouching(true);
            } else {
                crouching(false);
            }
        }
        int newX = (int) x, newY = (int) y;
        if (moveTop && onGround) {
            velY -= velYAcceleration;
            onGround = false;
        }

        // Velocity difference depends on the current state (air or ground)
        if (!crouching) {
            if (moveRight) {
                velX += velXAcceleration;
                if (onGround) {
                    dir = RIGHT;
                }
            } else if (moveLeft) {
                velX -= velXAcceleration;
                if (onGround) {
                    dir = LEFT;
                }
            }
        } else {
            if (moveRight) {
                dir = RIGHT;
            } else if (moveLeft) {
                dir = LEFT;
            }
        }

        //Air mouvements
        if (!onGround) {
            if (moveRight && dir == LEFT) {
                velX += 0.5;
            } else if (moveLeft && dir == RIGHT) {
                velX -= 0.5;
            }
        }

        //Add gravity
        if (moveTop) {
            velY += GRAVITY - 0.19f;
        } else {
            velY += GRAVITY;
        }

        //Keep on walking bug
//        velX = (dir == LEFT && velX > 0 && onGround) || (dir == RIGHT && velX < 0 && onGround) ? 0 : velX;
        //Add X slow effect
        if ((!moveLeft && !moveRight && velX != 0) || (velX > 0 && dir == LEFT) || (velX < 0 && dir == RIGHT) || crouching) {
            if (velX > 0) {
                if (velX > GROUND_SLOW_EFFECT) {
                    velX -= GROUND_SLOW_EFFECT;
                } else {
                    velX = 0;
                }
            } else if (velX < 0) {
                if (velX < -GROUND_SLOW_EFFECT) {
                    velX += GROUND_SLOW_EFFECT;
                } else {
                    velX = 0;
                }
            }
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

        //Calculate new poses
        newX += velX;
        newY += velY;

        //Mario can't go back to after the cam
        if (newX < refWrk.getCam().getxOffSet()) {
            newX = refWrk.getCam().getxOffSet();
        }

        collision(newX, newY);
    }

    @Override
    public void collision(float newX, float newY) {
        //Test if these new poses have collision with blocks that are around mario
        boolean continueCheck;
        boolean collX = false;
        boolean collY = false;
        for (Block b : refWrk.getBlocks()) {
            continueCheck = false;
            //Check if around mario
            //Left Side
            if (b.getX() <= x) {
                if (b.getX() > refWrk.getCam().getxOffSet() - ENTITY_UPDATE_SCREEN_OFFSET) {
                    continueCheck = true;
                }
                //Right Side 
            } else if (b.getX() >= x) {
                if (b.getX() < refWrk.getCam().getxOffSet() + 1000 + ENTITY_UPDATE_SCREEN_OFFSET) {
                    continueCheck = true;
                }
            }
            if (!continueCheck) {
                continue;
            }

            //X Collision
            if (!collX) {
                if (Physics.collision(newX, y, width, height, b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                    if (b.isTransparent()) {
                        transparentCollision(b);
                        continue;
                    }
                    if (b.getId() != OTHER_MOVING_PLATFORM) {
                        //collision !
                        collX = true;
                    }
                }
            }
            //Y Collisison
            if (!collY) {
                if (Physics.collision(x, newY, width, height, b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
                    if (b.isTransparent()) {
                        transparentCollision(b);
                        continue;
                    }
                    //Test for "onGround"
                    if (velY > 0) {
                        onGround = true;
                    } else {
                        onGround = false;
                    }

                    if (b.getId() == OTHER_MOVING_PLATFORM) {
                        y = b.getY() - height;
                        onGround = true;
                    }

                    //collision !
                    moveTop = false;
                    velY = 1;
                    //Test if the block can be broken
                    if (b.isBreakable()) {
                        if (b.getY() + b.getHeight() - 10 < y) {
                            switch (b.getId()) {
                                case LETTER_BLOCK_MISTERY_FULL_MUSCHROOM:
                                    b.kill();
                                    velY = 4;
                                    moveTop = false;
                                    Sound.playSound(SOUND_POWERUP_APPEARS);
                                    break;
                                case LETTER_BLOCK_BRICK:
                                    if (currentState != MARIO_STATE_SMALL) {
                                        b.kill();
                                        velY = 4;
                                        moveTop = false;
                                    } else {
                                        b.bumpUp();
                                    }
                                    break;
                                case LETTER_BLOCK_MISTERY_FULL_COIN:
                                    b.kill();
                                    velY = 4;
                                    moveTop = false;
                                    Sound.playSound(SOUND_COIN);
                                    break;
                                case LETTER_BLOCK_MISTERY_FULL_FLOWER:
                                    b.kill();
                                    velY = 4;
                                    moveTop = false;
                                    Sound.playSound(SOUND_POWERUP_APPEARS);
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                    collY = true;
                }
            }
        }

        //If no collision, then set new poses
        if (!onFlag) {
            if (!collX) {
                x = newX;
            }
            if (!collY) {
                y = newY;
            }
        }
    }

    @Override
    public void updateAnimation() {
        if (invincible) {
            if (refWrk.getTimer().getCurrentTimePassed() - lastTimeInvincibleAnimationChange >= invincibleAnimationChangeTime * 1000) {
                lastTimeInvincibleAnimationChange = refWrk.getTimer().getCurrentTimePassed();
                invisible = (invisible) ? false : true;
            }
        }
        if (onFlag) {
            switch (currentState) {
                case MARIO_STATE_SMALL:
                    animation.setCurrentCategorie(animationSmallFlag);
                    break;
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalFlag);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireFlag);
                    break;
                default:
                    break;
            }
            if (dir == LEFT) {
                animation.setCurrentSprite(ANIMATION_MARIO_LEFT);
            } else {
                animation.setCurrentSprite(ANIMATION_MARIO_RIGHT);
            }
            return;
        }

        if (crouching && currentState != MARIO_STATE_SMALL) {
            switch (currentState) {
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalCrouch);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireCrouch);
                    break;
                default:
                    break;
            }
            if (dir == LEFT) {
                animation.setCurrentSprite(ANIMATION_MARIO_LEFT);
            } else {
                animation.setCurrentSprite(ANIMATION_MARIO_RIGHT);
            }

        } else if (!onGround) { //Jump 
            switch (currentState) {
                case MARIO_STATE_SMALL:
                    animation.setCurrentCategorie(animationSmallJump);
                    break;
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalJump);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireJump);
                    break;
                default:
                    break;
            }
            if (dir == LEFT) {
                animation.setCurrentSprite(ANIMATION_MARIO_LEFT);
            } else {
                animation.setCurrentSprite(ANIMATION_MARIO_RIGHT);
            }
        } else if (velX == 0) { // Standing Still
            switch (currentState) {
                case MARIO_STATE_SMALL:
                    animation.setCurrentCategorie(animationSmallStill);
                    break;
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalStill);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireStill);
                    break;
                default:
                    break;
            }
            if (dir == LEFT) {
                animation.setCurrentSprite(ANIMATION_MARIO_LEFT);
            } else {
                animation.setCurrentSprite(ANIMATION_MARIO_RIGHT);
            }
        } else if (velX < 0) { // Moving Left
            switch (currentState) {
                case MARIO_STATE_SMALL:
                    animation.setCurrentCategorie(animationSmallLeft);
                    break;
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalLeft);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireLeft);
                    break;
                default:
                    break;
            }
        } else { // Moving right
            switch (currentState) {
                case MARIO_STATE_SMALL:
                    animation.setCurrentCategorie(animationSmallRight);
                    break;
                case MARIO_STATE_NORMAL:
                    animation.setCurrentCategorie(animationNormalRight);
                    break;
                case MARIO_STATE_FIRE:
                    animation.setCurrentCategorie(animationFireRight);
                    break;
                default:
                    break;
            }

        }

        animation.updateAnimation();
    }

    public void setAnimationStill() {
        switch (currentState) {
            case MARIO_STATE_SMALL:
                animation.setCurrentCategorie(animationSmallStill);
                break;
            case MARIO_STATE_NORMAL:
                animation.setCurrentCategorie(animationNormalStill);
                break;
            case MARIO_STATE_FIRE:
                animation.setCurrentCategorie(animationFireStill);
                break;
            default:
                break;
        }
    }

    public void hit() {
        System.out.println(onGround);
        if (!noClip) {
            if (currentState == MARIO_STATE_SMALL) {
                dead = true;
                Sound.playSound(SOUND_DEATH);
            } else {
                becomeInvincible();
                changeState(currentState - 1);
            }
        }
    }

    public void changeState(int i) {
        if (i == MARIO_STATE_SMALL && currentState != MARIO_STATE_SMALL) {
            //Become small
            width = 32;
            height = 32;
        } else if (i == MARIO_STATE_SMALL && currentState == MARIO_STATE_SMALL) {
            //Keep on being small
            width = 32;
            height = 32;
        } else if (i == MARIO_STATE_NORMAL) {
            //Become normal
            width = 32;
            height = 64;
            if (currentState == MARIO_STATE_SMALL) {
                //Growing to normal
                y = y - 32;
            } else if (currentState == MARIO_STATE_FIRE) {
                //From fire to normal
            }
        } else if (i == MARIO_STATE_FIRE && currentState == MARIO_STATE_SMALL) {
            //Becoming fire
            width = 32;
            height = 64;
            y = y - 32;
        }
        currentState = i;
    }

    private void transparentCollision(Block b) {
        if (b.getId() == OTHER_END_FLAG_BODY || b.getId() == OTHER_END_FLAG_TOP) {
            onFlag = true;
            if (dir == RIGHT) {
                x = b.getX() - 20;
            } else {
                x = b.getX() + 5;
            }
            refWrk.endFlagReached();
        }
    }

    public void respawn() {
        x = refWrk.getMap().getStartX();
        y = refWrk.getMap().getStartY();
        velX = 0;
        velY = 0;
        changeState(MARIO_STATE_SMALL);
    }

    public void bumpUp() {
        velY -= 13;
    }

    public void bumpUp(float velY) {
        this.velY -= velY;
    }

    private void crouching(boolean b) {
        crouching = b;
        if (b) {
            if (height != 32) {
                height = 32;
                width = 32;

                y += 32;
            }
        } else {
            if (height != 64) {
                width = 32;
                height = 64;

                y -= 32;
            }
        }
    }

    public boolean isMoveTop() {
        return moveTop;
    }

    public void setMoveTop(boolean moveTop) {
        this.moveTop = moveTop;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    @Override
    public void killed() {
    }

    @Override
    public void hitMario() {
    }

    public Wrk getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public float getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(float invincibleTime) {
        this.invincibleTime = invincibleTime;
    }

    public float getInvincibleStartTime() {
        return invincibleStartTime;
    }

    public void setInvincibleStartTime(float invincibleStartTime) {
        this.invincibleStartTime = invincibleStartTime;
    }

    public float getInvincibleAnimationChangeTime() {
        return invincibleAnimationChangeTime;
    }

    public void setInvincibleAnimationChangeTime(float invincibleAnimationChangeTime) {
        this.invincibleAnimationChangeTime = invincibleAnimationChangeTime;
    }

    public float getLastTimeInvincibleAnimationChange() {
        return lastTimeInvincibleAnimationChange;
    }

    public void setLastTimeInvincibleAnimationChange(float lastTimeInvincibleAnimationChange) {
        this.lastTimeInvincibleAnimationChange = lastTimeInvincibleAnimationChange;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public void setOnFlag(boolean onFlag) {
        this.onFlag = onFlag;
    }

    public boolean isOnFlag() {
        return onFlag;
    }

    public float getFireballCooldownLastUpdate() {
        return fireballCooldownLastUpdate;
    }

    public void setFireballCooldownLastUpdate(float fireballCooldownLastUpdate) {
        this.fireballCooldownLastUpdate = fireballCooldownLastUpdate;
    }

    public int getAnimationSmallStill() {
        return animationSmallStill;
    }

    public void setAnimationSmallStill(int animationSmallStill) {
        this.animationSmallStill = animationSmallStill;
    }

    public int getAnimationSmallLeft() {
        return animationSmallLeft;
    }

    public void setAnimationSmallLeft(int animationSmallLeft) {
        this.animationSmallLeft = animationSmallLeft;
    }

    public int getAnimationSmallRight() {
        return animationSmallRight;
    }

    public void setAnimationSmallRight(int animationSmallRight) {
        this.animationSmallRight = animationSmallRight;
    }

    public int getAnimationSmallJump() {
        return animationSmallJump;
    }

    public void setAnimationSmallJump(int animationSmallJump) {
        this.animationSmallJump = animationSmallJump;
    }

    public int getAnimationSmallFlag() {
        return animationSmallFlag;
    }

    public void setAnimationSmallFlag(int animationSmallFlag) {
        this.animationSmallFlag = animationSmallFlag;
    }

    public int getAnimationNormalStill() {
        return animationNormalStill;
    }

    public void setAnimationNormalStill(int animationNormalStill) {
        this.animationNormalStill = animationNormalStill;
    }

    public int getAnimationNormalLeft() {
        return animationNormalLeft;
    }

    public void setAnimationNormalLeft(int animationNormalLeft) {
        this.animationNormalLeft = animationNormalLeft;
    }

    public int getAnimationNormalRight() {
        return animationNormalRight;
    }

    public void setAnimationNormalRight(int animationNormalRight) {
        this.animationNormalRight = animationNormalRight;
    }

    public int getAnimationNormalJump() {
        return animationNormalJump;
    }

    public void setAnimationNormalJump(int animationNormalJump) {
        this.animationNormalJump = animationNormalJump;
    }

    public int getAnimationNormalCrouch() {
        return animationNormalCrouch;
    }

    public void setAnimationNormalCrouch(int animationNormalCrouch) {
        this.animationNormalCrouch = animationNormalCrouch;
    }

    public int getAnimationNormalFlag() {
        return animationNormalFlag;
    }

    public void setAnimationNormalFlag(int animationNormalFlag) {
        this.animationNormalFlag = animationNormalFlag;
    }

    public int getAnimationFireStill() {
        return animationFireStill;
    }

    public void setAnimationFireStill(int animationFireStill) {
        this.animationFireStill = animationFireStill;
    }

    public int getAnimationFireLeft() {
        return animationFireLeft;
    }

    public void setAnimationFireLeft(int animationFireLeft) {
        this.animationFireLeft = animationFireLeft;
    }

    public int getAnimationFireRight() {
        return animationFireRight;
    }

    public void setAnimationFireRight(int animationFireRight) {
        this.animationFireRight = animationFireRight;
    }

    public int getAnimationFireJump() {
        return animationFireJump;
    }

    public void setAnimationFireJump(int animationFireJump) {
        this.animationFireJump = animationFireJump;
    }

    public int getAnimationFireCrouch() {
        return animationFireCrouch;
    }

    public void setAnimationFireCrouch(int animationFireCrouch) {
        this.animationFireCrouch = animationFireCrouch;
    }

    public int getAnimationFireFlag() {
        return animationFireFlag;
    }

    public void setAnimationFireFlag(int animationFireFlag) {
        this.animationFireFlag = animationFireFlag;
    }

    public float getFireballAmount() {
        return fireballAmount;
    }

    public void setFireballAmount(float fireballAmount) {
        this.fireballAmount = fireballAmount;
    }

    public boolean isNoClip() {
        return noClip;
    }

    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }

    public boolean isGoingToEndCastle() {
        return goingToEndCastle;
    }

    public void setGoingToEndCastle(boolean goingToEndCastle) {
        this.goingToEndCastle = goingToEndCastle;
    }

}
