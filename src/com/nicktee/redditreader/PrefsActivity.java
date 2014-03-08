package com.nicktee.redditreader;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.services.DatabaseManager;
import com.nicktee.redditreader.utils.JsonUtils;

public class PrefsActivity extends DialogFragment {

	ArrayList mSelectedItems = new ArrayList();
	ArrayList mNotSelectedItems = new ArrayList();

	DatabaseManager db = DatabaseManager.getInstance();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		List<Prefs> prefsList = new ArrayList<Prefs>();
		
		// Get the prefs from the DB
		try {
			prefsList = db.getAllPrefs();
			Log.v("DB",
					prefsList.get(0).getSubReddits() + ", Selected: "+ prefsList.get(0).getSelected()  + " | size:"
							+ Integer.toString(prefsList.size()));
		} catch (Exception e) {
			Log.v("DB", "Epic fail getting prefs");
		}

		// set up data to pass into the dialog
		List<String> parsePrefsTitle = new ArrayList<String>();
		List<Boolean> parsePrefsSelected = new ArrayList<Boolean>();
		for (Prefs s : prefsList) {
			parsePrefsTitle.add(s.getSubReddits());
			parsePrefsSelected.add((boolean) s.getSelected());
		}
		final CharSequence[] items = parsePrefsTitle
				.toArray(new CharSequence[parsePrefsTitle.size()]);
		final boolean[] isItemSelectedArray = JsonUtils
				.toPrimitiveArray(parsePrefsSelected);

		// Set up the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Subreddits")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(items, isItemSelectedArray,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(which);
									if (mNotSelectedItems.contains(which)){
										mNotSelectedItems.remove(Integer
												.valueOf(which));	
									}
									
								} else {
									// Else, if the item is already in the
									// array, remove it
									if (mSelectedItems.contains(which)){
										mSelectedItems.remove(Integer
												.valueOf(which));	
									}
									// also tell the db to remove it
									mNotSelectedItems.add(which);
								}
								
								
							}
						})
				// Set the action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
			
						DatabaseManager db = DatabaseManager.getInstance();
						
						// save selected items to database
						for (Object o : mSelectedItems){
							String selected = o.toString();
							selected = items[Integer.parseInt(selected)].toString();
							Log.v("S", "Save as selected : " + selected );
							Prefs newPref = new Prefs();
							newPref.setSelected(true);
							newPref.setSubReddits(selected);
							db.updatePref(newPref);
						}
						
						// remove selected items from database
						for (Object o : mNotSelectedItems){
							String notSelected = o.toString();
							notSelected = items[Integer.parseInt(notSelected)].toString();
							Log.v("S", "Save as NOT selected : " + notSelected);
							Prefs newPref = new Prefs();
							newPref.setSelected(false);
							newPref.setSubReddits(notSelected);
							db.updatePref(newPref);
						}
						
						// restart main activity with new subreddit to be shown
						Intent i = new Intent(getActivity().getApplicationContext(), MainActivity_.class);
						startActivity(i);
						
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

							}
						});

		
		return builder.create();
		
	}

	
	
}
