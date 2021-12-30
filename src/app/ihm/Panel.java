/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ihm;

import app.wrk.Constantes;
import app.wrk.others.Camera;
import app.wrk.map.blocks.Block;
import app.wrk.entities.Entity;
import app.wrk.entities.Entity_Mario;
import app.wrk.map.Map;
import app.wrk.map.Map_Link;
import app.wrk.map.blocks.Block_Moving_Platform;
import app.wrk.others.Particle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author lione_000
 */
public class Panel extends JPanel implements Constantes {

    //Debug
    private boolean debugShowHitbox;

    //Map
    private Map map;

    //Camera
    private Camera cam;

    //Particles
    private ArrayList<Particle> particles;

    //Entity
    private ArrayList<Entity> entities;
    private Entity_Mario mario;

    //Blocks
    private ArrayList<Block> blocks;

    public Panel() {
        blocks = new ArrayList<Block>();
    }

    public void paintComponent(Graphics g) {
        try {
            //Background 
            if (map.getMapType() == MAP_TYPE_UNDERGROUND) {
                g.setColor(new Color(0, 0, 0));
            } else {
                g.setColor(new Color(107, 140, 255));
            }

            g.fillRect(0, 0, 1000, 1000);

            //Background Decorations
            if (map.getMapType() != MAP_TYPE_UNDERGROUND) {
                for (int i = 0; i < map.getDecorationNbr(); i++) {
                    Image image = map.getDecorations()[i].getSprite();
                    int posX = map.getDecorations()[i].getX();
                    int posY = map.getDecorations()[i].getY();
                    g.drawImage(image, posX - cam.getxOffSet(), posY - cam.getyOffSet(), this);
                }
            }
            //Blocks
            for (Block b : blocks) {
                //Top pipe handling
                if (b.getId() != LETTER_BLOCK_PIPE_TOP) {
                    g.drawImage(b.getSpriteToDraw(), (int) (b.getX() - cam.getxOffSet()), (int) (b.getY() - cam.getyOffSet()), this);
                } else {
                    g.drawImage(b.getSpriteToDraw(), (int) (b.getX() - cam.getxOffSet() - 4), (int) (b.getY() - 1 - cam.getyOffSet()), this);
                }

                if (debugShowHitbox) {
                    g.setColor(DEBUG_HITBOX_COLOR);
                    g.drawRect((int) (b.getX() - cam.getxOffSet()), (int) (b.getY() - cam.getyOffSet()), b.getWidth(), b.getHeight());
                }
            }

            //Entities
            for (Entity e : entities) {
                g.drawImage(e.getSpriteToDraw(), (int) (e.getX() - cam.getxOffSet()), (int) (e.getY() - cam.getyOffSet()), this);

                if (debugShowHitbox) {
                    g.setColor(DEBUG_HITBOX_COLOR);
                    g.drawRect((int) (e.getX() - cam.getxOffSet()), (int) (e.getY() - cam.getyOffSet()), e.getWidth(), e.getHeight());
                }
            }

            //Mario
            if (!mario.isInvisible() || (mario.isInvisible() && mario.getInvincibleTime() < 2)) {
                g.drawImage(mario.getSpriteToDraw(), (int) (mario.getX() - cam.getxOffSet()), (int) (mario.getY() - cam.getyOffSet()), this);

                if (debugShowHitbox) {
                    g.setColor(DEBUG_HITBOX_COLOR);
                    g.drawRect((int) (mario.getX() - cam.getxOffSet()), (int) (mario.getY() - cam.getyOffSet()), mario.getWidth(), mario.getHeight());
                }
            }

            //Blocks
            for (Block b : blocks) {
                //Top pipe handling
                if (b.getId() != LETTER_BLOCK_PIPE_TOP) {
                    g.drawImage(b.getSpriteToDraw(), (int) (b.getX() - cam.getxOffSet()), (int) (b.getY() - 1 - cam.getyOffSet()), this);
                } else {
                    g.drawImage(b.getSpriteToDraw(), (int) (b.getX() - cam.getxOffSet()) - 4, (int) (b.getY() - 1 - cam.getyOffSet()), this);
                }

                if (debugShowHitbox) {
                    g.setColor(DEBUG_HITBOX_COLOR);
                    g.drawRect((int) (b.getX() - cam.getxOffSet()), (int) (b.getY() - cam.getyOffSet()), b.getWidth(), b.getHeight());

                    if (b.getContent().size() != 0) {
                        g.drawImage(b.getContent().get(0).getSpriteToDraw(), (int) (b.getX() - cam.getxOffSet()), (int) (b.getY() - 1 - cam.getyOffSet()), this);
                    }
                }
            }

            //Particles
            for (Particle p : particles) {
                Graphics2D g2 = (Graphics2D) g;
                BufferedImage image = toBufferedImage(p.getSprite());

                // The required drawing location
                int drawLocationX = p.getX() - cam.getxOffSet();
                int drawLocationY = p.getY() - cam.getyOffSet();

                // Rotation information
                double rotationRequired = Math.toRadians(p.getCurrentRotation());
                double locationX = image.getWidth() / 2;
                double locationY = image.getHeight() / 2;
                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                // Drawing the rotated image at the required drawing locations
                g2.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
            }

            //Links
            for (Map_Link l : map.getLinks()) {
                if (debugShowHitbox) {
                    g.setColor(Color.RED);
                    g.fillRect(l.getSrcX() - cam.getxOffSet(), l.getSrcY() - cam.getyOffSet(), LINK_WIDTH, LINK_HEIGHT);
                    g.setColor(Color.BLACK);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawString(l.getDstName(), l.getSrcX() - cam.getxOffSet(), l.getSrcY() - cam.getyOffSet());
                }
            }

            //Moving platforms
            if (debugShowHitbox) {
                for (Block b : blocks) {
                    if (b.getId() == OTHER_MOVING_PLATFORM) {
                        Block_Moving_Platform mp = (Block_Moving_Platform) b;
                        g.setColor(Color.red);
                        g.fillRect((int) (mp.getX() - cam.getxOffSet()), (int) mp.getStartY() - cam.getyOffSet(), mp.getWidth(), mp.getHeight());
                        g.setColor(Color.blue);
                        g.fillRect((int) (mp.getX() - cam.getxOffSet()), (int) mp.getStopY()- cam.getyOffSet(), mp.getWidth(), mp.getHeight());

                    }
                }
            }
        } catch (Exception e) {
        }
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

    public boolean isDebugShowHitbox() {
        return debugShowHitbox;
    }

    public void setDebugShowHitbox(boolean debugShowHitbox) {
        this.debugShowHitbox = debugShowHitbox;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}
