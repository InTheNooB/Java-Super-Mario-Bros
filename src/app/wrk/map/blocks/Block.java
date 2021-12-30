/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map.blocks;

import app.wrk.Constantes;
import app.wrk.Wrk;
import app.wrk.entities.Entity;
import app.wrk.animation.AnimationSet;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author lione_000
 */
public abstract class Block implements Constantes {

    protected Wrk refWrk;

    protected float x;
    protected float y;
    protected float tempY;
    protected float velY;

    protected int width;
    protected int height;

    protected boolean breakable;
    protected boolean transparent;

    protected boolean broken;

    protected ArrayList<Entity> content;

    protected int mapType;

    protected char id; //Used for the texture

    //Animation    
    protected AnimationSet animation;
    protected float animationChangeTime;

    public Block(Wrk refWrk, float x, float y, boolean breakable, char id, int width, int height, int mapType) {
        setup(refWrk, x, y, breakable, id, width, height, mapType);
        transparent = false;
        if (content == null) {
            content = new ArrayList();
        }
        setupSprites();
    }

    public Block(Wrk refWrk, float x, float y, boolean breakable, char id, int width, int height, boolean transparent, int mapType) {
        setup(refWrk, x, y, breakable, id, width, height, mapType);
        this.transparent = transparent;
        if (content == null) {
            content = new ArrayList();
        }
        setupSprites();
    }

    private void setup(Wrk refWrk, float x, float y, boolean breakable, char id, int width, int height, int mapType) {
        velY = 0;
        this.refWrk = refWrk;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.breakable = breakable;
        this.id = id;
        this.mapType = mapType;
    }

    public void bumpUp() {
        velY = -BLOCK_BUMP_UP_VEL;
        tempY = y;
    }

    public void update() {
        updateAnimation();
        if (velY != 0) {
            velY += GRAVITY;
            y += velY;
            if (y >= tempY) {
                y = tempY;
                velY = 0;
            }
        }
    }

    public void updateAnimation() {
        animation.updateAnimation();
    }

    public abstract void setupSprites();

    public Image getSpriteToDraw() {
        return animation.getSpriteToDraw();
    }

    public void kill() {
        broken = true;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public AnimationSet getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationSet animation) {
        this.animation = animation;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public Wrk getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    public ArrayList<Entity> getContent() {
        return content;
    }

    public void setContent(ArrayList<Entity> content) {
        this.content = content;
    }

    public float getAnimationChangeTime() {
        return animationChangeTime;
    }

    public void setAnimationChangeTime(float animationChangeTime) {
        this.animationChangeTime = animationChangeTime;
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

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
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

    public float getTempY() {
        return tempY;
    }

    public void setTempY(float tempY) {
        this.tempY = tempY;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

}
