/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map.blocks;

import app.wrk.Wrk;
import app.wrk.animation.AnimationSet;

/**
 *
 * @author lione_000
 */
public class Block_Mistery_Empty extends Block {

    public Block_Mistery_Empty(Wrk refWrk, float x, float y, boolean breakable, char id, int width, int height, int mapType) {
        super(refWrk, x, y, breakable, id, width, height, mapType);
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        switch (mapType) {
            case MAP_TYPE_NORMAL:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_empty"));
                break;
            case MAP_TYPE_UNDERGROUND:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_empty_underground"));
                break;
            case MAP_TYPE_SKY:
                animation.setCurrentCategorie(animation.importAnimation("block_mistery_empty_sky"));
                break;
        }
    }

}
