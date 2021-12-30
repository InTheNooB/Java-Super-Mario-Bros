/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map;

/**
 *
 * @author dingl01
 */
public class Map_Link {

    private String srcName;
    private String dstName;
    private int srcX;
    private int srcY;
    private int dstX;
    private int dstY;
    private boolean pipeIn;
    private boolean pipeOut;

    public Map_Link(String srcName, String dstName, int srcX, int srcY, int dstX, int dstY, boolean pipeIn, boolean pipeOut) {
        this.srcName = srcName;
        this.dstName = dstName;
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
        this.pipeIn = pipeIn;
        this.pipeOut = pipeOut;
    }

    public Map_Link(String srcName, String dstName, int srcX, int srcY, boolean pipeIn, boolean pipeOut) {
        this.srcName = srcName;
        this.dstName = dstName;
        this.srcX = srcX;
        this.srcY = srcY;
        this.pipeIn = pipeIn;
        this.pipeOut = pipeOut;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDstName() {
        return dstName;
    }

    public void setDstName(String dstName) {
        this.dstName = dstName;
    }

    public int getSrcX() {
        return srcX;
    }

    public void setSrcX(int srcX) {
        this.srcX = srcX;
    }

    public int getSrcY() {
        return srcY;
    }

    public void setSrcY(int srcY) {
        this.srcY = srcY;
    }

    public int getDstX() {
        return dstX;
    }

    public void setDstX(int dstX) {
        this.dstX = dstX;
    }

    public int getDstY() {
        return dstY;
    }

    public void setDstY(int dstY) {
        this.dstY = dstY;
    }

    public boolean isPipeIn() {
        return pipeIn;
    }

    public void setPipeIn(boolean pipeIn) {
        this.pipeIn = pipeIn;
    }

    public boolean isPipeOut() {
        return pipeOut;
    }

    public void setPipeOut(boolean pipeOut) {
        this.pipeOut = pipeOut;
    }

}
