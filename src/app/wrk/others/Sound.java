/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.others;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author lione_000
 */
public class Sound {

    public static synchronized void playSound(String fileName) {

        new Thread(new Runnable() {

            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                AudioInputStream audioInputStream = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("bin/sounds/game/" + fileName + ".wav"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AudioFormat audioFormat = audioInputStream.getFormat();
                SourceDataLine line = null;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                try {
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(audioFormat);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                line.start();
                int nBytesRead = 0;
                byte[] abData = new byte[128000];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (nBytesRead >= 0) {
                        int nBytesWritten = line.write(abData, 0, nBytesRead);
                    }
                }
                line.drain();
                line.close();
            }

        }).start();
    }

}
