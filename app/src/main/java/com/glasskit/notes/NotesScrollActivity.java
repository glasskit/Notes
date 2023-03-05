

package com.glasskit.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class NotesScrollActivity extends Activity {
    private GestureDetector openMenuGesture;
    private List<Card> noteCards;
    private CardScrollView noteCardScrollView;
    private ArrayList<String> notesList;
    private NotesScrollAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the cards to be used in the CardScrollView
        createCards();
        // Set up the gesture recognizer
        openMenuGesture = createGestureDetector(this);
        // Create the the CardScrollView
        noteCardScrollView = new CardScrollView(this);
        // Create the adapter for the cardscoll
        adapter = new NotesScrollAdapter(noteCards);
        noteCardScrollView.setAdapter(adapter);
        noteCardScrollView.activate();
        setContentView(noteCardScrollView);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get the saved list of notes from shared preferences
        if (Utils.checkForObjectInSharedPrefs(this, getString(R.string.shared_notes_key))) {
            notesList = new ArrayList<>(
                Utils.getStringArrayPref(this, getString(R.string.shared_notes_key)));
        } else {
            notesList = new ArrayList<>();
        }

        // Check if a note has been deleted
        if (adapter.getCount() > notesList.size()) {
            //remove the card of the note that has been deleted
            adapter.removeNote(noteCardScrollView.getSelectedItemPosition());
            //refresh the service. This allows the list in the live card to update
            stopService(new Intent(this, ViewNoteService.class));
            final Intent serviceIntent = new Intent(this, ViewNoteService.class);
            serviceIntent.putExtra("update", true);
            startService(serviceIntent);
        }
        // close the card scroll view if there are no more notes
        if (notesList.size() == 0) {
            finish();
        }

        adapter.notifyDataSetChanged();
    }

    private void createCards() {
        // initalize the array list that will hold all the cards
        noteCards = new ArrayList<>();

        // Get the saved list of notes from shared preferences
        if (Utils.checkForObjectInSharedPrefs(this, getString(R.string.shared_notes_key))) {
            notesList = new ArrayList<>(Utils.getStringArrayPref(this,
                getString(R.string.shared_notes_key)));
        } else {
            notesList = new ArrayList<>();
        }

        // Create a card for each note
        Card tempNoteCard;
        for (int i = 0; i < notesList.size(); i++) {
            tempNoteCard = new Card(this);
            tempNoteCard.setText(notesList.get(i));
            tempNoteCard.setFootnote("Note " + (i + 1) + " of " + notesList.size());
            noteCards.add(tempNoteCard);
        }
    }

    // Gesture recognizer. this will handle opening the menu when a card is clicked
    private GestureDetector createGestureDetector(final Context context) {
        final GestureDetector gestureDetector = new GestureDetector(context);
        // Create a base listener for generic gestures
        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.LONG_PRESS) {
                    // create intent to open the menu
                    final Intent intent = new Intent(context, NotesScrollMenuActivity.class);
                    // pass card index and card text to the intent
                    intent.putExtra("scrollposition",
                        noteCardScrollView.getSelectedItemPosition())
                        .putExtra("notetext",
                            notesList.get(noteCardScrollView.getSelectedItemPosition()));
                    // open the menu
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // Send generic motion events to the gesture detector
        return openMenuGesture != null && openMenuGesture.onMotionEvent(event);
    }
}
