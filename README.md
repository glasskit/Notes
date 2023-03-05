# Notes
This is a small notes app for Google Glass based on the notes app from [open quartz](https://github.com/jaredsburrows/open-quartz).

## Changes
1. The LiveCard now only displays the newest note instead of a numbered list of all created notes.
2. If more then one note is created the card on the home screen will show a stack symbol.
3. The app now uses the built-in glass command "Take a note"

## Download
To download this to your Glass, first [download the apk](https://github.com/GlassKit/Notes/releases/download/v1.0.0/Notes.apk). Then run the following commands:

```
cd Downloads
adb install Notes.apk
```

Now you should have the app!

## Testing
To make sure the app is working:

1. Wake up your Glass
2. Say "OK Glass"
3. Say "Take a note"
4. Glass will prompt you with a voice input screen for your new note

The new note should be displayed on your home screen.
