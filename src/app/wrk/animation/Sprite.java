/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.animation;

import java.awt.Image;
import java.io.Serializable;

/**
 *
 * @author lione_000
 */
public class Sprite implements Serializable {

    private transient Image sprite;
    private String spritePath;
    private float cooldown;
    private int id;

    public Sprite(Image sprite, float cooldown, int id, String spritePath) {
        this.sprite = sprite;
        this.cooldown = cooldown;
        this.id = id;
        this.spritePath = spritePath;
    }

    public Sprite(Image sprite, int id, String spritePath) {
        this.sprite = sprite;
        this.id = id;
        this.cooldown = -1;
        this.spritePath = spritePath;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

}
