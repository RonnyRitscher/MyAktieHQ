package com.example.myaktiehq;



import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                holeDatenTask.execute("Aktie");

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

            String[] ergebnisArray = new String[20]; //HardCoded StringArray

            for(int i=0 ; i<20 ; i++ ){

                // Den StringArray füllen wir mit Beispieldaten
                ergebnisArray[i] = strings[0] + "_" + (i+1);

                // Alle 5 Elemente geben wir den aktuellen Fortschritt bekannt
                if(i%5 == 4){
                    //publishProgress( Fortschritt , MaximalWert) -> wird in onProgressUpdate() definiert/ausgeführt
                    publishProgress(i+1 , 20);
                }

                // Mit Thread.sleep(600) simulieren wir eine Wartezeit von 600 ms
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    //e.printStackTrace mit LOG-message Log.e(TAG,message,exception) getauscht
                    Log.e(TAG, "doInBackground: ERROR ", e );
                }
            }

            return ergebnisArray ;
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
    }

    //-------------------------------------------------------------------------
}
