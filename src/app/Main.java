/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.ctrl.Ctrl;
import app.ihm.Frame;
import app.wrk.Wrk;

/**
 *
 * @author lione_000
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Ctrl ctrl = new Ctrl();
        Wrk wrk = new Wrk();
        Frame frame = new Frame();

        ctrl.setRefFrame(frame);
        ctrl.setRefWrk(wrk);

        wrk.setRefCtrl(ctrl);

        frame.setRefCtrl(ctrl);

        ctrl.startUp();
    }

}
