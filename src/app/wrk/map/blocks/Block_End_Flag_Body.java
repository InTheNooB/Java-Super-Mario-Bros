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
 * @author dingl01
 */
public class Block_End_Flag_Body extends Block {

    public Block_End_Flag_Body(Wrk refWrk, int x, int y, boolean breakable, char id, int width, int height, int mapType) {
        super(refWrk, x, y, breakable, id, width, height, true, mapType);
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        animation.setCurrentCategorie(animation.importAnimation("block_end_flag_body"));
    }

    
}
