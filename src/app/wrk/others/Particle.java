/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.others;

import app.wrk.Constantes;
import app.wrk.animation.Timer;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author lione_000
 */
public class Particle implements Constantes {

    private int x;
    private int y;
    private float velX;
    private float velY;
    private Image sprite;
    private int ttl;
    private float creationTime;
    private boolean dead;
    private int currentRotation;
    private int rotation;

    public Particle(int x, int y, float velX, float velY, Image sprite, int ttl, int rotation) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.sprite = sprite;
        this.ttl = ttl;
        creationTime = Timer.getCurrentTimePassed();
        dead = false;
        currentRotation = 0;
        this.rotation = rotation;
    }

    public static ArrayList<Particle> createParticles(int posX, int posY, float velX, float velY, Image sprite, int ttl) {

        ArrayList<Particle> particles = new ArrayList();

        //Top left+
        particles.add(new Particle(posX, posY, -velX * 2f, -velY * 2f, sprite, ttl, 1));

        //Top right+
        particles.add(new Particle(posX, posY, velX * 2f, -velY * 2f, sprite, ttl, 0));

        //Top left+
        particles.add(new Particle(posX, posY, -velX, -velY, sprite, ttl, 0));

        //Top right+
        particles.add(new Particle(posX, posY, velX, -velY, sprite, ttl, 1));
        return particles;
    }

    public void update() {

        currentRotation = rotation == 0 ? currentRotation + 10 : currentRotation - 10;

        currentRotation = currentRotation > 360 ? 0 : currentRotation;
        currentRotation = currentRotation < 0 ? 360 : currentRotation;

        if (Timer.getCurrentTimePassed() - creationTime > ttl) {
            dead = true;
        }

        //X Slow
        if (velX < 0) {
            velX += PARTICLE_AIR_SLOW_EFFECT;
        } else if (velX > 0) {
            velX -= PARTICLE_AIR_SLOW_EFFECT;
        }

        //Y (Gravity)
        velY += PARTICLE_GRAVITY;

        //Changes poses
        x += velX;
        y += velY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

}
