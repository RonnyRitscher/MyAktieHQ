package com.example.myaktiehq;
// Projekt AktienAnzeigen
// ... http://www.programmierenlernenhq.de/tutorial-android-studio-projekt-erstellen/xxxx

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /* Nur für die LOG-Informationen*/
    private static final String TAG = MainActivity.class.getSimpleName();

    /* SharedPreferences */
    private EditText userName;
    //------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* LOG - Informationen*/
        Log.d(TAG, "onCreate: View ist fertig gebaut!");


        /*
        //SHARED-PREFERENCES
        userName = findViewById(R.id.editText_userName);
        //Shared-Pref Datei öffnen
        SharedPreferences userSP = getSharedPreferences("UserPrefFile" , 0);
        //Schlüsselwerte aud der UserPrefFile in Textfelder
        userName.setText(userSP.getString("userName" , "Gast"));
        */
    }
    //------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: kurz vor sichtbar werden!");
    }

    //------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: für interaktionen bereit! -> (Wieder-)Aufnahme der Activity");
    }


    //------------------------------------------------------------------

    @Override
    protected void onPause() {

        Log.d(TAG, "onPause: kurz vor in den Hintergrund gehen -> zum speichern von Änderungen des Benutzers -> (wird aufgerufen bevor eine andere Activity startet)");
    }


    //------------------------------------------------------------------
    
    //Methode zum einbinden des erstellten Menu-Items in dem Header-Bereich (...)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate() verwenden, um das menu einzubinden
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Methode zum anklicken der MenuItems (Menu hat einen listener)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Toast.makeText(this, "Settings wurde gedrückt", Toast.LENGTH_SHORT).show();

                /*AUSGEBLENDET - KANN GGF GELÖSCHT WERDEN!
                //TextView textViewSettings = findViewById(R.id.fragment_main_textView);
                //textViewSettings.setText("GEÄNDERT - Einstellung wird angezeigt");
                */

                // einbinden des Intent für die Menü-Weiterleitung zu den Einstellungen
                startActivity(new Intent(this, EinstellungenActivity.class));

                return true;

            case R.id.action_profile:
                Toast.makeText(this, "Profil wurde gedrückt", Toast.LENGTH_SHORT).show();

                /*AUSGEBLENDET - KANN GGF GELÖSCHT WERDEN!
                TextView textViewProfile = findViewById(R.id.fragment_main_textView);
                textViewProfile.setText("GEÄNDERT - Profil wird angezeigt");
                */

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        /*
        //SHARED-PREFERENCES -> speichern der eingegebenen Daten
        //Shared-Pref Datei öffnen
        SharedPreferences userSP = getSharedPreferences("UserPrefFile" , 0);
        //Editor Klasse initialisieren (Umweg da direkter Zugriff nicht möglich ist)
        SharedPreferences.Editor spEdior = userSP.edit();
        //Text aus den (oberen) Textfeldern holen und in unsere Editor-Instanz schreiben
        spEdior.putString("userName" , userName.getText().toString() );
        //Speichern
        spEdior.commit();
        */

    }
}
