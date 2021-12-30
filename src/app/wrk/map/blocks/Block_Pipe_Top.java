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
public class Block_Pipe_Top extends Block {

    public Block_Pipe_Top(Wrk refWrk, int x, int y, boolean breakable, char id, int width, int height) {
        super(refWrk, x, y, breakable, id, width, height, MAP_TYPE_NORMAL);
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        animation.setCurrentCategorie(animation.importAnimation("block_pipe_top"));
    }

}
