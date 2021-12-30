/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.others;

import app.wrk.Constantes;
import java.awt.Rectangle;

/**
 *
 * @author lione_000
 */
public class Physics implements Constantes {

    public static boolean collision(float x1, float y1, int w1, int h1, float x2, float y2, int w2, int h2) {
        Rectangle rect1 = new Rectangle((int) x1, (int) y1, w1, h1);
        Rectangle rect2 = new Rectangle((int) x2, (int) y2, w2, h2);
        return rect1.intersects(rect2);
    }
}
