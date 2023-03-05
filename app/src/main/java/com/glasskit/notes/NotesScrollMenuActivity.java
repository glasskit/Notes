package com.glasskit.notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NotesScrollMenuActivity extends Activity {
    private int scrollPosition = -1;
    private String noteText = "";
    private TextToSpeech noteReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the text to speech to read the notes
        noteReader = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) { /* Do nothing */ }
        });
        // grab the note index and note text from the intent
        scrollPosition = getIntent().getIntExtra("scrollposition", -1);
        noteText = getIntent().getStringExtra("notetext");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scroll_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_note_aloud:
                // make the glass read the note out loud
                readNote(this, noteText);
                return true;
            case R.id.delete:
                // delete note from shared preferences
                Utils.deleteNoteAtIndex(scrollPosition, this, getString(R.string.shared_notes_key));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // close the note live card
        finish();
    }

    public void readNote(Context context, String note) {
        // Make the class speak the current note
        // QUEUE_FLUSH => Gets rid of all queued items and sets the current on in the queue
        noteReader.speak(note, TextToSpeech.QUEUE_FLUSH, null);
    }
}
