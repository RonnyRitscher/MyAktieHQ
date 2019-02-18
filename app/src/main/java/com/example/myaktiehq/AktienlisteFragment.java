package com.example.myaktiehq;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AktienlisteFragment extends Fragment {

    //FELDER
    /*LOGGING  -  TESTEN ob etwas vohenden ist - siehe unten*/
    public static final String TAG = AktienlisteFragment.class.getSimpleName();
    private ArrayAdapter<String> mAktienListeAdapter;

    /* default Konstruktor */
    public AktienlisteFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //-------------------------------------------------------------------------
        /*LOGGING  -  TESTEN ob etwas vohenden ist*/
        Log.d(TAG, "onCreateView: Debugmeldung " + getActivity().toString());
        Log.e(TAG, "onCreateView: Errormeldung ");

        //-------------------------------------------------------------------------
        /*Dummy-Daten für die ListView über eine ArrayList<>*/
        String [] aktienlisteArray = {
                "Adidas - Kurs: 73,45 €",
                "Allianz - Kurs: 145,12 €",
                "BASF - Kurs: 84,27 €",
                "Bayer - Kurs: 128,60 €",
                "Beiersdorf - Kurs: 80,55 €",
                "BMW St. - Kurs: 104,11 €",
                "Commerzbank - Kurs: 12,47 €",
                "Continental - Kurs: 209,94 €",
                "Daimler - Kurs: 84,33 €"
        };
        List<String> aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));

        /*ADAPTER für die Liste erzeugen  - über Refactoring*/
        mAktienListeAdapter = new ArrayAdapter<>(
                getActivity() ,                         // Container - die View - wo kommt es her?
                R.layout.list_item_aktienliste ,        // Aussehen - wie sollen die Einträge aussehen?
                R.id.list_item_aktienliste_textview,    // Ausgabe - Wo kommt der einzelne Eintrag her?
                aktienListe                             // Welche ArrayList - Beispieldaten in der ArrayList

        );


        // ---> Erstellen des ListViews und Zuweisen des ArrayAdapters und Layouts: <---
        // View root erzeugen und zuweisen des Layouts mit dem Inflater:
        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

        // Ansprechen der findById() NUR über den rootView möglich:
        ListView aktienlisteListView =  rootView.findViewById(R.id.listview_aktienliste);

        //ADAPTER heransetzen:
        aktienlisteListView.setAdapter(mAktienListeAdapter);

        // T10 - Der Listview einen setOnItemClickListener
        aktienlisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aktieninfo = (String) parent.getItemAtPosition(position);

                // EXPLIZITER intent um eine NEUE ACTIVITY aufzurufen/zu starten:
                // -> new Intent(wo kommt es her, class wo soll es hin);
                Intent aktiendetailIntent = new Intent(getActivity(), AktiendetailActivity.class);
                aktiendetailIntent.putExtra(Intent.EXTRA_TEXT , aktieninfo);

                startActivity(aktiendetailIntent);  //ruft die De
            }
        });


        /*als Returntyp muss über inflater das Layout angegeben werden */
        return rootView;
    }

    //-------------------------------------------------------------------------
    // O P T I O N - M E N U und A C T I O N - B A R
    // Hier teilen wir dem Fragment beim Erstellen mit, dass es ein Options Menu besitzen wird:
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menü bekannt geben, dadurch kann unser Fragment Menü-Events verarbeiten
        // erweitert das vorhandene Menu-Item wenn das Fragment "aktienlisteFragment" angezeigt wird
        setHasOptionsMenu(true);
    }

    //Hier füllen (inflate) wir das Options Menu mit dem Menüeintrag, den wir in der XML-Datei menu_aktienlistefragment.xml definiert haben.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_aktienlistefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_daten_aktualisieren:
                // Methode zum aktualisieren der Daten
                // Erzeugen einer Instanz von HoleDatenTask und starten des asynchronen Tasks
                HoleDatenTask holeDatenTask = new HoleDatenTask();


                // Auslesen der ausgewählten Aktienliste aus den SharedPreferences
                SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());    // holt den DefaultSharedPref der aktuellen Activity über PrefManager
                String prefAktienlisteKey = getString(R.string.preference_aktienliste_key) ;                // holt den Key von preference.xml
                String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);         // holt den Default-Value von preference.xml
                String akliste = sPrefs.getString(prefAktienlisteKey, prefAktienlisteDefault); //  ,ODER,   sPrefs.getString(gesetzter/hinterlegter Wert , oder wenn nicht vorhanden den Default-Wert);

                // Auslesen des Anzeige-Modus aus den SharedPreferences
                String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
                Boolean indizemodus = sPrefs.getBoolean(prefIndizemodusKey, false);

                // Starten des asynchronen Tasks und Übergabe der Aktienliste
                if (indizemodus) {
                    String indizeliste = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";

                    // Aufruf über der ausgewählten Indizeliste
                    holeDatenTask.execute(indizeliste);
                }else{
                    // Aufruf über der ausgewählten Aktienliste
                    holeDatenTask.execute(akliste);
                }




                // Den Benutzer informieren, dass neue Aktiendaten im Hintergrund abgefragt werden
                // this geht nicht in einem Fragment -> wir nutzen getActivity()
                Toast.makeText(getActivity(), "Aktualisierung gedrückt", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------------------------------
    // A S Y N C - T A S K
    // Innere Klasse HoleDatenTask führt den asynchronen Task auf eigenem Arbeitsthread aus (Innere Klasse)
    // AsyncTask<Eingabewert, Fortschritt, Rückgabewert >
    public class HoleDatenTask extends AsyncTask<String, Integer, String[]>{


        private final String TAG = HoleDatenTask.class.getSimpleName(); //LOG-TAG-String //kein static möglich

        @Override
        protected String[] doInBackground(String... strings) {
//            AUSKOMMENTIERT! NUR ZU DUMMY-ZWECKEN
//            String[] ergebnisArray = new String[20]; //HardCoded StringArray
//
//            for(int i=0 ; i<20 ; i++ ){
//
//                // Den StringArray füllen wir mit Beispieldaten
//                ergebnisArray[i] = strings[0] + "_" + (i+1);
//
//                // Alle 5 Elemente geben wir den aktuellen Fortschritt bekannt
//                if(i%5 == 4){
//                    //publishProgress( Fortschritt , MaximalWert) -> wird in onProgressUpdate() definiert/ausgeführt
//                    publishProgress(i+1 , 20);
//                }
//
//                // Mit Thread.sleep(600) simulieren wir eine Wartezeit von 600 ms
//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e) {
//                    //e.printStackTrace mit LOG-message Log.e(TAG,message,exception) getauscht
//                    Log.e(TAG, "doInBackground: ERROR ", e );
//                }
//            }
//            return ergebnisArray
//            AUSKOMMENTIERT! NUR ZU DUMMY-ZWECKEN

            if(strings.length == 0){
                return null;
            }

            // Exakt so muss die Anfrage-URL an unseren Web-Server gesendet werden:
            // http://www.programmierenlernenhq.de/tools/query.php?s=DAI.DE,BMW.DE
            // Wir konstruieren die Anfrage-URL für unseren Web-Server
            final String URL_PARAMETER = "http://www.programmierenlernenhq.de/tools/query.php";

            // symbols = "DAI.DE,BMW.DE";  -> ...geändert...
            String symbols = strings[0];

            String anfrageString = URL_PARAMETER + "?s=" + symbols;
            Log.d(TAG, "doInBackground: anfrageString -> " + anfrageString);            //TEST ob string richtig zusammengesetzt ist

            // Die URL-Verbindung und der BufferedReader, werden im finally-Block geschlossen
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            // In diesen String speichern wir die Aktiendaten im XML-Format
            String aktiendatenXmlString = "";                               //Platzhalter für unsere AktienDaten

            //VERBINDEN
            try {
                URL url = new URL(anfrageString);

                // Aufbau der Verbindung zur YQL Platform
                httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                if(inputStream == null){                                    // Keinen Aktiendaten-Stream erhalten, daher Abbruch
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null ){
                    aktiendatenXmlString += line +"\n";
                }

                if (aktiendatenXmlString.length() == 0) {                   // Keine Aktiendaten ausgelesen, Abbruch
                    return null;
                }

                Log.d(TAG, "Aktiendaten XML-String: " + aktiendatenXmlString);
                publishProgress(1,1);

            }catch (IOException ioe){   // Beim Holen der Daten trat ein Fehler auf, daher Abbruch
                Log.e(TAG, "Error ", ioe);
                return null;

            }finally { // Schließen der Connection
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException ioe) {
                        Log.e(TAG, "doInBackground: ERROR", ioe);
                    }
                }
            }

            //PARSEN und AUSLESEN des XML-STREAMS  ... sie he Methode leseXmlAktiendatenAus()
            String[] ergebnis = leseXmlAktiendatenAus(aktiendatenXmlString);

            return ergebnis;
        }

        @Override
        protected void onPreExecute() { //Sachen VOR der Ausführung - zb vorbereitung einer ProzessBar
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) { //Sachen NACH der Ausführung - zb Arrayadapter mit den neuen Daten bestücken

            // Wir löschen den Inhalt des ArrayAdapters und fügen den neuen Inhalt ein
            // Der neue Inhalt ist der Rückgabewert von doInBackground(String...) also
            // der StringArray gefüllt mit Beispieldaten
            if( strings != null){
                mAktienListeAdapter.clear(); //zurücksetzen des Adapters
                // Bestücken des Adapters
                for(String aktienString : strings){
                    mAktienListeAdapter.add(aktienString);
                }
            }
            Toast.makeText(getActivity(), "Daten wurden vollständig geladen...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), values[0]+ " von " +values[1]+"geladen", Toast.LENGTH_SHORT).show();
        }

        private String[] leseXmlAktiendatenAus(String xmlString){
            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                //Bauen des Documents
                doc = db.parse(is);

            } catch (ParserConfigurationException pce) {
                Log.e(TAG, "leseXmlAktiendatenAus: ERROR - ParserConfigurationException ", pce);
                return null;
            } catch (SAXException saxe) {
                Log.e(TAG, "leseXmlAktiendatenAus: ERROR - SAXException ", saxe);
                return null;
            } catch (IOException ioe) {
                Log.e(TAG, "leseXmlAktiendatenAus: ERROR - IOException ", ioe);
                return null;
            }
            //Das Element holen
            Element xmlAktiendaten = doc.getDocumentElement();
            NodeList aktienListe = xmlAktiendaten.getElementsByTagName("row");

            int anzahlAktien = aktienListe.getLength();
            int anzahlAktienParameter = aktienListe.item(0).getChildNodes().getLength();

            String[] ausgabeArray = new String[anzahlAktien];    //Dieses Array soll bestückt zurückgegeben werden
            String[][] alleAktienDatenArray = new String[anzahlAktien][anzahlAktienParameter];

            Node aktienParameter;
            String aktienParameterWert;

            for(int i=0 ; i<anzahlAktien ; i++){
                NodeList aktienParameterListe = aktienListe.item(i).getChildNodes();

                for(int j=0 ; j<anzahlAktienParameter ; j++ ){
                    aktienParameter = aktienParameterListe.item(j);
                    aktienParameterWert = aktienParameter.getFirstChild().getNodeValue();
                    alleAktienDatenArray[i][j] = aktienParameterWert;
                }

                ausgabeArray[i]  = alleAktienDatenArray[i][0];                // symbol
                ausgabeArray[i] += ": " + alleAktienDatenArray[i][4];         // price
                ausgabeArray[i] += " " + alleAktienDatenArray[i][2];          // currency
                ausgabeArray[i] += " (" + alleAktienDatenArray[i][8] + ")";   // percent
                ausgabeArray[i] += " - [" + alleAktienDatenArray[i][1] + "]"; // name

                Log.d(TAG, "leseXmlAktiendatenAus: " + ausgabeArray[i]);
            }

            return ausgabeArray;
        }
    }

    //-------------------------------------------------------------------------
}
