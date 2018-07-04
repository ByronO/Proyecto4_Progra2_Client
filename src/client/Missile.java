/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Byron Ortiz Rojas
 */
public class Missile extends Thread {

    private int x, y;
    private BufferedImage image;
    Graphics g;
    private boolean exe = true;
    int type;
    int yDes;

    public Missile(int x, int y, int type ,int yDes) {
        try {
            this.type = type;
            this.yDes = yDes;
            this.x = x;
            this.y = y;
            this.image = ImageIO.read(getClass().getResourceAsStream("/assets/misil.png"));
        } catch (IOException ex) {
            Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void draw(Graphics g) {
        this.g = g;
        this.g.drawImage(getImage(), getX(), getY(), 15, 30, null);
    }

    public boolean isExe() {
        return exe;
    }

    @Override
    public void run() {
        try {
            if (type == 0) {
                while (this.y + 30 != 0) {
                    super.sleep(10);
                    this.y--;
                    if (this.y + 29 == 0) {
                        this.exe = false;
                    }
                }
            }else {
                while (this.y - 30 != yDes) {
                    super.sleep(10);
                    this.y++;
                    if (this.y - 29 == yDes) {
                        this.exe = false;
                    }
                }
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
