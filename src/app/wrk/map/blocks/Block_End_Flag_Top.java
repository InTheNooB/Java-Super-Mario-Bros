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
public class Block_End_Flag_Top extends Block {

    private boolean usable;

    public Block_End_Flag_Top(Wrk refWrk, boolean usable, int x, int y, boolean breakable, char id, int width, int height, int mapType) {
        super(refWrk, x, y, breakable, id, width, height, true, mapType);
        this.usable = usable;
    }

    @Override
    public void setupSprites() {
        animation = new AnimationSet();
        animation.setCurrentCategorie(animation.importAnimation("block_end_flag_top"));
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

}
