package com.glasskit.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.ArrayList;

public class VoiceToSpeechActivity extends Activity {
    private static final int SPEECH_REQUEST = 0;
    private String noteResult;
    private ArrayList<String> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displaySpeechRecognizer();
    }

    private void displaySpeechRecognizer() {
        startActivityForResult(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
            SPEECH_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
            // Get results of speech to text
            noteResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            // get list of notes saved in shared prefs
            if (Utils.checkForObjectInSharedPrefs(this, getString(R.string.shared_notes_key))) {
                notesList = new ArrayList<>(
                    Utils.getStringArrayPref(this, getString(R.string.shared_notes_key)));
            } else {
                notesList = new ArrayList<>();
            }
            // add the new note to the list
            notesList.add(noteResult);
            // save the new list
            Utils.commitNewNoteList(this, getString(R.string.shared_notes_key), notesList);
            // update the list in the live card
            stopService(new Intent(this, ViewNoteService.class));
            final Intent serviceIntent = new Intent(this, ViewNoteService.class);
            serviceIntent.putExtra("update", true);
            startService(serviceIntent);
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
