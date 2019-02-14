package com.example.myaktiehq;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;


public class EinstellungenActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "Einstellungen wurde gestartet", Toast.LENGTH_SHORT).show(); //KANN ENTFERNT WERDEN
        addPreferencesFromResource(R.xml.preferences);          //!DEPRICATED
        Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key)); // identifiziert den Key der Pref
        aktienlistePref.setOnPreferenceChangeListener(this);    // übergibt den identifizierten Key an onPreferenceChange()

        //-> onPreferenceChange sofort aufrufen/anzeige mit der in SharedPreferences gespeicherten Aktienliste
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this); // holt den default
        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(), ""); //

        onPreferenceChange(aktienlistePref, gespeicherteAktienliste); // ruft die Methode oPC mit Parametern auf
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        preference.setSummary(newValue.toString());             // Summary ist der wert/value von "preference_aktienliste_default"
                                                                // ... da vom typ Objekt der Aufruf von toString()
                                                                //... zeigt den eingetragenen Value (kleiner, unterhalb) des Keys an
        return true;
    }
}
