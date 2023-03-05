package com.glasskit.notes;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.widget.CardBuilder;

import java.util.ArrayList;
import java.util.List;

public class ViewNoteService extends Service {
    private LiveCard noteLiveCard;
    private final static String CARD_ID = "NotesGlassLiveCard";
    private List<String> notesList;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Utils.checkForObjectInSharedPrefs(this, getString(R.string.shared_notes_key))) {
            notesList = new ArrayList<>(
                    Utils.getStringArrayPref(this, getString(R.string.shared_notes_key)));
        } else {
            notesList = new ArrayList<>();
        }
    }

    // Sets what happens when the service is launched, here we push the card to the Timeline
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // sets the menu of the card
        Intent menu;

        // get the update boolean from the intent
        boolean update = intent.getBooleanExtra("update", false);
        // the live card is not created and published yet
        if (noteLiveCard == null) {
            // create live card
            noteLiveCard = new LiveCard(this, CARD_ID);
            RemoteViews remoteViews;
            CardBuilder builder = new CardBuilder(this, CardBuilder.Layout.TEXT);

            // if there are notes
            if (notesList.size() > 0) {
                // Set the menu activity when the card is pressed
                menu = new Intent(this, ViewNoteMenuActivity.class);

                builder.setText(notesList.get(notesList.size() - 1))
                        .setFootnote("Note");

                if (notesList.size() > 1) {
                    builder.showStackIndicator(true);
                }

                remoteViews = builder.getRemoteViews();
            } else {
                menu = new Intent(this, ViewNoteMenuActivityNoNotes.class);

                remoteViews = builder
                        .setText("There are no notes yet!")
                        .setFootnote("To add a new note say \"ok glass, take a note\"")
                        .getRemoteViews();
            }
            // set the views of the card
            noteLiveCard.setViews(remoteViews);
            noteLiveCard.setAction(PendingIntent.getActivity(this, 0, menu, 0));

            // Publish the card to the timeline: Set publish mode to reveal
            if (update) {
                noteLiveCard.publish(LiveCard.PublishMode.SILENT);
            } else {
                noteLiveCard.publish(LiveCard.PublishMode.REVEAL);
            }
        } else {
            // Live card is already published, Jump to the live card.
            noteLiveCard.navigate();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // remove the card from timeline
        if (noteLiveCard != null && noteLiveCard.isPublished()) {
            noteLiveCard.unpublish();
            noteLiveCard = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
