

package com.glasskit.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * Checks if there exists an object in shared preferences with the chosen key
     */
    public static boolean checkForObjectInSharedPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void commitNewNoteList(Context context, String key, List<String> notesList) {
        final SharedPreferences.Editor sharedPrefEditor =
            PreferenceManager.getDefaultSharedPreferences(context).edit();

        if (!notesList.isEmpty()) {
            sharedPrefEditor.putString(key, new JSONArray(notesList).toString());
        } else {
            sharedPrefEditor.putString(key, null);
        }

        sharedPrefEditor.apply();
    }

    /**
     * Gets the arraylist from shared preferences
     */
    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String json = sharedPref.getString(key, null);
        final ArrayList<String> notesList = new ArrayList<>();

        if (json != null) {
            try {
                final JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    notesList.add(a.optString(i));
                }
            } catch (JSONException ignored) {
            }
        }

        return notesList;
    }

    /**
     * Deletes an item from the list stored in shared preferences
     */
    public static void deleteNoteAtIndex(int index, Context context, String key) {
        final ArrayList<String> notesList = new ArrayList<>(getStringArrayPref(context, key));
        notesList.remove(index);
        commitNewNoteList(context, key, notesList);
    }
}
