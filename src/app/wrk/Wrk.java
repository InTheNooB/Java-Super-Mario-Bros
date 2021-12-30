/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk;

import app.ctrl.Ctrl;
import app.wrk.entities.Entity;
import app.wrk.entities.Entity_Fireball;
import app.wrk.entities.Entity_Mario;
import app.wrk.map.Map;
import app.wrk.map.blocks.Block;
import app.wrk.map.blocks.Block_Mistery_Empty;
import app.wrk.others.Camera;
import app.wrk.others.Music;
import app.wrk.others.Particle;
import app.wrk.others.Sound;
import app.wrk.animation.Timer;
import app.wrk.map.Map_Link;
import app.wrk.map.decorations.Decoration;
import app.wrk.others.Physics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author lione_000
 */
public class Wrk implements Constantes {

    private Timer timer;
    private Ctrl refCtrl;
    private Map map;
    private Entity_Mario mario;
    private ArrayList<Block> blocks;
    private Camera cam;
    private ArrayList<Entity> entities;
    private ArrayList<Particle> particles;
    private boolean inputDisabled;
    private double lastTime;
    private long sleepTime;
    private Music music;
    private Thread musicThread;

    public Wrk() {

    }

    public void startUp() {
        setup();
        loop();
    }

    private void setup() {
        setupEntities();
        setupMario();
        setupCam();
        setupMap();
        setupTimer();
        setupParticles();
        setupMusic();
    }

    private void setupParticles() {
        particles = new ArrayList<Particle>();
    }

    private void setupMusic() {
        if (music == null) {
            music = new Music();
        } else {
            try {
                stopMusic();
            } catch (Exception e) {

            }
        }

        switch (map.getMapType()) {
            case MAP_TYPE_NORMAL:
                music.setFileName(SOUND_MAIN_THEME);
                break;
            case MAP_TYPE_UNDERGROUND:
                music.setFileName(SOUND_UNDERGROUND_THEME);
                break;
            case MAP_TYPE_SKY:
                music.setFileName(SOUND_MAIN_THEME);
                break;
        }
        musicThread = new Thread(music);
        musicThread.start();
    }

    private void setupTimer() {
        sleepTime = 10;
        timer = new Timer();
    }

    private void setupEntities() {
        entities = new ArrayList<Entity>();
    }

    private void setupCam() {
        cam = new Camera();
    }

    private void setupMap() {
        blocks = new ArrayList<Block>();
        map = new Map(mario, this);
    }

    private void setupMario() {
        mario = new Entity_Mario(this);
        mario.setDir(RIGHT);
        mario.setWidth(32);
        mario.setHeight(32);
        mario.setCurrentState(MARIO_STATE_SMALL);
        inputDisabled = false;
    }

    public void changeMap() {
        setupCam();
        setupEntities();
        setupParticles();
        mario.setVelX(0);
        mario.setVelY(0);
        blocks.clear();
    }

