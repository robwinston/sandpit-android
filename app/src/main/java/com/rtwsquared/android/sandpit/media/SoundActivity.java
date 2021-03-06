package com.rtwsquared.android.sandpit.media;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class SoundActivity extends TraceBaseActivity {

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        setupTrace(SoundActivity.class.getSimpleName());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.brown_eyed_girl);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                traceMe("Player complete: " + formatDuration(mediaPlayer.getDuration()));
                setButtonsState(mp);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                traceMe(String.format("Player error - what: %d; extra: %d", what, extra));
                setButtonsState(mp);
                return false;
            }
        });


        findViewById(R.id.sound_play_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    traceMe("Player paused: " + formatDuration(mediaPlayer.getCurrentPosition()));
                }
                else
                    mediaPlayer.start();
                setButtonsState(mediaPlayer);
            }
        });

        findViewById(R.id.sound_stop_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                setButtonsState(mediaPlayer);
            }
        });
        findViewById(R.id.sound_reload_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                setButtonsState(mediaPlayer);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
        {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
        }
    }

    // These methods assume they'll be working with an ImageButton!
    private void setButtonImage(View view, int imageId) {
        ((ImageButton) view).setImageResource(imageId);
    }
    private void setButtonImage(int buttonId, int imageId) {
        ((ImageButton)findViewById(buttonId)).setImageResource(imageId);
    }

    private void setButtonsState(MediaPlayer mediaPlayer) {
        View playButton = findViewById(R.id.sound_play_button_id);
        View stopButton = findViewById(R.id.sound_stop_button_id);
        View reloadButton = findViewById(R.id.sound_reload_button_id);

        if (mediaPlayer.isPlaying()) {
            setButtonImage(playButton, R.drawable.pause);
            stopButton.setEnabled(true);
            reloadButton.setEnabled(true);
        }
        else {
            setButtonImage(playButton, R.drawable.play);
            stopButton.setEnabled(false);
            reloadButton.setEnabled(false);
        }
    }


    private String formatDuration(int duration) {
        int milliseconds = duration%1000;
        int seconds = (duration/1000)%60;
        int minutes = ((duration/1000)%60)/60;

        return String.format("%02d:%02d:%04d (%d)", minutes, seconds, milliseconds, duration);
    }
}
