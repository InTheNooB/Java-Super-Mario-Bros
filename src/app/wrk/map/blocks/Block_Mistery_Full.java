/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map.blocks;

import app.wrk.Wrk;
import app.wrk.entities.Entity;
import app.wrk.animation.AnimationSet;
import java.util.ArrayList;

/**
 *
 * @author lione_000
 */
public class Block_Mistery_Full extends Block {


    public Block_Mistery_Full(Wrk refWrk, int x, int y, boolean breakable, char id, int width, int height, Entity content, int mapType) {
        super(refWrk, x, y, breakable, id, width, height, mapType);
        this.content = new ArrayList();
        this.content.add(content);
    }

    @Override
    public void kill() {
        broken = true;
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_full"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_full_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_full_sky"));
                break;
        }
    }

}
