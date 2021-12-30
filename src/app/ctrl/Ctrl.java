/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ctrl;

import app.ihm.Frame;
import app.wrk.Constantes;
import app.wrk.others.Camera;
import app.wrk.Wrk;
import app.wrk.map.blocks.Block;
import app.wrk.entities.Entity;
import app.wrk.entities.Entity_Mario;
import app.wrk.map.Map;
import app.wrk.others.Particle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author lione_000
 */
public class Ctrl implements Constantes {

    private Frame refFrame;
    private Wrk refWrk;

    public Ctrl() {

    }

    public void draw(Entity_Mario mario, ArrayList<Block> blocks, Camera cam, ArrayList<Entity> entities, Map map, ArrayList<Particle> particles) {
        refFrame.draw(mario, blocks, cam, entities, map, particles);
    }

    public void keyPressed(KeyEvent e, boolean in) {
        if (in) {
            switch (e.getKeyCode()) {
                case DEBUG_SHOW_HITBOX:
                    refFrame.setDebugShowHitbox();
                    break;
                case DEBUG_NOCLIP:
                    refWrk.setNoClip(refWrk.getNoClip() ? false : true);
                    break;
                default:
                    break;
            }
        }

        refWrk.move(e, in);

    }

    public void setTitle(String string) {
        refFrame.setTitle(string);
    }

    public void startUp() {
        refWrk.startUp();
    }

    public Frame getRefFrame() {
        return refFrame;
    }

    public void setRefFrame(Frame refFrame) {
        this.refFrame = refFrame;
    }

    public Wrk getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

}
