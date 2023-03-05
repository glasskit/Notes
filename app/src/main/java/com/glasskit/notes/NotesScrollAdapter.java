package com.glasskit.notes;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

class NotesScrollAdapter extends CardScrollAdapter {
    private List<Card> noteCards;

    public NotesScrollAdapter(List<Card> cards) {
        super();
        noteCards = cards;
    }

    @Override
    public int getCount() {
        return noteCards.size();
    }

    @Override
    public Object getItem(int position) {
        return noteCards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return noteCards.get(position).getView();
    }

    // Remove a note from the card scroller
    public void removeNote(int position) {
        noteCards.remove(position);
        updateNoteCountIndicator();
        notifyDataSetChanged();
    }

    // updates the numbering on the note cards
    public void updateNoteCountIndicator() {
        for (int i = 0; i < noteCards.size(); i++) {
            noteCards.get(i).setFootnote("Note " + (i + 1) + " of " + noteCards.size());
        }
    }

    @Override
    public int getPosition(Object arg0) {
        return 0;
    }
}
