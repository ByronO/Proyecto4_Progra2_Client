/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



/**
 *
 * @author Byron Ortiz Rojas
 */
public class Ship {
    
    private BufferedImage image;
    private int x, y;
    private int type;
    int health;

    public Ship( int type) {
        this.type = type;
    }

    public Ship(BufferedImage image, int x, int y, int type, int health) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.type = type;
        this.health = health;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(int type) throws IOException {
         switch (type) {
                case 0:
                    this.image = null;
                    break;
                case 1:
                    this.image = ImageIO.read(getClass().getResourceAsStream("/assets/nave.png"));
                    break;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
       
            this.type = type;
        
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    
}
