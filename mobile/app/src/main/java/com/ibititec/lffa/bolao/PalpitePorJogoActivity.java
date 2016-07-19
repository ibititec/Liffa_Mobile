package com.ibititec.lffa.bolao;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.helpers.HttpHelper;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.modelo.Partida;
import com.ibititec.lffa.modelo.Usuario;

import java.io.IOException;

public class PalpitePorJogoActivity extends AppCompatActivity {
    private Button btnSalvar;
    private TextView nomeTimePalpitePorJogoMandante, nomeTimePalpitePorJogoVisitante;
    private EditText txtGolsTimeMandante, txtGolsTimeVisitante;
    private SimpleDraweeView escudoMandante, escudoVisitante;
    private String jogosBolao, divisao;
    private Partida partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palpite_por_jogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lerIntent();
        carregarComponentes();
        executarAcoes();
    }

    private void iniciarAppodeal() {
        try {
            Appodeal.setBannerViewId(R.id.appodealBannerView_palpiteporjogo);
            Appodeal.show(this, Appodeal.BANNER);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: iniciarAppodeal: " + ex.getMessage());
        }
    }

    private void lerIntent() {
        iniciarAppodeal();
        Intent intent = getIntent();
        divisao = intent.getStringExtra("divisao");
        partida = (Partida) intent.getSerializableExtra("jogo_bolao");
        if (!HttpHelper.existeConexao(this)) {
            exibirMensagem("Não identificado conexão com a internet, verifique sua conexão está ativa.", "Atenção");

        }

    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent();
            intent.putExtra("divisao", divisao);

            // add data to Intent
            setResult(PalpitePorJogoActivity.RESULT_OK, intent);
            Appodeal.show(this, Appodeal.BANNER_TOP);
            super.onBackPressed();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: onBackPressedPrimeiraDivisaoTabela: " + ex.getMessage());
        }
    }

    private void executarAcoes() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    salvarPlapiteBolao();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void salvarPlapiteBolao() throws IOException {
        partida.setGolMandante(txtGolsTimeMandante.getText().toString());
        partida.setGolVisitante(txtGolsTimeVisitante.getText().toString());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PalpitePorJogoActivity.this);

        Usuario usuarioLogado = JsonHelper.getObject(sharedPreferences.getString(MainActivity.USUARIO + ".json", ""), Usuario.class);
        String jsonPartida = JsonHelper.objectToJson(partida);
        donwnloadFromUrl("", getString(R.string.url_salvar_palpite_bolao), jsonPartida.substring(0, jsonPartida.length() - 1) + ", \"emailUsuario\":\"" + usuarioLogado.getLoginEmail() + "\",\"senha\":\"" + usuarioLogado.getSenha() + "\"}");
    }

    private void exibirMensagem(String mensagem, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(mensagem);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
                // Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void exibirMensagemOK(String mensagem, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(mensagem);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                onBackPressed();
                // Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }
    private void carregarComponentes() {
        btnSalvar = (Button) findViewById(R.id.btnSalvarPalpiteBolao);
        txtGolsTimeMandante = (EditText) findViewById(R.id.txtBolaoGolsTimeMandante);
        txtGolsTimeVisitante = (EditText) findViewById(R.id.txtBolaoGolsTimeVisitante);
        escudoMandante = (SimpleDraweeView) findViewById(R.id.img_time_mandante_palpite);
        escudoVisitante = (SimpleDraweeView) findViewById(R.id.img_time_visitante_palpite);
        nomeTimePalpitePorJogoMandante = (TextView) findViewById(R.id.nomeTimePalpitePorJogoMandante);
        nomeTimePalpitePorJogoVisitante = (TextView) findViewById(R.id.nomeTimePalpitePorVisitante);

        Uri imageUriMandante = Uri.parse(MainActivity.PATH_FOTOS + partida.getEscudoPequenoMandante().trim());
        Uri imageUriVisitante = Uri.parse(MainActivity.PATH_FOTOS + partida.getEscudoPequenoVisitante().trim());

        nomeTimePalpitePorJogoMandante.setText(partida.getTimeMandante());
        nomeTimePalpitePorJogoVisitante.setText(partida.getTimeVisitante());

        escudoMandante.setImageURI(imageUriMandante);
        escudoVisitante.setImageURI(imageUriVisitante);
    }

    private void donwnloadFromUrl(final String nomeJsonParam, String urlJson, final String parametro) {
        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PalpitePorJogoActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;
                try {
                    String url = params[0];
                    //json = HttpHelper.POSTJson(url, parametro);

                    if (!HttpHelper.existeConexao(PalpitePorJogoActivity.this)) {
                        exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção");
                    } else {
                        json = HttpHelper.POSTJson(getString(R.string.url_salvar_palpite_bolao), parametro);

                    }

                    Log.i(MainActivity.TAG, json);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(MainActivity.TAG, String.format(getString(R.string.msg_erro_json), e.getMessage()));
                }
                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                progressDialog.dismiss();

                if (json.equals("OK")) {
                    exibirMensagemOK("Palpite salvo com sucesso.", "Sucesso");

                } else {
                    exibirMensagem("Erro", "Não foi possível salvar seu palpite, tente novamente.");
                }
            }
        }).execute(urlJson);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        onBackPressed();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.home) {
//            onBackPressed();
//            return true;
//        }
        return true;
    }
}
