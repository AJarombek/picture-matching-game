package com.example.andy.picturematchinggame;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * This is the main fragment of this application.  It contains the playing grid and the
 * properties of the specific game instance.
 * @author Andrew Jarombek
 * @since 9/28/2016
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment: ";
    private static final int NUMBER_OF_PICTURES = 16;
    private static final int NUMBER_OF_ATHLETES = 32;

    private int timesClicked, correctPicks;
    private List<String> pickedAthlete;
    private List<Integer> pickedLocation;
    private List<Integer> incompleteLocations;
    private List<String> athletes;
    private List<Integer> locations;
    private Map<Integer, String> pictureLocations;
    private Handler handler;
    private View v;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Android onCreate method
     * @param savedInstanceState --
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save on rotation
        setRetainInstance(true);
        handler = new Handler();
    }

    /**
     * Android onCreateView method
     * @param inflater --
     * @param container --
     * @param savedInstanceState --
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inside onCreateView.");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        this.v = v;

        // populate athlete array
        athletes = new ArrayList<>(NUMBER_OF_ATHLETES);
        for (int i = 1; i <= NUMBER_OF_ATHLETES; i++) {
            athletes.add("athlete" + i);
        }

        // Populate locations
        locations = new ArrayList<>();
        for (int i=0; i < NUMBER_OF_PICTURES; i++) {
            locations.add(i);
        }

        // Initialize instance variables
        pictureLocations = new HashMap<>();
        pickedAthlete = new ArrayList<>();
        pickedLocation = new ArrayList<>();

        return v;
    }

    /**
     * Android onStart method - disables clicking on pictures that have already been matched
     */
    @Override
    public void onStart() {
        super.onStart();
        disableButtons(incompleteLocations);
    }

    /**
     * Starts a new game from scratch, resets any old game data
     */
    public void startNewGame() {
        Log.d(TAG, "Inside startNewGame.");
        incompleteLocations = new ArrayList<>(locations);
        setPictureLocations();
        initViews(pictureLocations);
        correctPicks = timesClicked = 0;
    }

    /**
     * Sets the picturelocations map for the game
     * This method first gets 8 athletes to play with and then
     * determines which tiles contain which hidden pictures
     */
    protected void setPictureLocations() {
        Log.d(TAG, "Inside setPictureLocations.");
        // Make a set of athletes to be put on the playing board
        List<String> athletesInPlay = new ArrayList<>();

        // Get 8 athletes
        while (athletesInPlay.size() < 16) {
            String randomAthlete = athletes.get(new Random().nextInt(athletes.size()));
            if (!athletesInPlay.contains(randomAthlete)) {
                athletesInPlay.add(randomAthlete);
                athletesInPlay.add(randomAthlete);
            }
        }
        for (int i = 0; i < NUMBER_OF_PICTURES; i++) {
            // Get random picture for this tile
            final String athlete = athletesInPlay.get(new Random().nextInt(athletesInPlay.size()));
            athletesInPlay.remove(athlete);

            // ImageButton for this tile
            final ImageButton ib = getImageButton(i);

            // Populate the pictureLocations map with this buttons data
            pictureLocations.put(i, athlete);

            // Gives each button metadata about what image the button is hiding
            ib.setTag(i);
        }
    }

    /**
     * Initialize all of the tile views on the game board
     */
    protected void initViews(Map<Integer, String> pictureLoc) {
        Log.d(TAG, "Inside initViews.");

        // set up the click listeners for each tile
        for (int i = 0; i < NUMBER_OF_PICTURES; i++) {
            final int location = i;
            final String athlete = pictureLoc.get(i);

            // ImageButton for this tile
            final ImageButton ib = getImageButton(location);

            // Set a click listener for selecting this tile
            ib.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)getActivity()).startMove();

                    // When a tile is clicked, the player image is displayed
                    showImage(ib, athlete);

                    timesClicked++;
                    ((MainActivity)getActivity()).updateScore(timesClicked);
                    if (timesClicked % 2 == 1) {
                        // The first move has been made
                        pick(location, athlete);
                    } else {
                        disableButtons(incompleteLocations);
                        pick(location, athlete);
                        if (pickedAthlete.get(0).equals(pickedAthlete.get(1))) {
                            // We have a match!
                            interpretMove(true);
                        } else {
                            // We don't have a match
                            interpretMove(false);
                        }
                    }
                    ((MainActivity)getActivity()).endMove();
                }
            });
        }
    }

    /**
     * pick a tile and store its location and athlete information
     * @param location the location picked
     * @param athlete the athlete picked
     */
    protected void pick(int location, String athlete) {
        pickedAthlete.add(athlete);
        pickedLocation.add(location);
        disableTile(location);
    }

    /**
     * Make a button non clickable
     * @param location the location of the button in the grid
     */
    protected void disableTile(int location) {
        ImageButton ib = getImageButton(location);
        ib.setClickable(false);
    }

    /**
     * Make a button clickable
     * @param location the location of the button in the grid
     */
    protected void enableTile(int location) {
        ImageButton ib = getImageButton(location);
        ib.setClickable(true);
    }

    /**
     * Make a group of buttons non clickable
     * @param locations a list of button locations in the grid
     */
    protected void disableButtons(List<Integer> locations) {
        for (int i : locations) {
            ImageButton ib = getImageButton(i);
            ib.setClickable(false);
        }
    }

    /**
     * Make a group of buttons clickable
     * @param locations a list of button locations in the grid
     */
    protected void enableButtons(List<Integer> locations) {
        for (int i : locations) {
            ImageButton ib = getImageButton(i);
            ib.setClickable(true);
        }
    }

    /**
     * Reset all of the images on the playing board to the default image
     */
    protected void resetImages() {
        for (int i : locations)
            flipTile(i);
    }

    /**
     * Show the hidden image at a specific tile on the board
     * @param ib the ImageButton that should reveal its hidden image
     * @param athlete the athlete that is hidden
     */
    protected void showImage(ImageButton ib, String athlete) {
        Context context = getActivity();
        int id = context.getResources().getIdentifier(
                athlete, "drawable", context.getPackageName());
        ib.setImageDrawable(ContextCompat.getDrawable(getActivity(), id));
    }

    /**
     * After a rotation or other type of destruction of the app, this method helps to
     * restore the image to match the current game state.  The logic of this scenario is more
     * involved than showImage(), so it requires a separate method.
     * @param location the location of the ImageButton in the playing grid
     */
    protected void restoreImage(int location) {
        Log.d(TAG, "Inside restoreImage.");
        ImageButton ib = getImageButton(location);
        ib.setClickable(false);
        String imageName = pictureLocations.get(location);
        Context context = getActivity();
        int id = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
        ib.setImageDrawable(ContextCompat.getDrawable(getActivity(), id));
    }

    /**
     * Flips tile back to the default tile
     * @param location the location of the tile
     */
    protected void flipTile(int location) {
        ImageButton ib = getImageButton(location);

        ib.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.defaultpic));
        enableTile(location);
    }

    /**
     * Gets the ImageButton object from a specific location in the playing grid
     * @param location the location of the button
     * @return the requested ImageButton
     */
    protected ImageButton getImageButton(int location) {
        Resources r = getResources();
        int id = r.getIdentifier("i"+location,"id", getActivity().getPackageName());
        return (ImageButton) v.findViewById(id);
    }

    /**
     * The main logic behind when two moves have been made.  An overlay is first applied to
     * the screen so nothing can be clicked on.  Then the method checks to see if the pictures
     * are a match or not.
     * @param match
     */
    protected void interpretMove(boolean match) {
        final boolean isMatch = match;
        View overlay = getActivity().findViewById(R.id.overlay);
        overlay.setVisibility(View.VISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMatch) {
                    correctPicks++;
                    incompleteLocations.remove(pickedLocation.get(0));
                    incompleteLocations.remove(pickedLocation.get(1));
                    if (correctPicks == NUMBER_OF_PICTURES / 2) {
                        ((MainActivity) getActivity()).gameOver(timesClicked);
                    }
                } else {
                    // Flip back the picked tiles
                    for (int loc : pickedLocation)
                        flipTile(loc);
                }

                enableButtons(incompleteLocations);

                // reset picked items
                pickedLocation = new ArrayList<>();
                pickedAthlete = new ArrayList<>();
            }
        }, 500);
        v.setEnabled(true);
        overlay.setVisibility(View.GONE);
    }

    /**
     * Getter method for the incompleteLocations list
     * @return the incompleteLocations list
     */
    public List<Integer> getIncompleteLocations() {
        return incompleteLocations;
    }

    /**
     * Getter method for the pictureLocations map
     * @return the pictureLocations map
     */
    public Map<Integer, String> getPictureLocations() {
        return pictureLocations;
    }

    /**
     * Creates and returns a GameState object which is a snapshot of the current state of
     * the game
     * @return the current GameState
     */
    public GameState getGameState() {
        return new GameState(pictureLocations, incompleteLocations, timesClicked,
                ((MainActivity) getActivity()).getHighScore());
    }

    /**
     * Restores a saved GameState, likely after a rotation change.
     * @param gameState the GameState that a app should be set to
     */
    public void restoreGameState(GameState gameState) {
        Log.d(TAG, "Inside restoreGameState.");
        pictureLocations = gameState.getPictureLocations();
        incompleteLocations = gameState.getUnrevealed();
        initViews(pictureLocations);
        for (int l : locations) {
            if (!incompleteLocations.contains(l))
                restoreImage(l);
        }
    }
}
