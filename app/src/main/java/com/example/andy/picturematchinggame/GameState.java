package com.example.andy.picturematchinggame;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Jarombek
 * @since 9/29/2016
 */
public class GameState implements Serializable {

    private Map<Integer, String> pictureLocations;
    private List<Integer> unrevealed;
    private int score, highscore;

    public GameState(Map<Integer, String> pictureLocations, List<Integer> unrevealed,
                     int score, int highscore) {
        this.pictureLocations = pictureLocations;
        this.unrevealed = unrevealed;
        this.score = score;
        this.highscore = highscore;
    }

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