    private void loop() {
        while (true) {
            lastTime = System.nanoTime();

            //Update Mario
            mario.update();
            if (mario.isGoingToEndCastle()) {
                goToEndCastle();
            }
            if (mario.isDead()) {
                mario.respawn();
                cam.reset();
                mario.setDead(false);
            }

            //Update Cam
            cam.update(mario, map);

            //Update Timer
            timer.update();

            //Update Blocks animation
            for (Block b : blocks) {
                b.update();
            }

            //Update Entities if on/near the player's visibility and remove fireballs
            updateNearEntities();

            //Update Particles
            for (Particle p : particles) {
                p.update();
            }

            //Remove dead entities
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).isDead()) {
                    entities.remove(i);
                }
            }

            //Remove broken blocks
            removeBrokenBlocks();

            //Remove dead particles
            for (int i = 0; i < particles.size(); i++) {
                if (particles.get(i).isDead()) {
                    particles.remove(i);
                }
            }

            //Check for falling
            checkForFalling();

            //Update for links
            updateLinks();

            //Draw
            draw();

            sleep();

            //Title
            setTitle();
        }
    }

    private void sleep() {
        lastTime = System.nanoTime();
        try {
            //Wait
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }

        if (System.nanoTime() - lastTime > 12000000) {
            sleepTime = (long) (10 - (System.nanoTime() - lastTime) / 12000000);
        }
    }

    private void checkForFalling() {
        if (mario.getY() > map.getHeight() * 32) {
            mario.setDead(true);
        }
    }

    private void setTitle() {
        refCtrl.setTitle("Map : " + map.getMapFolder() + " - X : " + mario.getX() + " - Y : " + mario.getY() + " - VelX : " + mario.getVelX() + " - VelY : " + mario.getVelY());
    }

    public void updateNearEntities() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!e.isDead()) {
                //Left Side
                if (e.getX() <= mario.getX()) {
                    if (e.getX() > cam.getxOffSet() - ENTITY_UPDATE_SCREEN_OFFSET) {
                        e.update();
                    } else {
                        if (e.getId() == FIREBALL_ID) {
                            e.killed();
                        }
                    }
                    //Right Side 
                } else if (e.getX() >= mario.getX()) {
                    if (e.getX() < cam.getxOffSet() + 1000 + ENTITY_UPDATE_SCREEN_OFFSET) {
                        e.update();
                    } else {
                        if (e.getId() == FIREBALL_ID) {
                            e.killed();
                        }
                    }
                }
            }
        }
    }

    public void removeBrokenBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).isBroken()) {
                Block blc = blocks.get(i);
                switch (blc.getId()) {
                    case LETTER_BLOCK_MISTERY_FULL_MUSCHROOM:
                    case LETTER_BLOCK_MISTERY_FULL_COIN:
                    case LETTER_BLOCK_MISTERY_FULL_FLOWER:
                        blocks.add(new Block_Mistery_Empty(this, blc.getX(), blc.getY(), false, LETTER_BLOCK_MISTERY_EMPTY, 32, 32, map.getMapType()));
                        entities.add(blc.getContent().get(0));
                        blocks.get(blocks.size() - 1).bumpUp();
                        blocks.remove(i);
                        break;
                    case LETTER_BLOCK_BRICK:
                        if (blc.getContent().size() != 0) {
                            entities.add(blc.getContent().get(0));
                            blc.getContent().remove(0);
                            Sound.playSound(SOUND_COIN);
                            if (blc.getContent().size() == 0) {
                                blocks.remove(i);
                                Sound.playSound(SOUND_BREAK_BRICK);
                                addBrickParticles(blc);
                            } else {
                                blc.setBroken(false);
                            }
                        } else {
                            blocks.remove(i);
                            Sound.playSound(SOUND_BREAK_BRICK);
                            addBrickParticles(blc);
                        }
                        break;
                }
            }
        }
    }

    public void updateLinks() {
        ArrayList<Map_Link> links = map.getLinks();
        if (!mario.isOnGround() || mario.isNoClip()) {
            return;
        }
        for (int i = 0; i < links.size(); i++) {
            if (Physics.collision(mario.getX(), mario.getY(), mario.getWidth(), mario.getHeight(), links.get(i).getSrcX(), links.get(i).getSrcY(), LINK_WIDTH, LINK_HEIGHT)) {
                if ((links.get(i).isPipeIn() && mario.isMoveDown()) || !links.get(i).isPipeIn()) {
                    if (links.get(i).isPipeIn()) {
                        usePipe(links.get(i).getSrcX());
                    }
                    int spawnX = links.get(i).getDstX();
                    int spawnY = links.get(i).getDstY();
                    boolean outOfPipe = links.get(i).isPipeOut();
                    map.setMapFolder(links.get(i).getDstName());
                    changeMap();
                    map.setup();
                    if (spawnX != 0 && spawnY != 0) {
                        setStartPoint(spawnX, spawnY);
                    }
                    if (outOfPipe) {
                        outOfPipe();
                    }
                    setupMusic();
                    break;
                }
            }

        }
    }

    public void usePipe(int x) {
        stopMusic();
        Sound.playSound(SOUND_PIPE);
        mario.setX(x - LINK_WIDTH / 2);
        mario.setAnimationStill();
        for (int i = 0; i < 50; i++) {
            mario.setY(mario.getY() + 1);
            draw();
            sleep();
        }
    }

    private void outOfPipe() {
        Sound.playSound(SOUND_PIPE);
        mario.setAnimationStill();
        mario.setY(mario.getY() + 64);
        for (int i = 0; i < 32; i++) {
            cam.update(mario, map);
            mario.setY(mario.getY() - 1);
            draw();
            sleep();
        }
    }

    public void addBrickParticles(Block b) {
        Image sprite = null;
        try {
            sprite = ImageIO.read(new File("bin/textures/particles/brick.png"));
            int posX = (int) (b.getX() + b.getWidth() / 2);
            int posY = (int) (b.getY() + b.getHeight() / 2);
            int ttl = 1000;
            int powerVel = 4;
            particles.addAll(Particle.createParticles(posX, posY, powerVel, powerVel, sprite, ttl));
        } catch (IOException ex) {
        }
    }

    public void move(KeyEvent e, boolean in) {
        if (!inputDisabled) {
            switch (e.getKeyChar()) {
                //Top
                case 'W':
                case 'w':
                    mario.setMoveTop(in);
                    //Play sound of jump
                    if (in && mario.isOnGround() && !mario.isNoClip()) {
                        Sound.playSound(SOUND_JUMP);
                    }
                    break;
                //Right
                case 'D':
                case 'd':
                    mario.setMoveRight(in);
                    break;
                //Down
                case 'S':
                case 's':
                    mario.setMoveDown(in);
                    break;
                //Left
                case 'A':
                case 'a':
                    mario.setMoveLeft(in);
                    break;
                case ' ':
                    throwFireBall();
                    break;
            }
        }

    }

    private void throwFireBall() {
        if (mario.getCurrentState() == MARIO_STATE_FIRE && mario.getFireballAmount()
                < FIREBALL_MAX_QTT_AMOUNT && Timer.getCurrentTimePassed() - mario.getFireballCooldownLastUpdate()
                > FIREBALL_COOLDOWN_TIME * 1000) {

            Sound.playSound(SOUND_FIREBALL);
            entities.add(new Entity_Fireball(this, mario, (int) mario.getX() + 10, (int) mario.getY(), mario.getDir()));
        }
    }

    public void draw() {
        refCtrl.draw(mario, blocks, cam, entities, map, particles);
    }

    public void setStartPoint(int x, int y) {
        mario.setX(x);
        mario.setY(y);
    }

    public void endFlagReached() {
        for (Entity e : entities) {
            if (e.getId() == END_FLAG_ID && e.getVelY() == 0) {
                stopMusic();
                Sound.playSound(SOUND_FLAG);
                e.setVelY(END_FLAG_FALLING_SPEED);
                break;
            }
        }
    }

    public void stopMusic() {
        try {
            musicThread.stop();
        } catch (Exception e) {
            System.out.println("Error while stopping the music");
        }
    }

    public void goToEndCastle() {
        boolean running = true;
        mario.setGoingToEndCastle(false);
        inputDisabled = true;
        while (running) {
            for (Entity e : entities) {
                if (e.getId() == END_FLAG_ID) {
                    if (e.getVelY() != 0) {
                        e.update();
                    } else {
                        running = false;
                        mario.setOnFlag(false);
                        mario.setX(mario.getX() + 64);
                    }
                }
            }
            draw();
            sleep();
        }
        running = true;
        while (running) {
            draw();
            mario.setVelX(mario.getMaxXVelocity() / 2);
            mario.move();
            mario.updateAnimation();
            for (Decoration d : map.getCastles()) {
                if (d.getFileName().equals(CASTLE_TYPE_END)) {
                    if (mario.getX() >= d.getX() + d.getWidth() / 2) {
                        Sound.playSound(SOUND_STAGE_CLEAR);
                        try {
                            Thread.sleep(5000); // Duration of sound
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Wrk.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        running = false;
                        updateLinks();
                        inputDisabled = false;
                        break;
                    }
                }
                sleep();
            }
        }

    }

    public void setNoClip(boolean noClip) {
        mario.setNoClip(noClip);
    }

    public Ctrl getRefCtrl() {
        return refCtrl;
    }

    public void setRefCtrl(Ctrl refCtrl) {
        this.refCtrl = refCtrl;
    }

    public Entity_Mario getMario() {
        return mario;
    }

    public void setMario(Entity_Mario mario) {
        this.mario = mario;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public boolean getNoClip() {
        return mario.isNoClip();
    }

}
