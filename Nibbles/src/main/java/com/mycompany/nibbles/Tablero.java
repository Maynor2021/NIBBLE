/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nibbles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author Carlos
 */
public class Tablero extends JPanel implements ActionListener {

    private final int ANCHO_TABLERO = 600;
    private final int ALTURA_TABLERO = 600;
    private final int TAM_PUNTO = 10;
    private final int RAND_POS = 59;
    private final int RETRASO = 140;
    private final int TODOS_PUNTOS = 3600;

    private final int x[] = new int[TODOS_PUNTOS];
    private final int y[] = new int[TODOS_PUNTOS];

    private Image punto;
    private Image cabeza;
    private Image manzana;

    private Timer timer;
    private int puntos;
    private int manzana_x;
    private int manzana_y;

    private boolean enJuego = true;
    private boolean dirDerecha = true;
    private boolean dirArriba = false;
    private boolean dirIzquierda = false;
    private boolean dirAbajo = false;
     

    // Botón para reiniciar el juego
    private JButton btnReiniciar;
    private int puntuacion = 0;  // Variable para almacenar el puntaje


    public Tablero() {
        inicializarTablero();
    }

    public void inicializarTablero() {
        addKeyListener(new AdaptadorTeclado());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(ANCHO_TABLERO, ALTURA_TABLERO));
        cargarImagenes();
        inicializarJuego();

        // Inicializar botón de reinicio
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJuego();
                requestFocusInWindow();  //investigue para que  Solicite el foco para que las teclas vuelvan a funcionar
            }                              //porque despues de reiniciar las teclas dejan de funcior 
        });
        add(btnReiniciar);
    }
    private void reiniciarJuego(){ //metodo para reiniciar el juego , pero no lo hice reiniciar al finalizar , solo mientras esta en juego reinicia
        posicionarManzana();
        timer.restart();
        // Reiniciar puntuación
        puntuacion = 0;
        
    }

        private void cargarImagenes(){
        ImageIcon iiPunto = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/dot.png");
        punto = iiPunto.getImage();
        
        ImageIcon iiCabeza = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/head.png");
        cabeza = iiCabeza.getImage();
        
        ImageIcon iiManzana = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/apple.png");
        manzana = iiManzana.getImage();
    }
    
    private void inicializarJuego(){
        puntos = 3;
        for(int i=0; i<puntos; i++){
            x[i] = 50 -i * 10;
            y[i] = 50;
        }
        
        posicionarManzana();
        
        timer = new Timer(RETRASO, this);
        timer.start();
    }
    
    private void posicionarManzana(){
        int r = (int)(Math.random() * RAND_POS);
        manzana_x = ((r * TAM_PUNTO));
        
        r = (int)(Math.random() * RAND_POS);
        manzana_y = ((r * TAM_PUNTO));
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        pintar(g);
    }
    
   private void pintar(Graphics g) {
        if (enJuego) {
            g.drawImage(manzana, manzana_x, manzana_y, this);

            for (int i = 0; i < puntos; i++) {
                if (i == 0) {
                    g.drawImage(cabeza, x[i], y[i], this);
                } else {
                    g.drawImage(punto, x[i], y[i], this);
                }
            }

            // Mostrar puntuación en la esquina superior derecha
            String score = "Puntuación: " + puntuacion;
            g.setColor(Color.white);
            g.drawString(score, ANCHO_TABLERO - 100, 20);

            Toolkit.getDefaultToolkit().sync();

        } else {
            finJuego(g);
        }
    }
    private void finJuego(Graphics g){
        String msj = "Fin de Juego";
        Font peq = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(peq);
        
        g.setColor(Color.white);
        g.setFont(peq);
        g.drawString(msj, (ANCHO_TABLERO - metrics.stringWidth(msj))/2, ALTURA_TABLERO/2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(enJuego){
            verificarManzana();
            verificarColision();
            Mover();
        }
        
        repaint();
    }
    
    private void verificarManzana() {
        if ((x[0] == manzana_x) && (y[0] == manzana_y)) {
            puntos++;
            posicionarManzana();
            puntuacion++;  // Incrementar puntuación
        }
    }
    
    private void verificarColision(){
    //Cabeza colisiono con cola
   
     for(int i = puntos - 1; i > 0; i--){  //  cambie la condición i > 0 a i > 1 para incluir todo el cuerpo
        if((x[0] == x[i]) && (y[0] == y[i])){
            enJuego = false;  // Si la cabeza colisiona, el juego termina
        }
    }
    
    // Colisión con los bordes del tablero
    if(y[0] >= ALTURA_TABLERO){
        y[0] = 0;  // si la cabeza sube al borde  superior sale por bajo
    }
    
    if(y[0] < 0){
        y[0] = ALTURA_TABLERO - TAM_PUNTO;  // Si la cabeza llega al borde superior, aparece en el borde inferior
    }
    
    if(x[0] >= ANCHO_TABLERO){
        x[0] = 0;  
    }
    
    if(x[0] < 0){
        x[0] = ANCHO_TABLERO - TAM_PUNTO;  // 
    }
    
    if(!enJuego){
        timer.stop();
    }
}

private void Mover(){
    
    //mover los puntos verdes siguiendo la ultima ubicacion del punto rojo
    for(int i = puntos; i > 0; i--){
        x[i] = x[i-1];
        y[i] = y[i-1];
    }
    
    if(dirIzquierda){
        x[0] -= TAM_PUNTO;
    }
    
    if(dirDerecha){
        x[0] += TAM_PUNTO;
    }
    
    if(dirArriba){
        y[0] -= TAM_PUNTO;
    }
    
    if(dirAbajo){
        y[0] += TAM_PUNTO;
    }
}
  
    
    private class AdaptadorTeclado extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e){
            int tecla = e.getKeyCode();
            
            if((tecla == KeyEvent.VK_LEFT) && (!dirDerecha)){
                dirIzquierda = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_RIGHT) && (!dirIzquierda)){
                dirDerecha = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_UP) && (!dirAbajo)){
                dirArriba = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
            
            if((tecla == KeyEvent.VK_DOWN) && (!dirArriba)){
                dirAbajo = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
        }        
    }
}
