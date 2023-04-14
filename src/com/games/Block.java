package com.games;

public class Block {

    private int x;
    private int y;
    private BlockType blockType;
    private Direction blockDirection = null;

    public Block(int x, int y, BlockType blockType) {
        this.x = x;
        this.y = y;
        this.blockType = blockType;
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

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType block) {
        this.blockType = block;
    }

    public Direction getBlockDirection() {
        return blockDirection;
    }

    public void setBlockDirection(Direction blockDirection) {
        this.blockDirection = blockDirection;
    }
}
