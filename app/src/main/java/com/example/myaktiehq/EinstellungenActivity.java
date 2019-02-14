package com.example.myaktiehq;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class EinstellungenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //Toast.makeText(this, "Einstellungen wurde gestartet", Toast.LENGTH_SHORT).show(); //KANN ENTFERNT WERDEN
//
//        addPreferencesFromResource(R.xml.preferences);          //!DEPRICATED
//        Preference aktienlistePref = findPreference(getString(R.string.preference_aktienliste_key)); // identifiziert den Key der Pref
//        aktienlistePref.setOnPreferenceChangeListener(this);    // übergibt den identifizierten Key an onPreferenceChange()
//
//        // ...onPreferenceChange sofort aufrufen/anzeige mit der in SharedPreferences gespeicherten Aktienliste
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this); // holt den default
//        String gespeicherteAktienliste = sharedPrefs.getString(aktienlistePref.getKey(), ""); //
//        onPreferenceChange(aktienlistePref, gespeicherteAktienliste); // ruft die Methode oPC mit Parametern auf

        // Hier binden wir das EinstellungsFragment in der Activity ein
        // ...nicht den  getFragmentManager(), da wir über "extends AppCompatActivity" arbeiten
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new EinstellungenFragment())
                .commit();

    }


//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//        preference.setSummary(newValue.toString());             // Summary ist der wert/value von "preference_aktienliste_default"
//                                                                // ... da vom typ Objekt der Aufruf von toString()
//                                                                //... zeigt den eingetragenen Value (kleiner, unterhalb) des Keys an
//        return true;
//    }
}
