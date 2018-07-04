package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Byron Ortiz Rojas
 */
public class JFrameClient extends javax.swing.JFrame {

    String username;
    String address;
    ArrayList<String> users = new ArrayList();
    int port = 5000;
    Boolean isConnected = false;

    Socket sock;
    BufferedReader reader;
    PrintWriter send;

    int size = 0, size2 = 0, cont = 0;
    int quantityOfShips;
    Matrix matrix;
    Ship[][] matShips;
    Graphics g, g2;
    int xAttack = 1000, yAttack = 1000;
    int mother = 0;
    int xMis, yMis;

    //--------------------------//
    public void ListenThread() {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    //--------------------------//
    public void userAdd(String data) {
        users.add(data);
    }

    //--------------------------//
    public void userRemove(String data) {
        textAreaChat.append(data + " is now offline.\n");
    }

    //--------------------------//
    public void writeUsers() {
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);
        for (String token : tempList) {
            //users.append(token + "\n");
        }
    }

    //--------------------------//
    public void sendDisconnect() {
        String bye = (username + ": :Disconnect");
        try {
            send.println(bye);
            send.flush();
        } catch (Exception e) {
            textAreaChat.append("Could not send Disconnect message.\n");
        }
    }

    //--------------------------//
    public void disconnect() {
        try {
            textAreaChat.append("Disconnected.\n");
            sock.close();
        } catch (Exception ex) {
            textAreaChat.append("Failed to disconnect. \n");
        }
        isConnected = false;
        tf_username.setEditable(true);

    }

    // Metodo para atacar que recibe las posiciones recibidas del servidor y compara a ver si se le acerto a una nave
    public void attack(String position) {
        String pos[] = position.split(",");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);

