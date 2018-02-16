package wmf.qrcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;

import android.os.StrictMode;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ToggleButton;

import java.util.Locale;

/**
 * {@inheritDoc}
 */
public class OperaActivity extends Activity {

    private ToggleButton read_btn;
    private TextToSpeech tts = null;
    /**
     * {@inheritDoc}
     */
    public String titolo = null;
    /**
     * {@inheritDoc}
     */
    public String categoria = null;
    /**
     * {@inheritDoc}
     */
    public String autore = null;
    /**
     * {@inheritDoc}
     */
    public String descrizione = null;
    /**
     * {@inheritDoc}
     */
    public String immagine = null;
    /**
     * {@inheritDoc}
     */
    public String video = null;
    /**
     * {@inheritDoc}
     */
    public String proprietario = null;
    /**
     * {@inheritDoc}
     */
    public String materiali = null;
    /**
     * {@inheritDoc}
     */
    public String tecnica = null;
    /**
     * {@inheritDoc}
     */
    public String periodo_storico = null;
    /**
     * {@inheritDoc}
     */
    public String dimensioni = null;
    /**
     * {@inheritDoc}
     */
    public String peso = null;
    /**
     * {@inheritDoc}
     */
    public String movimento_artistico = null;
    /**
     * {@inheritDoc}
     */
    public String valore = null;
    /**
     * {@inheritDoc}
     */
    public String restaurato = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opera);
        String id = getIntent().getStringExtra("id");


        try {
            boolean flag = false;
            JSONArray json = readJsonFromUrl();
            int jsonLength = json.length();
            for (int i = 0; i < jsonLength; i++) {
                if (json.getJSONObject(i).getString("id_opera").equals(id)) {
                    titolo = json.getJSONObject(i).getString("titolo");
                    categoria = json.getJSONObject(i).getString("categoria");
                    autore = json.getJSONObject(i).getString("autore");
                    descrizione = json.getJSONObject(i).getString("descrizione");
                    immagine = json.getJSONObject(i).getString("immagine");
                    video = json.getJSONObject(i).getString("video");
                    proprietario = json.getJSONObject(i).getString("proprietario");
                    materiali = json.getJSONObject(i).getString("materiali");
                    tecnica = json.getJSONObject(i).getString("tecnica");
                    periodo_storico = json.getJSONObject(i).getString("periodo_storico");
                    dimensioni = json.getJSONObject(i).getString("dimensioni");
                    peso = json.getJSONObject(i).getString("peso");
                    movimento_artistico = json.getJSONObject(i).getString("movimento_artistico");
                    valore = json.getJSONObject(i).getString("valore");
                    restaurato = json.getJSONObject(i).getString("restaurato");
                    flag = true;
                }


            }
            if (flag == false) {
                Toast.makeText(this, "Nessuna opera del museo è associata al QR code letto", Toast.LENGTH_LONG).show();
                finish();
            } else {


                TextView titoloView = findViewById(R.id.titolo);
                TextView categoriaView = findViewById(R.id.categoria);
                TextView autoreView = findViewById(R.id.autore);
                TextView descrizioneView = findViewById(R.id.descrizione);
                ImageView immagineView = findViewById(R.id.immagine);
                TextView proprietarioView = findViewById(R.id.proprietario);
                TextView materialiView = findViewById(R.id.materiali);
                TextView tecnicaView = findViewById(R.id.tecnica);
                TextView periodo_storicoView = findViewById(R.id.periodo_storico);
                TextView dimensioniView = findViewById(R.id.dimensioni);
                TextView pesoView = findViewById(R.id.peso);
                TextView movimento_artisticoView = findViewById(R.id.movimento_artistico);
                TextView valoreView = findViewById(R.id.valore);
                TextView restauratoView = findViewById(R.id.restaurato);

                titoloView.setText(titolo);
                categoriaView.setText(categoria);
                autoreView.setText(autore);
                descrizioneView.setText(descrizione);
                proprietarioView.setText(proprietario);
                materialiView.setText(materiali);
                tecnicaView.setText(tecnica);
                periodo_storicoView.setText(periodo_storico);
                dimensioniView.setText(dimensioni);
                pesoView.setText(peso);
                movimento_artisticoView.setText(movimento_artistico);
                valoreView.setText(valore);
                restauratoView.setText(restaurato);
                Glide.with(this)
                        .load(immagine)
                        .into(immagineView);


                read_btn = findViewById(R.id.read_btn);
                read_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        readText(descrizione, view);
                    }
                });
            }

        } catch (Exception e) {
            finish();
        }
    }

    public void onDestroy() {
        if (tts != null) {
            tts.stop();
        }
        super.onDestroy();
    }

    public void onStop() {
        if (tts != null) {
            tts.stop();
            read_btn.setChecked(false);
        }
        super.onStop();
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            read_btn.setChecked(false);
        }
        super.onPause();
    }

    protected void onRestart() {
        if (tts != null) {
            tts.stop();
            read_btn.setChecked(false);
        }
        super.onRestart();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp = 0;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONArray readJsonFromUrl() throws IOException, JSONException, URISyntaxException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        InputStream is = null;
        JSONArray json=null;
        try {
            is = new URL("http://museosmart.altervista.org/MUS/web/index.php?r=opera%2fsend").openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                json = new JSONArray(jsonText);
                return json;
            } catch (Exception e) {
                Toast.makeText(this, "Nessuna opera del museo è associata al QR code letto", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Errore di connessione!", Toast.LENGTH_LONG).show();
        }
        is.close();
        return json;
    }

    public void readText(final String stringa, View view) {
        boolean checked = ((ToggleButton) view).isChecked();
        if (checked) {
            tts = new TextToSpeech(OperaActivity.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.US);
                        if (result == TextToSpeech.LANG_MISSING_DATA ||
                                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("error", "Questa lingua non è supportata");
                        } else {
                            int i = tts.setLanguage(Locale.ITALIAN);
                            tts.speak(stringa, TextToSpeech.QUEUE_ADD, null);

                        }
                    } else
                        Log.e("error", "Inizializzazione fallita!");
                }
            });
        } else {
            tts.stop();
        }
    }


    public void playVideo(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video)));
            Log.i("Video", "Video Playing....");
        } catch (Exception e) {
            Toast.makeText(this, "Nessun video associato all'opera", Toast.LENGTH_LONG).show();
        }
    }
}
