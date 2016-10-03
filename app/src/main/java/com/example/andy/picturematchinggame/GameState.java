package com.example.andy.picturematchinggame;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class that stores a snapshot of a game in progress.  Basically a Java Bean except with a
 * special constructor which populates all the fields.
 * @author Andrew Jarombek
 * @since 9/29/2016
 */
public class GameState implements Serializable {

    private Map<Integer, String> pictureLocations;
    private List<Integer> unrevealed;
    private int score, highscore;

    /**
     * Constructor for the full GameState.  This constructor populates all of the fields in the
     * class, and is used exclusively in the app to create GameState instances
     * @param pictureLocations maps the location on the board to the name of the player that
     *                         is hidden there.
     * @param unrevealed a list of all the locations that have yet to be successfully picked
     * @param score the score of the game, how many times the user has clicked on an ImageButton
     * @param highscore the high score of the user across all of the games played
     */
    public GameState(Map<Integer, String> pictureLocations, List<Integer> unrevealed,
                     int score, int highscore) {
        this.pictureLocations = pictureLocations;
        this.unrevealed = unrevealed;
        this.score = score;
        this.highscore = highscore;
    }

    // Getters and Setters

    public Map<Integer, String> getPictureLocations() {
        return pictureLocations;
    }

    public void setPictureLocations(Map<Integer, String> pictureLocations) {
        this.pictureLocations = pictureLocations;
    }

    public List<Integer> getUnrevealed() {
        return unrevealed;
    }

    public void setUnrevealed(List<Integer> unrevealed) {
        this.unrevealed = unrevealed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}
