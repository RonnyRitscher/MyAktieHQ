package com.example.myaktiehq;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.security.PrivateKey;
/*
impliziter vs expliziter Intent :
    impleziter Intent -> ruft eine unbekannte Activity auf dem Gerät auf
    expliziter Intent -> ruft eine bekannte Activity aus unserer App auf
 */

public class AktiendetailActivity extends AppCompatActivity {

    //Zum Internen Test
    private static final String TAG = AktiendetailActivity.class.getSimpleName();

    //Benötigte Elemente, Felder ,etc...
    private String aktienInfo;
    private String symbol;

    private String webseitenURL;
    private Uri webseitenUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktiendetail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Menu -> Fügt die Items in der ActionBar hinzu
        getMenuInflater().inflate(R.menu.menu_aktiendetail, menu);

        //SHARED-ACTION-PROVIDER:
        // Wir lesen aus dem empfangenen Intent die übermittelten Daten aus
        aktienInfo = "";
        Intent empfangenerIntent = this.getIntent();

        if (empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            // wählt den ausgewählten Eintrag aus der aktienlisteFragment
            aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
        }

        // Holt das Menüeintrag-Objekt, das dem ShareActionProvider zugeordnet ist
        MenuItem shareMenuItem = menu.findItem(R.id.action_teile_aktiendaten);

        // Holt den ShareActionProvider über den Share-Menüeintrag
        // Der CAST ist erforderlich, da wie die android.support.v7.widget.ShareActionProvider verwenden
        ShareActionProvider sAP;
        sAP = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);

        // Erzeugen des SEND-Intents mit den Aktiendaten als Text
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        //noinspection deprecation
        // -> .addFlags() verhindern wir, dass die aufzurufende Share-Activity in den Activity Stack aufgenommen wird.
        // -> wenn die App in den Hintergrung läuft, soll wieder die App aufgerufen werden
        // -> Dadurch wird später immer unsere App geöffnet, wenn das Symbol unserer App geklickt wurde und nicht unter Umständen die Activity einer anderen App.
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        // Da Aktiendaten in einem normalen String gespeichert wurden ...
        shareIntent.setType("text/plain");

        // fügt dem shareIntent die AktienInfos hinzu
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Daten zu: " + aktienInfo);

        // Der SEND-Intent wird an den ShareActionProvider angehangen
        // -> überprüfung ob in dem ShareActionProvider Daten vorhanden sind
        if (sAP != null ) {
            sAP.setShareIntent(shareIntent);
        } else {
            Log.d(TAG, "Kein Provider vorhanden!");
            Toast.makeText(this, "Kein Provider vorhanden!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings wurde gedrückt", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_profile:
                Toast.makeText(this, "Profile wurde gedrückt", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_starte_browser:
                Toast.makeText(this, "Starte Browser wurde gedrückt", Toast.LENGTH_SHORT).show();
                //Methode zum offnen des Browsers
                zeigeWebseiteImBrowser();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    //-------------------------------------------------------------------
    // IMPLIZIETE INTENTS
    // -> ANZEIGEN WEITERER DETAILS IM BROSER
    public void zeigeWebseiteImBrowser(){


        Intent empfangenerIntent = getIntent();

        // Die AktiendetailActivity wurde über einen Intent aufgerufen
        // Wir lesen aus dem empfangenen Intent die übermittelten Daten aus
        // und bauen daraus die URL der aufzurufenden Webseite
        if(empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)){
            //
            String aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);

            int pos = aktienInfo.indexOf(":");
            String symbol = aktienInfo.substring(0, pos);
            webseitenURL  = "http://finance.yahoo.com/q?s=" + symbol;
        }

        // Erzeugen des Data-URI Scheme für die anzuzeigende Webseite
        // -> bastelt die URI aus der webseitenURL zusammen
        webseitenUri = Uri.parse(webseitenURL);

        // Erzeugen eines IMPLIZITEN View-Intents mit der Data URI-Information
        // -> rufe eine ACTION-VIEW auf, die mit der webseitenURI umgehen kann
        Intent intent = new Intent(Intent.ACTION_VIEW, webseitenUri);

        // Prüfen mit der .resolveActivity() ob eine Web-App auf dem Android Gerät installiert ist
        // und Starten der App mit dem oben erzeugten impliziten View-Intent
        // -> in unserem Fall den Crome-Browser
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startet
            startActivity(intent);
        } else {
            // Wenn keine WebApp vorhanden, gib eine Info im Logcat und einen Toast aus:
            Log.d(TAG, "Keine Web-App installiert!");
            Toast.makeText(this, "Keine Web-App installiert!", Toast.LENGTH_SHORT).show();
        }

    }
    //-------------------------------------------------------------------
}
