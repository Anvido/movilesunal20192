package com.example.tictactoem;

import java.util.ArrayList;
import java.util.Random;

/* TicTacToeConsole.java
 * By Frank McCown (Harding University)
 *
 * This is a tic-tac-toe game that runs in the console window.  The human
 * is X and the computer is O.
 */


public class TicTacToeGame {
    //private char mBoard[] = new char[9];
    public ArrayList<String> mBoard;
    public static final int BOARD_SIZE = 9;

    public ArrayList<String> getmBoard() {
        return mBoard;
    }

    public void setmBoard(ArrayList<String> mBoard) {
        this.mBoard = mBoard;
    }

    public static final String HUMAN_PLAYER = "X";
    public static final String COMPUTER_PLAYER = "O";
    public static final String OPEN_SPOT = "-";

    // The computer's difficulty levels
    public enum DifficultyLevel {Easy, Harder, Expert};

    // Current difficulty level
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    private Random mRand;

    public TicTacToeGame() {

        // Seed the random number generator
        mRand = new Random();
        this.mBoard = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            this.mBoard.add("-");
    }

    public TicTacToeGame(ArrayList<String> mBoard) {

        // Seed the random number generator
        mRand = new Random();
        this.mBoard = mBoard;
    }

    public String getBoardOccupant(int location) {
        if (location >= 0 && location < BOARD_SIZE)
            return mBoard.get(location);
        return "-";
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard.get(i).equalsIgnoreCase(HUMAN_PLAYER) &&
                    mBoard.get(i + 1).equalsIgnoreCase(HUMAN_PLAYER) &&
                    mBoard.get(i + 2).equalsIgnoreCase(HUMAN_PLAYER))
                return 2;
            if (mBoard.get(i).equalsIgnoreCase(COMPUTER_PLAYER) &&
                    mBoard.get(i + 1).equalsIgnoreCase(COMPUTER_PLAYER) &&
                    mBoard.get(i + 2).equalsIgnoreCase(COMPUTER_PLAYER))
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard.get(i).equalsIgnoreCase(HUMAN_PLAYER) &&
                    mBoard.get(i + 3).equalsIgnoreCase(HUMAN_PLAYER) &&
                    mBoard.get(i + 6).equalsIgnoreCase(HUMAN_PLAYER))
                return 2;
            if (mBoard.get(i).equalsIgnoreCase(COMPUTER_PLAYER) &&
                    mBoard.get(i + 3).equalsIgnoreCase(COMPUTER_PLAYER) &&
                    mBoard.get(i + 6).equalsIgnoreCase(COMPUTER_PLAYER))
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard.get(0).equalsIgnoreCase(HUMAN_PLAYER) &&
                mBoard.get(4).equalsIgnoreCase(HUMAN_PLAYER) &&
                mBoard.get(8).equalsIgnoreCase(HUMAN_PLAYER)) ||
                (mBoard.get(2).equalsIgnoreCase(HUMAN_PLAYER) &&
                        mBoard.get(4).equalsIgnoreCase(HUMAN_PLAYER) &&
                        mBoard.get(6).equalsIgnoreCase(HUMAN_PLAYER)))
            return 2;
        if ((mBoard.get(0).equalsIgnoreCase(COMPUTER_PLAYER) &&
                mBoard.get(4).equalsIgnoreCase(COMPUTER_PLAYER) &&
                mBoard.get(8).equalsIgnoreCase(COMPUTER_PLAYER)) ||
                (mBoard.get(2).equalsIgnoreCase(COMPUTER_PLAYER) &&
                        mBoard.get(4).equalsIgnoreCase(COMPUTER_PLAYER) &&
                        mBoard.get(6).equalsIgnoreCase(COMPUTER_PLAYER)))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (!mBoard.get(i).equalsIgnoreCase(HUMAN_PLAYER) && !mBoard.get(i).equalsIgnoreCase(COMPUTER_PLAYER))
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    private int getRandomMove(){
        int move;

        // Generate random move
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard.get(move).equalsIgnoreCase(HUMAN_PLAYER) || mBoard.get(move).equalsIgnoreCase(COMPUTER_PLAYER));

        System.out.println("Rand + Computer is moving to " + (move));

        return move;
    }

    private int getWinningMove(){

        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!mBoard.get(i).equalsIgnoreCase(HUMAN_PLAYER) && !mBoard.get(i).equalsIgnoreCase(COMPUTER_PLAYER)) {
                String curr = mBoard.get(i);
                mBoard.set(i, COMPUTER_PLAYER);
                if (checkForWinner() == 3) {
                    System.out.println("To Win + Computer is moving to " + (i));
                    return i;
                } else
                    mBoard.set(i, curr);
            }
        }

        return -1;
    }

    private int getBlockingMove(){

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!mBoard.get(i).equalsIgnoreCase(HUMAN_PLAYER) && !mBoard.get(i).equalsIgnoreCase(COMPUTER_PLAYER)) {
                String curr = mBoard.get(i);   // Save the current number
                mBoard.set(i, HUMAN_PLAYER);
                if (checkForWinner() == 2) {
                    mBoard.set(i, COMPUTER_PLAYER);
                    System.out.println("To Block + Computer is moving to " + (i));
                    return i;
                } else
                    mBoard.set(i, curr);
            }
        }

        return -1;
    }

    public int getComputerMove() {

        int move = -1;

        if (mDifficultyLevel == DifficultyLevel.Easy)
            move = getRandomMove();
        else if (mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove();
            if (move == -1)
                move = getRandomMove();
        }
        else if (mDifficultyLevel == DifficultyLevel.Expert) {

            // Try to win, but if that's not possible, block.
            // If that's not possible, move anywhere.
            move = getWinningMove();
            if (move == -1)
                move = getBlockingMove();
            if (move == -1)
                move = getRandomMove();
        }

        //mBoard[move] = COMPUTER_PLAYER;

        return move;
    }

    /**
     * Clear the board of all X's and O's by setting all spots to OPEN_SPOT.
     */
    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard.set(i,OPEN_SPOT);
        }
    }

    /**
     * Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player   - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public boolean setMove(String player, int location) {
        if (location >= 0 && location < BOARD_SIZE && mBoard.get(location).equalsIgnoreCase(OPEN_SPOT)) {
            System.out.println("Player : " + player + ", location: " + location);
            mBoard.set(location,player);
            return true;
        }
        return false;
    }

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    //public int getComputerMove(){}

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    //public int checkForWinner(){}


    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }
}

