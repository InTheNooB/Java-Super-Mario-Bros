/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.others;

import javax.swing.JOptionPane;

/**
 *
 * @author dingl01
 */
public class ErrorHandler {

    public static void error(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", 0);
    }

    public static void warning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning", 2);
    }
}
