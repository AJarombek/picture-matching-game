package com.example.andy.picturematchinggame;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the main activity of the application.  It contains the menu screens and starts/ends
 * games.  It also holds the views for the score and high score.
 * @author Andrew Jarombek
 * @since 9/28/2016
 */

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity: ";
    private static final String KEY = "restore";
    int hs;
    Button startButton, resetButton;
    View pausedView, overlay, finished, playAgain;
    TextView score, highscore, result;
    Fragment fragment;

    /**
     * Android onCreate method
     * @param savedInstanceState --
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside onCreate.");

        // Instantiate the views
        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        pausedView = findViewById(R.id.paused_view);
        overlay = findViewById(R.id.overlay);
        finished = findViewById(R.id.finished_view);
        playAgain = findViewById(R.id.replay_button);
        score = (TextView) findViewById(R.id.score);
        highscore = (TextView) findViewById(R.id.high_score);
        result = (TextView) findViewById(R.id.result);
        fragment = getFragmentManager().findFragmentById(R.id.main_fragment);

        highscore.setTag(null);

        // If there is a saved state, apply the proper game state to the application
        if (savedInstanceState != null) {
            GameState gameState = (GameState) savedInstanceState.getSerializable(KEY);
            startButton.setText("Resume");
            updateScore(gameState.getScore());
            updateHighScore(gameState.getHighscore());
            ((MainFragment) fragment).restoreGameState(gameState);
        } else {
            ((MainFragment) fragment).startNewGame();
        }

        // Click listener for the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedView.setVisibility(View.GONE);
                ((MainFragment) fragment).enableButtons(
                        ((MainFragment) fragment).getIncompleteLocations());
            }
        });

        // Click listener for the reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) fragment).startNewGame();
                ((MainFragment) fragment).resetImages();
                updateScore(0);
                pausedView.setVisibility(View.GONE);
            }
        });

        // Click listener for the play again button
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) fragment).startNewGame();
                ((MainFragment) fragment).resetImages();
                updateScore(0);
                finished.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Android onSaveInstanceState method
     * @param outState --
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Inside onSaveInstanceState.");

        // Save the current game state
        outState.putSerializable(KEY, ((MainFragment) fragment).getGameState());
    }

    /**
     * Update the score of the current game
     * @param score the score to be applied
     */
    public void updateScore(int score) {
        String newScore = "Score: " + score;
        this.score.setText(newScore);
    }

    /**
     * Update the high score among all games played
     * @param score the high score to potentially be applied
     */
    public void updateHighScore(int score) {
        String newhs = "High Score: " + score;

        // If there was no previous high score, update the high score
        // Else if the score is better than the old high score, update the high score
        if (highscore.getTag() == null || highscore.getTag().equals(0)) {
            hs = score;
            highscore.setTag(score);
            highscore.setText(newhs);
        } else if (score < Integer.parseInt(highscore.getTag().toString())) {
            highscore.setText(newhs);
            hs = score;
        }
    }

    /**
     * Start the players move by applying an overlay to the board,
     * making none of the buttons clickable
     */
    public void startMove() {
        overlay.setVisibility(View.VISIBLE);
        overlay.setClickable(false);
    }

    /**
     * End the players move by removing the overlay from the board,
     * making the buttons clickable
     */
    public void endMove() {
        overlay.setVisibility(View.GONE);
        overlay.setClickable(true);
    }

    /**
     * Method to apply appropriate logic when the game is over
     * @param score the final score of the game
     */
    public void gameOver(int score) {
        String yourScore = "Your Score: " + score;
        result.setText(yourScore);
        updateHighScore(score);

        // Show the finished screen with the play again button
        finished.setVisibility(View.VISIBLE);
    }

    /**
     * Getter method for the high score
     * @return the high score
     */
    public int getHighScore() {
        return hs;
    }
}