        if (size == 3) {
            int yDestino = y * 116;
            Missile missile = new Missile((x * 116) + 50, 0, 1, yDestino);
            missile.start();

            while (missile.isExe()) {
                missile.draw(g);

            }
            jPanel1.repaint(0, 0, 351, 351);
            if (this.matrix.getMatrix()[y][x].getType() == 1) {
                this.matrix.getMatrix()[y][x].setHealth(0);
                this.matrix.draw(g);
                send.println(username + ":" + "aaa" + ":" + "right");
                send.flush(); // flushes the buffer
            } else if (this.matrix.getMatrix()[y][x].getType() == 2) {
                this.matrix.getMatrix()[y][x].setHealth(this.matrix.getMatrix()[y][x].getHealth() - 1);
                this.matrix.draw(g);
                send.println(username + ":" + "aaa" + ":" + "right");
                send.flush(); // flushes the buffer
            }

            this.matrix.draw(g);
        } else {
            int yDestino = y * 70;
            Missile missile = new Missile((x * 70) + 30, 0, 1, yDestino);
            missile.start();

            while (missile.isExe()) {
                missile.draw(g);

            }
            jPanel1.repaint(0, 0, 351, 351);
            if (this.matrix.getMatrix()[y][x].getType() == 1) {
                this.matrix.getMatrix()[y][x].setHealth(0);
                this.matrix.draw(g);
                send.println(username + ":" + "aaa" + ":" + "right");
                send.flush(); // flushes the buffer
            } else if (this.matrix.getMatrix()[y][x].getType() == 2) {
                this.matrix.getMatrix()[y][x].setHealth(this.matrix.getMatrix()[y][x].getHealth() - 1);
                this.matrix.draw(g);
                send.println(username + ":" + "aaa" + ":" + "right");
                send.flush(); // flushes the buffer
            }
            this.matrix.draw(g);
        }
        if (this.matrix.getMatrix()[y][x].getType() == 2 && this.matrix.getMatrix()[y][x].getHealth() == 0) {
            send.println(username + ":" + "loser" + ":" + "END");
            send.flush(); // flushes the buffer
        }

    }

    public void end(String userLost) {
        if (userLost.equals(username)) {
            textAreaChat.append("---You lose---" + "\n");
            textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
        } else {
            textAreaChat.append("---Yow win---" + "\n");
            textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
        }
        btnAttack.setEnabled(false);
    }

    public void endTurn(String userEnd) {
        if (userEnd.equals(username)) {
            btnAttack.setEnabled(false);
        } else {
            btnAttack.setEnabled(true);
        }
    }

    public void info(String userEnd) {
        if (userEnd.equals(username)) {
            textAreaChat.append("---Your ship was impacted---" + "\n");
        } else {
            textAreaChat.append("---You hit a ship---" + "\n");
        }
    }

    public JFrameClient() {

        initComponents();
        address = "localhost";
        g = jPanel1.getGraphics();
        g2 = jPanel2.getGraphics();
        g.setColor(Color.white);
        g2.setColor(Color.white);
        b_connect.setEnabled(false);
        b_disconnect.setEnabled(false);
        b_send.setEnabled(false);
        btnAttack.setEnabled(false);

    }

    //--------------------------//
    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat", attack = "Attack", end = "END", turn = "fin", info = "right";

            try {
                while ((stream = reader.readLine()) != null) {
                    data = stream.split(":");

                    if (data[2].equals(chat)) {
                        textAreaChat.append(data[0] + ": " + data[1] + "\n");
                        textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
                    } else if (data[2].equals(connect)) {
                        textAreaChat.removeAll();
                        userAdd(data[0]);
                    } else if (data[2].equals(disconnect)) {
                        userRemove(data[0]);
                    } else if (data[2].equals(attack)) {
                        if (!data[0].equals(username)) {
                            attack(data[1]);
                        }
                    } else if (data[2].equals(end)) {
                        end(data[0]);
                    } else if (data[2].equals(turn)) {
                        endTurn(data[0]);
                    } else if (data[2].equals(info)) {
                        info(data[0]);
                    } else if (data[2].equals(done)) {
                        //users.setText("");
                        writeUsers();
                        users.clear();
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    //--------------------------//
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        b_disconnect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaChat = new javax.swing.JTextArea();
        jtfChat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jCBSize = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbl2 = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        btnAttack = new javax.swing.JButton();
        lblAd = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Player frame");
        setName("client"); // NOI18N
        setResizable(false);

        lb_username.setText("Username :");

        tf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_usernameActionPerformed(evt);
            }
        });

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_disconnectActionPerformed(evt);
            }
        });

        textAreaChat.setColumns(20);
        textAreaChat.setRows(5);
        jScrollPane1.setViewportView(textAreaChat);

        b_send.setText("SEND");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });

        jLabel1.setText("Size of the matrix");

        jCBSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3x3", "5x5" }));

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 0, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(351, 351));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(51, 0, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(186, 186));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        lbl2.setText(" Selected position:");

        lbl.setText("Select the position to attack");

        lblPosition.setText("   ");

        btnAttack.setText("Attack");
        btnAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttackActionPerformed(evt);
            }
        });

        lblAd.setForeground(new java.awt.Color(204, 0, 0));
        lblAd.setText("   ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(b_send, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jtfChat, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_username, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCBSize, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_connect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_disconnect)))
                .addGap(50, 50, 50)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblAd, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAttack)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_username)
                            .addComponent(tf_username)
                            .addComponent(b_connect)
                            .addComponent(b_disconnect))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jCBSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtfChat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl)
                                .addGap(25, 25, 25)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl2)
                                    .addComponent(lblPosition))
                                .addGap(18, 18, 18)
                                .addComponent(btnAttack, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblAd)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_send)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_usernameActionPerformed

    }//GEN-LAST:event_tf_usernameActionPerformed

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
        if (isConnected == false && cont == quantityOfShips) {
            username = tf_username.getText();
            tf_username.setEditable(false);

            try {
                sock = new Socket(address, port);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                send = new PrintWriter(sock.getOutputStream());
                //guarda el tipo de cada posicion de la matriz en un string que se envia al servidor
                String matrix = "";
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        matrix += String.valueOf(this.matrix.getMatrix()[i][j].getType());
                    }
                }
                System.err.println(matrix);
                //aparte de las posiciones se envia el tamanno de la matriz
                matrix += ";" + String.valueOf(size);
                send.println(matrix);
                send.println(username + ":has connected.:Connect");
                send.flush();
                isConnected = true;

                btnAttack.setEnabled(true);

            } catch (Exception ex) {
                textAreaChat.append("Cannot Connect! Try Again. \n");
                tf_username.setEditable(true);
            }

            ListenThread();

        } else if (isConnected == true) {
            textAreaChat.append("You are already connected. \n");
        }
    }//GEN-LAST:event_b_connectActionPerformed

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_disconnectActionPerformed
        sendDisconnect();
        disconnect();
    }//GEN-LAST:event_b_disconnectActionPerformed

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
        String nothing = "";
        if ((jtfChat.getText()).equals(nothing)) {
            jtfChat.setText("");
            jtfChat.requestFocus();
        } else {
            try {
                send.println(username + ":" + jtfChat.getText() + ":" + "Chat");
                send.flush(); // flushes the buffer
            } catch (Exception ex) {
                textAreaChat.append("Message was not sent. \n");
            }
            jtfChat.setText("");
            jtfChat.requestFocus();
        }

        jtfChat.setText("");
        jtfChat.requestFocus();
    }//GEN-LAST:event_b_sendActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int size3 = 37;
        if (jCBSize.getSelectedItem().toString().equals("3x3")) {
            size = 3;
            quantityOfShips = 3;
            size2 = 350;
            size3 = 62;
        } else {
            size = 5;
            quantityOfShips = 5;
            size2 = 350;
        }

        Matrix matrix = new Matrix(this.size);

        this.matrix = matrix;
        this.matShips = this.matrix.getMatrix();
        this.matrix.draw(g);

        int x = 0, y = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                g2.drawRect(x, y, size3, size3);
                x += size3;
            }
            y += size3;
            x = 0;

        }

        jCBSize.setEnabled(false);
        b_connect.setEnabled(true);
        b_disconnect.setEnabled(true);
        b_send.setEnabled(true);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        if (size == 3) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                if (evt.getX() < 350 && evt.getY() < 350) {
                    try {
                        if (cont <= quantityOfShips - 1) {
                            if (this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].getType() == 0) {
                                this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].setType(1);
                                this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].setImage(1);
                                this.matrix.setMatrix(matShips);
                                this.matrix.draw(g);
                                cont++;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(JFrameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (evt.getX() < 350 && evt.getY() < 350) {
                    try {
                        if (cont <= quantityOfShips - 1 && mother == 0) {
                            if (this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].getType() == 0) {
                                this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].setType(2);
                                this.matShips[(evt.getY() / 116)][(evt.getX() / 116)].setImage(2);
                                this.yMis = (evt.getY() / 116) * 116 - 20;
                                this.xMis = (evt.getX() / 116) * 116 + 53;
                                this.matrix.setMatrix(matShips);
                                this.matrix.draw(g);
                                cont++;
                                mother++;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(JFrameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (size == 5) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                if (evt.getX() < 350 && evt.getY() < 350) {
                    try {
                        if (cont <= quantityOfShips - 1) {
                            if (this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].getType() == 0) {
                                this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].setType(1);
                                this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].setImage(1);
                                this.matrix.setMatrix(matShips);
                                this.matrix.draw(g);
                                cont++;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(JFrameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (evt.getX() < 350 && evt.getY() < 350) {
                    try {
                        if (cont <= quantityOfShips - 1 && mother == 0) {
                            if (this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].getType() == 0) {
                                this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].setType(2);
                                this.matShips[(evt.getY() / 70)][(evt.getX() / 70)].setImage(2);
                                this.yMis = (evt.getY() / 70) * 70 - 20;
                                this.xMis = (evt.getX() / 70) * 70 + 28;
                                this.matrix.setMatrix(matShips);
                                this.matrix.draw(g);
                                cont++;
                                mother++;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(JFrameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        if (size == 3) {
            this.xAttack = evt.getX() / 62;
            this.yAttack = evt.getY() / 62;
            this.lblPosition.setText(String.valueOf(xAttack + ", " + yAttack));

        } else if (size == 5) {
            this.xAttack = evt.getX() / 37;
            this.yAttack = evt.getY() / 37;
            this.lblPosition.setText(String.valueOf(xAttack + ", " + yAttack));
        }

    }//GEN-LAST:event_jPanel2MouseClicked

    private void btnAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttackActionPerformed
        if (xAttack == 1000) {
            lblAd.setText("Select a position");
        } else {
            try {
                Missile missile = new Missile(xMis, yMis, 0, 0);
                missile.start();

                while (missile.isExe()) {
                    missile.draw(g);
                    jPanel1.repaint(missile.getX(), missile.getY(), 15, 30);
                }
//                this.g = jPanel1.getGraphics();
                this.matrix.draw(g);
                send.println(username + ":" + String.valueOf(xAttack + "," + yAttack) + ":" + "Attack");
                send.flush(); // flushes the buffer
                send.println(username + ":" + "End turn" + ":" + "fin");
                send.flush(); // flushes the buffer
            } catch (Exception ex) {
                textAreaChat.append("Message was not sent. \n");
            }
            jtfChat.setText("");
            jtfChat.requestFocus();
        }
    }//GEN-LAST:event_btnAttackActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFrameClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;
    private javax.swing.JButton btnAttack;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCBSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtfChat;
    private javax.swing.JLabel lb_username;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lblAd;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JTextArea textAreaChat;
    private javax.swing.JTextField tf_username;
    // End of variables declaration//GEN-END:variables
}
