package com.example.tictactoemenus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView;
    private BoardView mBoardView;
    boolean gameOver = false;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = (TextView) findViewById(R.id.information);
        startNewGame();
    }

    private void startNewGame(){
        gameOver = false;
        mGame.clearBoard();
        mBoardView.invalidate();
        // Human goes first
        //mInfoTextView.setText("You go first.");
        mInfoTextView.setText(R.string.first_human);
    }

    private boolean setMove(char player, final int location) {
        if (player == TicTacToeGame.COMPUTER_PLAYER) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, location);
                    mBoardView.invalidate();   // Redraw the board
                    mComputerMediaPlayer.start();

                    int result = mGame.checkForWinner();
                    if (result == 0)
                        mInfoTextView.setText(R.string.turn_human);
                    else
                        checkResult();
                }
            }, 1000);
            return true;
        } else if (mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location)) {
            mBoardView.invalidate();   // Redraw the board
            mHumanMediaPlayer.start();
            return true;
        }

        return false;
    }

    public void checkResult(){
        int result = mGame.checkForWinner();

        if (result == 1) {
            mInfoTextView.setText(R.string.result_tie);
        }
        else if (result == 2) {
            mInfoTextView.setText(R.string.result_human_wins);
        }
        else if (result == 3) {
            mInfoTextView.setText(R.string.result_computer_wins);
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


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = 4;
                if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Easy)
                    selected = 0;
                else if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Harder)
                    selected = 1;
                else if (mGame.getDifficultyLevel() == TicTacToeGame.DifficultyLevel.Expert)
                    selected = 2;


                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.
                                switch (item){
                                    case 0:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;
                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;
                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;

            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
        }

        return dialog;
    }

    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;



            if (!gameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{
                if ( mGame.checkForWinner() == 0 ) {
                    System.out.println("-------location: " + pos);
                    // If no winner yet, let the computer make a move
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                }
            }
            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

}
