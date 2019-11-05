package com.example.tictactoem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Room {

    public String id;
    public String name;
    public String player1;
    public String player2;
    public String turn;
    public ArrayList<String> mBoard;

    public Room(){};

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Room(String id, String name, String player1){
        this.id = id;
        this.name = name;
        this.player1 = player1;
        this.player2 = "";
        this.turn = player1;
        this.mBoard = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            this.mBoard.add("-");
    }

    public Room(String id, String name, String player1, ArrayList<String> mBoard){
        this.id = id;
        this.name = name;
        this.player1 = player1;
        this.player2 = "";
        this.turn = player1;
        this.mBoard = mBoard;
    }

    public Room(String id, String name, String player1, String player2, String turn, ArrayList<String> mBoard){
        this.id = id;
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
        this.mBoard = mBoard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public ArrayList<String > getmBoard() {
        return mBoard;
    }

    public void setmBoard(ArrayList<String> mBoard) {
        this.mBoard = mBoard;
    }

    @Override
    public String toString() {
        return "Room: " + this.name;
    }
}
