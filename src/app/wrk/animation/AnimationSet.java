/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.animation;

import static app.wrk.Constantes.ANIMATION_FILE_EXT;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author dingl01
 */
public class AnimationSet implements Serializable {

    private ArrayList<Animation> animations;
    private int currentCategorie;

    public AnimationSet() {
        animations = new ArrayList();
    }

    public void addCategorie(float cooldown, int categorie) {
        animations.add(new Animation(categorie, cooldown));
    }

    public void addCategorie(int categorie) {
        animations.add(new Animation(categorie));
    }

    public void fillCategorie(Animation animation) {
        int categorieID = getMatchingCategorieID(animation.getCategorie());
        if (animation.getCooldown() != -1 && animation.isSwitching()) {
            animations.get(categorieID).setSwitching(true);
            animations.get(categorieID).setCooldown(animation.getCooldown());
        } else {
            animations.get(categorieID).setSwitching(false);
            animations.get(categorieID).setCooldown(-1);
        }

        for (Sprite s : animation.getSprites()) {
            if (s.getCooldown() != -1) {
                animations.get(categorieID).addSprite(s.getSprite(), s.getCooldown(), s.getId(), s.getSpritePath());
            } else {
                animations.get(categorieID).addSprite(s.getSprite(), s.getId(), s.getSpritePath());
            }
        }
        animations.get(categorieID).addSprites();

    }

    public void addCategorie(Animation animation) {
        if (animation.getCooldown() != -1) {
            animations.add(new Animation(animation.getCategorie(), animation.getCooldown()));
        } else {
            animations.add(new Animation(animation.getCategorie()));
        }

        int categorieID = getMatchingCategorieID(animation.getCategorie());

        for (Sprite s : animation.getSprites()) {
            if (s.getCooldown() != -1) {
                animations.get(categorieID).addSprite(s.getSprite(), s.getCooldown(), s.getId(), s.getSpritePath());
            } else {
                animations.get(categorieID).addSprite(s.getSprite(), s.getId(), s.getSpritePath());
            }
        }
        animations.get(categorieID).addSprites();

    }

    public boolean addSprite(File file, int categorie, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, float cooldown, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), cooldown, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, int sprite, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), spriteID, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, float cooldown, int sprite, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), cooldown, spriteID, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public void updateAnimationSettings(int categorie, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setCooldown(cooldown);
        animations.get(categorieID).setSwitching(true);
    }

    public void updateAnimationSettings(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setCooldown(-1);
        animations.get(categorieID).setSwitching(false);
    }

    public void updateAnimation() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        animations.get(categorieID).update();
    }

    public void setCurrentCategorie(int currentCategorie) {
        this.currentCategorie = currentCategorie;
    }

    public void setCurrentSprite(int currentSprite) {
        int categorieID = getMatchingCategorieID(currentCategorie);
        animations.get(categorieID).setCurrentSprite(currentSprite);
    }

    public Image getSpriteToDraw() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        return animations.get(categorieID).getSpriteToDraw();
    }

    public boolean isEmpty() {
        return animations.size() == 0;
    }

    public boolean isSwitching(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID).isSwitching();
    }

    public float getCooldown(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        // cateid = -1
        return animations.get(categorieID).getCooldown();
    }

    public float getCooldown(int categorie, int sprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        if (spriteID == -1) {
            return animations.get(categorieID).getCooldown();
        }
        return animations.get(categorieID).getSprites().get(spriteID).getCooldown();
    }

    public boolean categorieExist(int i) {
        return animations.size() >= i;
    }

    public void removeSprite(int categorie, int chosenSprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);
        if (spriteID != -1) {
            animations.get(categorieID).getSprites().remove(spriteID);
        }
    }

    public int getCurrentCategorie() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        return categorieID;
    }

    public void spriteUpdate(int categorie, int chosenSprite, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);

        if (spriteID != -1) {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(cooldown);
        }
        animations.get(categorieID).setSwitching(true);
    }

    public void categorieUpdate(int categorie, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        float oldCooldown = animations.get(categorieID).getCooldown();
        animations.get(categorieID).setSwitching(true);
        animations.get(categorieID).setCooldown(cooldown);
        for (Sprite s : animations.get(categorieID).getSprites()) {
            if (s.getCooldown() == oldCooldown) {
                s.setCooldown(cooldown);
            }
        }
    }

    public void categorieUpdate(int categorie, boolean switching) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setSwitching(switching);
    }

    public boolean isEmpty(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID).getSprites().size() == 0;
    }

    public void setCategorieSwitching(int categorie, boolean state) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setSwitching(state);
        if (!state) {
            for (Sprite s : animations.get(categorieID).getSprites()) {
                s.setCooldown(-1);
            }
        }
    }

    public void setSpriteCooldown(int categorie, int chosenSprite, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);
        if (cooldown != animations.get(categorieID).getCooldown()) {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(cooldown);
        } else {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(-1);
        }
    }

    private int getMatchingCategorieID(int categorie) {
        int categorieID = -1;
        for (int i = 0; i < animations.size(); i++) {
            if (animations.get(i).getCategorie() == categorie) {
                categorieID = i;
                break;
            }
        }
        return categorieID;
    }

    public Animation getAnimation(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID);
    }

    private int getMatchingSpriteID(int categorie, int chosenSprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = -1;
        for (int i = 0; i < animations.get(categorieID).getSprites().size(); i++) {
            if (animations.get(categorieID).getSprites().get(i).getId() == chosenSprite) {
                spriteID = i;
                break;
            }
        }
        return spriteID;
    }

    public int importAnimation(String animationName) {
        int categorie = -1;
        try {

            ObjectInputStream in = null;
            Animation importedAnimation = null;
            // Deserialization
            // Reading the object from a file
            in = new ObjectInputStream(new FileInputStream(new File("bin/animations/" + animationName + ANIMATION_FILE_EXT)));

            // Method for deserialization of object 
            importedAnimation = (Animation) in.readObject();
            in.close();
            categorie = importedAnimation.getCategorie();

            addCategorie(importedAnimation);

        } catch (Exception ex) {
        }
        return categorie;
    }

    public int getCategoriesAmount() {
        return animations.size();
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(ArrayList<Animation> animations) {
        this.animations = animations;
    }

}
