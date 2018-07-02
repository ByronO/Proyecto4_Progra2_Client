package client;

import java.awt.Graphics;
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

    String username, address = "localhost";
    ArrayList<String> users = new ArrayList();
    int port = 2222;
    Boolean isConnected = false;

    Socket sock;
    BufferedReader reader;
    PrintWriter send;

    int size = 0, size2 = 0, cont = 0;
    int quantityOfShips;
    Matrix matrix;
    Ship[][] matShips;
    Graphics g;

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
    public void Disconnect() {
        try {
            textAreaChat.append("Disconnected.\n");
            sock.close();
        } catch (Exception ex) {
            textAreaChat.append("Failed to disconnect. \n");
        }
        isConnected = false;
        tf_username.setEditable(true);

    }

    public JFrameClient() {
        initComponents();
        g = jPanel1.getGraphics();
        b_connect.setEnabled(false);
        b_disconnect.setEnabled(false);
        b_send.setEnabled(false);

    }

    //--------------------------//
    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
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

        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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
                .addGap(44, 44, 44)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
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
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                        matrix+= String.valueOf(this.matrix.getMatrix()[i][j].getType());
                    }
                }
                System.err.println(matrix);
                //aparte de las posiciones se envia el tamanno de la matriz
                matrix += ";" + String.valueOf(size);
                send.println(matrix);
                send.println(username + ":has connected.:Connect");
                send.flush();
                isConnected = true;
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
        Disconnect();
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
        if (jCBSize.getSelectedItem().toString().equals("3x3")) {
            size = 3;
            quantityOfShips = 3;
            size2 = 210;
        } else {
            size = 5;
            quantityOfShips = 5;
            size2 = 350;
        }

        Matrix matrix = new Matrix(this.size);

        this.matrix = matrix;
        this.matShips = this.matrix.getMatrix();
        this.matrix.draw(g);

        jCBSize.setEnabled(false);
        b_connect.setEnabled(true);
        b_disconnect.setEnabled(true);
        b_send.setEnabled(true);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        if (size == 3) {
            if (evt.getX() < 210 && evt.getY() < 210) {
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
        } else if (size == 5) {
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
        }
    }//GEN-LAST:event_jPanel1MouseClicked

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
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCBSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtfChat;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea textAreaChat;
    private javax.swing.JTextField tf_username;
    // End of variables declaration//GEN-END:variables
}
