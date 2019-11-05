package com.example.tictactoem;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TicTacToeActivity extends AppCompatActivity {

    //static final int DIALOG_QUIT_ID = 1;
    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private BoardView mBoardView;
    boolean gameOver = false;
    boolean mSoundOn = true;
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;
    private SharedPreferences mPrefs;
    private String roomId, currentPlayer, player_2, CURR_PLAYER, turn;
    private Room r;
    private boolean p2Join;


    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;

    /*
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuBuilder m = (MenuBuilder) menu;
        m.setOptionalIconsVisible(true);
        return true;
    }
    */

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            //case R.id.ai_difficulty:
            //   showDialog(DIALOG_DIFFICULTY_ID);
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }
    */
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            mSoundOn = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }
    }
    */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        initFirebase();

        roomId = getIntent().getStringExtra("ROOM_ID");
        currentPlayer= getIntent().getStringExtra("CURR_PLA");
        player_2 = getIntent().getStringExtra("PLAYER_2");
        turn = getIntent().getStringExtra("TURN");
        p2Join = false;

        if (player_2.equalsIgnoreCase("Yes")){
            CURR_PLAYER = TicTacToeGame.COMPUTER_PLAYER;
        } else {
            CURR_PLAYER = TicTacToeGame.HUMAN_PLAYER;
        }


        DatabaseReference room = dbReference.child("Rooms").child(roomId);
        room.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                r = dataSnapshot.getValue(Room.class);
                ArrayList<String> board = r.getmBoard();

                mGame.setmBoard(r.getmBoard());
                turn = r.getTurn();
                mBoardView.invalidate();
                int result = mGame.checkForWinner();
                if (result == 0 && turn.equalsIgnoreCase(currentPlayer))
                    mInfoTextView.setText(R.string.turn_human);
                checkResult();

                if(!r.getPlayer2().equalsIgnoreCase("") && !p2Join){
                    Toast.makeText(TicTacToeActivity.this, r.getPlayer2() + " has Joined", Toast.LENGTH_LONG).show();
                    p2Join = true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mGame = new TicTacToeGame();
        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = (TextView) findViewById(R.id.information);
        // Restore the scores from the persistent preference data source
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        /*
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
         */
        startNewGame();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        fbDatabase = FirebaseDatabase.getInstance();
        dbReference = fbDatabase.getReference();
    }

    private void startNewGame() {
        gameOver = false;
        mGame.clearBoard();
        mBoardView.invalidate();
        // Human goes first
        //mInfoTextView.setText("You go first.");
        if (player_2.equalsIgnoreCase("Yes"))
            mInfoTextView.setText("You go second");
        else
            mInfoTextView.setText(R.string.first_human);
    }

    private boolean setMove(String player, final int location) {
        DatabaseReference newRom = dbReference.child("Rooms").child(roomId);
        if (player_2.equalsIgnoreCase("Yes")){
            r.setTurn(r.getPlayer1());
        } else {
            r.setTurn(r.getPlayer2());
        }

        if (player.equalsIgnoreCase(TicTacToeGame.COMPUTER_PLAYER)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, location);

                    if (mSoundOn) mComputerMediaPlayer.start();
                    checkResult();
                    DatabaseReference newRom = dbReference.child("Rooms").child(roomId);
                    newRom.setValue(new Room(roomId, r.getName(), r.getPlayer1(), r.getPlayer2(), r.getTurn(), mGame.mBoard));
                    mBoardView.invalidate();   // Redraw the board
                }
            }, 1000);
            return true;
        } else if (mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location)) {

            if (mSoundOn)  mHumanMediaPlayer.start();
            checkResult();
            newRom.setValue(new Room(roomId, r.getName(), r.getPlayer1(), r.getPlayer2(), r.getTurn(), mGame.mBoard));
            mBoardView.invalidate();   // Redraw the board
            return true;
        }

        return false;
    }

    public void checkResult() {
        int result = mGame.checkForWinner();

        if (result == 1) {
            mInfoTextView.setText(R.string.result_tie);
        } else if (result == 2) {
            mInfoTextView.setText(r.getPlayer1() + " won!.");
        } else if (result == 3) {
            mInfoTextView.setText(r.getPlayer2() + " won!.");
        }
        if (result != 0) gameOver = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.x_sound);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.o_sound);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }


    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (turn.equalsIgnoreCase(currentPlayer) && !r.getPlayer2().equalsIgnoreCase("")) {
                if (!gameOver  && setMove(CURR_PLAYER, pos)) {
                    if (mGame.checkForWinner() == 0) {
                        System.out.println("-------location: " + pos);
                        if (player_2.equalsIgnoreCase("Yes"))
                            mInfoTextView.setText(r.getPlayer1() + " turn");
                        else
                            mInfoTextView.setText(r.getPlayer2() + " turn");
                    }
                }
            }
            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

}
