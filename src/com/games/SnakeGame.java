package com.games;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class SnakeGame extends JFrame  {
    public static final int WIDTH =600, HEIGHT = 600;
    private Board board;
    public static int FRAME_TIME = 100/1000; // time / FPS
    private Timer looper;








    public SnakeGame(){
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        board = new Board(this);

        add(board);
        addKeyListener(board);
        setLocationRelativeTo(null); // center window
        setVisible(true);

    }






    public void startGame() {


        looper = new Timer(FRAME_TIME , new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.updateBoard();
                board.repaint();
            }
        });

        looper.start();
    }


    public static void main(String[] args) {
        SnakeGame game = new SnakeGame();
        game.startGame();

    }
}
