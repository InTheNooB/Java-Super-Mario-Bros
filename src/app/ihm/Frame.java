/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihm;

import app.ctrl.Ctrl;
import app.wrk.others.Camera;
import app.wrk.map.blocks.Block;
import app.wrk.entities.Entity;
import app.wrk.entities.Entity_Mario;
import app.wrk.map.Map;
import app.wrk.others.Particle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author lione_000
 */
public class Frame extends JFrame implements KeyListener {

    private Panel pan;
    private Ctrl refCtrl;

    public Frame() {
        setup();
        screenSetup();
    }

    private void setup() {
        pan = new Panel();
    }

    private void screenSetup() {
        this.setVisible(true);
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(pan);
        this.setResizable(false);
        this.setSize(1000, 1020);
    }

    public void draw(Entity_Mario mario, ArrayList<Block> blocks, Camera cam, ArrayList<Entity> entities, Map map, ArrayList<Particle> particles) {
        pan.setMario(mario);
        pan.setBlocks(blocks);
        pan.setCam(cam);
        pan.setEntities(entities);
        pan.setMap(map);
        pan.setParticles(particles);
        pan.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        refCtrl.keyPressed(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        refCtrl.keyPressed(e, false);
    }

    public Panel getPan() {
        return pan;
    }

    public void setPan(Panel pan) {
        this.pan = pan;
    }

    public Ctrl getRefCtrl() {
        return refCtrl;
    }

    public void setRefCtrl(Ctrl refCtrl) {
        this.refCtrl = refCtrl;
    }

    public void setDebugShowHitbox() {
        pan.setDebugShowHitbox(pan.isDebugShowHitbox() ? false : true);
    }

}
