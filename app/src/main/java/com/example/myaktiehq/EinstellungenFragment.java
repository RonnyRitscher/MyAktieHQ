package com.example.myaktiehq;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

//Wir verwenden PreferenceFragmentCompat statt PreferenceFragment
public class EinstellungenFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    //eingefügt von EinstellungenActivity
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return false;
    }


    @Override
    public void onCreatePreferences(Bundle saveInstanceState, String rootKey) {
        //!GEÄNDERT! addPreferencesFromResource(R.xml.preferences);          //!DEPRICATED
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key));    // identifiziert den Key der Pref
        aktienlistePref.setOnPreferenceChangeListener(this);                                            // übergibt den identifizierten Key an onPreferenceChange()

        //-> onPreferenceChange sofort aufrufen/anzeige mit der in SharedPreferences gespeicherten Aktienliste
        //!GEÄNDERT! SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this); // holt den default
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());   // holt den default
        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(), "");
        onPreferenceChange(aktienlistePref, gespeicherteAktienliste);                                   // ruft die Methode oPC mit Parametern auf


    }
}
