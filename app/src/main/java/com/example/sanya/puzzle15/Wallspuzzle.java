package com.example.sanya.puzzle15;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by sanya on 2017.03.13..
 * It's totally not working, but left in, for a later possibility. or when I get smarter :P
 */

public class Wallspuzzle extends AppCompatActivity implements OnClickListener {
    Gameboard table;
    private int[] intImageResources = {0, R.drawable.tile_1, R.drawable.tile_2, R.drawable.tile_3, R.drawable.tile_4, R.drawable.tile_5, R.drawable.tile_6, R.drawable.tile_7, R.drawable.tile_8, R.drawable.tile_9, R.drawable.tile_10, R.drawable.tile_11, R.drawable.tile_12, R.drawable.tile_13, R.drawable.tile_14, R.drawable.tile_15, R.drawable.tile_1};

    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playboard);
        Button b100 = (Button) findViewById(R.id.button30);
        b100.setText(R.string.steps100);
        Button b300 = (Button) findViewById(R.id.button50);
        b300.setText(R.string.steps300);
        Button b500 = (Button) findViewById(R.id.button100);
        b500.setText(R.string.steps500);
/*
        View vHorizontal = findViewById(R.id.horizontalline);
        vHorizontal.setVisibility(View.VISIBLE);
        View vVertical = findViewById(R.id.verticalline);
        vVertical.setVisibility(View.VISIBLE);
*/
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        table = new Gameboard(this, Gameboard.WALLS);
        // set up the OnClickListener for the 10-30-50 radiobuttons and the shufflebutton
        findViewById(R.id.button30).setOnClickListener(this);
        findViewById(R.id.button50).setOnClickListener(this);
        findViewById(R.id.button100).setOnClickListener(this);
        findViewById(R.id.shufflebutton).setOnClickListener(this);
        showTable();
    }

    /**
     * displays the table values for linear(vertical) layouts
     */
    public void showTable() {
        int col, row;
        LinearLayout rowLayout;
        View viewLine;
        // erase all previous children from the layouts
        rowLayout = (LinearLayout) findViewById(R.id.row1);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row2);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row3);
        rowLayout.removeAllViewsInLayout();
        rowLayout = (LinearLayout) findViewById(R.id.row4);
        rowLayout.removeAllViewsInLayout();

        int leftMargin, topMargin;
        View line;

        for (row = 1; row <= 7; row+=2) {
            for (col = 1; col <= 7; col+=2) {
                ImageView newTile = new ImageView(Wallspuzzle.this);
                switch (row) {
                    case 1:
                        rowLayout = (LinearLayout) findViewById(R.id.row1);
                        break;
                    case 3:
                        rowLayout = (LinearLayout) findViewById(R.id.row2);
                        break;
                    case 5:
                        rowLayout = (LinearLayout) findViewById(R.id.row3);
                        break;
                    case 7:
                        rowLayout = (LinearLayout) findViewById(R.id.row4);
                        break;
                    default:
                        rowLayout = (LinearLayout) findViewById(R.id.row4);
                }
                String e = String.valueOf(table.getBoardValue(col, row));
                newTile.setPadding(pxToDp(6),pxToDp(6), pxToDp(6), pxToDp(6));
                newTile.setImageResource(intImageResources[Integer.valueOf(e)]);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxToDp(160), pxToDp(160));
                newTile.setLayoutParams(layoutParams);
                newTile.setTag(String.valueOf(e));
                newTile.setOnClickListener(this);
                rowLayout.addView(newTile);

                if(table.getBoardValue(col+1, row) == 999)  {
                    leftMargin = pxToDp(10+((col+1)/2)*172);
                    topMargin = pxToDp(((row)/2)*170-(row/2)*6);
                    line = new View(Wallspuzzle.this);
                    line.setBackgroundColor(0xffff0000);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(pxToDp(20), pxToDp(160));
                    rlp.setMargins(leftMargin, topMargin, 0, 0);
                    line.setLayoutParams(rlp);

                }

                if(table.getBoardValue(col, row+1) == 999)  {
                    leftMargin = pxToDp((col/2)*180);
                    topMargin = pxToDp((row-1)*160 + col*6);
                    line = new View(Wallspuzzle.this);
                    line.setBackgroundColor(0xffff0000);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(pxToDp(150), pxToDp(20));
                    rlp.setMargins(leftMargin, topMargin, 3, 3);
                    line.setLayoutParams(rlp);

                }
            }
        }
        if (table.isGameWon()) {
            Toast.makeText(Wallspuzzle.this, R.string.youwon, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * handles the clicks on 'tiles' and the radio buttons
     *
     * @param v the view that was clicked on
     */
    public void onClick(View v) {
        int row = 1, col = 1;
        boolean gotCha = false;
        int tileNumber;
        int pushedView = Integer.valueOf(v.getTag().toString());
        // a tile was pushed?
        if (pushedView <= 16) {
            // the tag of the tile clicked on
            String clickedTile = (String) v.getTag();
            if (clickedTile.equals(" ")) {
                tileNumber = 0;
            } else {
                tileNumber = pushedView;
            }

            // where is the clicked tile in the matrix?
            while (row < 8 && !gotCha) {
                while (col < 8 && !gotCha) {
                    if (table.getBoardValue(col, row) == tileNumber) {
                        gotCha = true;
                    }
                    col+=2;
                }
                row+=2;
                if (!gotCha) col = 1;
            }

            col-=2;
            row-=2;

            // col, row position shows to the clicked tile's position
            // now, if possible, swap the tile with the empty spot and play the click sound

            if (table.moveIfCan(col, row)) {

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(Wallspuzzle.this, R.raw.click);

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }

            // show the rearranged table
            showTable();
            // not a tile was clicked, but a radiobutton or the flush button
        } else {
            switch (pushedView) {
                case 100: {
                    // set the flush steps to 100
                    table.setFlushStep(100);
                    break;
                }
                case 300: {
                    // set the flush steps to 300
                    table.setFlushStep(300);
                    break;
                }
                case 500: {
                    // set the flush steps to 500
                    table.setFlushStep(500);
                    break;
                }
                case 700: {
                    // the flush button
                    // reset the table
                    table.resetTable();
                    // shuffle the tiles
                    table.shuffleTable();
                    // show the table
                    showTable();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    // converts px values to dp
    public int pxToDp(int px) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) ((px/scale)+0.5f);
    }
}
