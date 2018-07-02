/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Byron Ortiz Rojas
 */
public class Matrix {
    
    int size;
    Ship [][] matrix;

    public Matrix(int size) {
        this.size = size;
        matrix = new Ship[size][size];
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    this.matrix[i][j] = new Ship(0);
                    this.matrix[i][j].setImage(0);
                    this.matrix[i][j].setX(i*70);
                    this.matrix[i][j].setY(j*70);
                } catch (IOException ex) {
                    Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }

    public Matrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.matrix[i][j]= new Ship(0);
                this.matrix[i][j].setX(i*70);
                this.matrix[i][j].setY(j*70);
            }
        }
    }

    public int getSizeM() {
        return size;
    }

    public void setSizeM(int size) {
        this.size = size;
    }

    public Ship[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Ship[][] matrix) {
        this.matrix = matrix;
    }
    
    public void draw(Graphics g2){
        Graphics2D g2D = (Graphics2D) g2;
        int x = 0, y = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(this.matrix[i][j].getType() == 0){
                    g2D.drawRect(x, y, 70 , 70);
                    x+=70;
                }else {
                    g2D.drawImage(this.matrix[i][j].getImage(), x, y, 70, 70, null);
                    x+=70;
                }
            }
            y+=70;
            x= 0;
        }
    }
    
}
