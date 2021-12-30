/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.others;

import app.wrk.entities.Entity_Mario;
import app.wrk.map.Map;

/**
 *
 * @author lione_000
 */
public class Camera {

    private int xOffSet;
    private int yOffSet;
    private boolean locked;

    public Camera() {
        xOffSet = 0;
        yOffSet = 0;
    }

    public void reset() {
        xOffSet = 0;
        yOffSet = 0;
    }

    public void update(Entity_Mario mario, Map map) {

        //Mario can't go back
        int oldXOffSet = xOffSet;

        //xOffSet
        //Left
        if (!mario.isNoClip()) {
            if (mario.getX() < 500) {
                xOffSet = 0;
            } else {
                xOffSet = (int) (mario.getX() - 500);
            }
            //Right
            if (mario.getX() > map.getWidth() * 32) {
                xOffSet = map.getWidth() * 32 - 500;
            }
        } else {
            xOffSet = (int) (mario.getX() - 500);
        }
        //Mario can't bo back
        if (!mario.isNoClip()) {
            if (xOffSet < oldXOffSet) {
                xOffSet = oldXOffSet;
            }
        }

        //yOffSet
            if (!mario.isNoClip()) {
                if (!locked) {
                if (mario.getY() < 500) {
                    yOffSet = (int) (mario.getY() - 500);
                } else {
                    yOffSet = 0;
                }
                } else {
                    yOffSet = map.getHeight() * 32 / 12;
                }
                    
            } else {
                yOffSet = (int) (mario.getY() - 500);
            }

    }

    public int getxOffSet() {
        return xOffSet;
    }

    public void setxOffSet(int xOffSet) {
        this.xOffSet = xOffSet;
    }

    public int getyOffSet() {
        return yOffSet;
    }

    public void setyOffSet(int yOffSet) {
        this.yOffSet = yOffSet;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
