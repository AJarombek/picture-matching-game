package com.example.andy.picturematchinggame;

import android.os.Handler;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
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
    Handler h;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside onCreate.");

        h = new Handler();

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

        if (savedInstanceState != null) {
            GameState gameState = (GameState) savedInstanceState.getSerializable(KEY);
            startButton.setText("Resume");
            updateScore(gameState.getScore());
            updateHighScore(gameState.getHighscore());
            ((MainFragment) fragment).restoreGameState(gameState);
        } else {
            ((MainFragment) fragment).startNewGame();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedView.setVisibility(View.GONE);
                ((MainFragment) fragment).enableButtons(
                        ((MainFragment) fragment).getIncompleteLocations());
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) fragment).startNewGame();
                ((MainFragment) fragment).resetImages();
                updateScore(0);
                pausedView.setVisibility(View.GONE);
            }
        });

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Inside onSaveInstanceState.");
        outState.putSerializable(KEY, ((MainFragment) fragment).getGameState());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void updateScore(int score) {
        String newScore = "Score: " + score;
        this.score.setText(newScore);
    }

    public void updateHighScore(int score) {
        String newhs = "High Score: " + score;
        if (highscore.getTag() == null || highscore.getTag().equals(0)) {
            hs = score;
            highscore.setTag(score);
            highscore.setText(newhs);
        } else if (score < Integer.parseInt(highscore.getTag().toString())) {
            highscore.setText(newhs);
            hs = score;
        }
    }

    public void startMove() {
        overlay.setVisibility(View.VISIBLE);
        overlay.setClickable(false);
    }

    public void endMove() {
        overlay.setVisibility(View.GONE);
        overlay.setClickable(true);
    }

    public void gameOver(int score) {
        String yourScore = "Your Score: " + score;
        result.setText(yourScore);

        updateHighScore(score);
        finished.setVisibility(View.VISIBLE);
    }

    public int getHighScore() {
        return hs;
    }
}
