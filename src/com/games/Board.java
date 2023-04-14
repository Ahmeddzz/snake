package com.games;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
private SnakeGame game;
    private final int BOARD_WIDTH = 25, BOARD_HEIGHT = 25;
    private final int BLOCK_SIZE = 20;
    public final int EYE_LARGE_INSET = BLOCK_SIZE / 3;
    public final int EYE_SMALL_INSET = BLOCK_SIZE / 6;
    public final int EYE_LENGTH = BLOCK_SIZE/5;
    private Random r = new Random();
    private BlockType[][] blocks;
    private Block snakeHead;
    private Block fruit;
    private LinkedList<Block> snake;
    private int score=  0;
    private boolean rewardSnake = false;
    private int row=r.nextInt(BOARD_HEIGHT), col=r.nextInt(BOARD_WIDTH);
//    private int row = 10, col = 10;
    private int normal = 150;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime;

    private static int STATE_GAME_PLAY = 0;
    private static int STATE_GAME_PAUSE = 1;
    private static int STATE_GAME_OVER = 2;

    private int state = STATE_GAME_PAUSE;
    private boolean keyPressed = false;
    private Direction direction = Direction.Right;
    private int fruitCounter;



    public Board(SnakeGame game){
        this.game = game;
        this.blocks = new BlockType[BOARD_HEIGHT][BOARD_WIDTH];
        this.snakeHead = new Block(col,row,BlockType.SnakeHead);
        generateFruit();

        snake= new LinkedList<Block>();
        snake.add(snakeHead);
        drawSnake();
    }

    public void generateFruit(){
        fruitCounter = 0;
        for(int row = 0; row <BOARD_HEIGHT;row++){
            for(int col = 0; col <BOARD_WIDTH;col++){
                BlockType blockType = getBlock(row, col);
                if (blockType == BlockType.Fruit){
                    fruitCounter++;
                }
            }
        }
        if(fruitCounter == 0){
            fruit = new Block(r.nextInt(BOARD_WIDTH),r.nextInt(BOARD_HEIGHT),BlockType.Fruit);
            blocks[fruit.getY()][fruit.getX()] = BlockType.Fruit;
        }
    }

    private void updateScore(){
        Block head = snake.peekFirst();
        if(head.getX() == fruit.getX() &&head.getY() == fruit.getY() ){
            System.out.println(++score);
            rewardSnake = true;
        }
    }

    private boolean isCollision() {
        if (state == STATE_GAME_PLAY) {
            if(row <0 || row>= BOARD_HEIGHT || col <0 || row>= BOARD_WIDTH){
                state = STATE_GAME_OVER;
                System.out.println("Collision press R to Restart");
                return true;
            }

            for (Block i : snake) {
                for (Block j : snake) {
                    if (i != j) {
                        if (i.getX() == j.getX() && i.getY() == j.getY()) {
                            System.out.println("Collision press R to Restart");
                            state = STATE_GAME_OVER;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private void generateRandomPosition(){
        r.nextInt(BOARD_WIDTH);
        r.nextInt(BOARD_HEIGHT);
    }


    private BlockType getBlock(int x, int y){
        return blocks[y][x];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(0,0,getWidth(),getHeight());
        for(int row = 0; row <BOARD_HEIGHT;row++){
            for(int col = 0; col <BOARD_WIDTH;col++){
                BlockType blockType = getBlock(row, col);
                Block block = new Block(row, col,blockType);

                if (blockType !=null){
                    drawBlock(g,block);
                }
            }
        }

        drawBoard(g);
    }

    public void drawBoard(Graphics g){
        g.setColor(Color.darkGray);
        for(int row = 0; row <=BOARD_HEIGHT;row++){
                g.drawLine(0 ,row*BLOCK_SIZE,BLOCK_SIZE*BOARD_WIDTH,row*BLOCK_SIZE);
        }
        for(int col = 0; col <=BOARD_WIDTH;col++){
            g.drawLine(col*BLOCK_SIZE ,0,col*BLOCK_SIZE,BLOCK_SIZE*BOARD_HEIGHT);
        }
    }

    private void drawBlock(Graphics g, Block block){
    int x = block.getX()*BLOCK_SIZE ;
    int y = block.getY()*BLOCK_SIZE ;
        switch(block.getBlockType()){
            case Fruit:
                g.setColor(Color.GREEN);
                g.fillOval(x+2,y+2,BLOCK_SIZE-4,BLOCK_SIZE-4);
                break;
            case SnakeBody:
                g.setColor(Color.ORANGE);
                g.fillRect(x,y,BLOCK_SIZE,BLOCK_SIZE);
                break;
            case SnakeHead:
                g.setColor(Color.blue);
                g.fillRect(x,y,BLOCK_SIZE,BLOCK_SIZE);
                g.setColor(Color.black);
                int baseY;
                int baseX;
                switch(direction){

                    case Up:
                        baseY = y + EYE_SMALL_INSET;
                        g.drawLine(x + EYE_LARGE_INSET, baseY, x +  EYE_LARGE_INSET, baseY+EYE_LENGTH);
                        g.drawLine(x + BLOCK_SIZE - EYE_LARGE_INSET, baseY, x + BLOCK_SIZE - EYE_LARGE_INSET, baseY+EYE_LENGTH);
                        break;
                    case Down:
                        baseY = y - EYE_SMALL_INSET + BLOCK_SIZE;
                        g.drawLine(x + EYE_LARGE_INSET, baseY, x +  EYE_LARGE_INSET, baseY-EYE_LENGTH);
                        g.drawLine(x + BLOCK_SIZE - EYE_LARGE_INSET, baseY, x + BLOCK_SIZE - EYE_LARGE_INSET, baseY-EYE_LENGTH);
                        break;
                    case Left:
                        baseX = x + EYE_SMALL_INSET;
                        g.drawLine(baseX, y+EYE_LARGE_INSET, baseX +  EYE_LENGTH ,y+EYE_LARGE_INSET);
                        g.drawLine(baseX,y + BLOCK_SIZE - EYE_LARGE_INSET, baseX+ EYE_LENGTH, y + BLOCK_SIZE - EYE_LARGE_INSET);
                        break;
                    case Right:
                        baseX = x + BLOCK_SIZE - EYE_SMALL_INSET;
                        g.drawLine(baseX, y+EYE_LARGE_INSET, baseX -  EYE_LENGTH ,y+EYE_LARGE_INSET);
                        g.drawLine(baseX,y + BLOCK_SIZE - EYE_LARGE_INSET, baseX-EYE_LENGTH, y + BLOCK_SIZE - EYE_LARGE_INSET);
                        break;

                }
                break;
        }

    }
    public void updateBoard(){
        generateFruit();
        updateSnake();
        updateScore();
        isCollision();
    }


    public void updateSnake(){
        Block tail = snake.peekLast();
        Block head = snake.peekFirst();
        head.setBlockType(BlockType.SnakeBody);
        if(state == STATE_GAME_PLAY){
            if(System.currentTimeMillis() - beginTime > delayTimeForMovement){
                blocks[tail.getY()][tail.getX()] = null;
                if (!rewardSnake){
                    snake.removeLast();
                }
                rewardSnake = false;

                if (direction == Direction.Up  && row>0){
                    head.setY(row--);
                } else if( direction == Direction.Down && row<BOARD_HEIGHT){
                    head.setY(row++);
                } else if( direction == Direction.Right && col<BOARD_WIDTH){
                    head.setX(col++);
                } else if( direction == Direction.Left && col>0){
                    head.setX(col--);
                }
                snake.addFirst(new Block(col, row,BlockType.SnakeHead));
                try{
                    drawSnake();
                    blocks[row][col] = BlockType.SnakeHead;
                }
                catch (ArrayIndexOutOfBoundsException e){
                    isCollision();
                    return;
                }
                beginTime = System.currentTimeMillis();
            }

        }
    }

    private void drawSnake(){
            for(Block block : snake){
                blocks[block.getY()][block.getX()] = block.getBlockType();
            }
    }

    private void clearBoard(){
        for(int row = 0; row <BOARD_HEIGHT;row++){
            for(int col = 0; col <BOARD_WIDTH;col++){
                blocks[row][col] = null;
            }
        }
    }
    private void resetGame(){
        clearBoard();
        snake.clear();
        row=r.nextInt(BOARD_HEIGHT);
        col=r.nextInt(BOARD_WIDTH);
        this.snakeHead = new Block(col,row,BlockType.SnakeHead);
        snake.add(this.snakeHead);
        drawSnake();


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_R){
//            state = STATE_GAME_PLAY;
            state = STATE_GAME_PAUSE;
            resetGame();

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            } else if(state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            }
        }
        if (state == STATE_GAME_PLAY){
            switch (e.getKeyCode() ){
                case KeyEvent.VK_UP:
                    if (direction !=Direction.Down){
                        direction = Direction.Up ;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction !=Direction.Up) {
                        direction = Direction.Down;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction !=Direction.Left) {
                        direction = Direction.Right;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction !=Direction.Right) {
                        direction = Direction.Left;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (delayTimeForMovement == normal){
                        delayTimeForMovement = fast;
                    } else{
                        delayTimeForMovement = normal;
                    }
                    break;
            }
}


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
