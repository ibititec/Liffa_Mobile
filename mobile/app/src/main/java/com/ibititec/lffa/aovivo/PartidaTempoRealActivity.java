package com.ibititec.lffa.aovivo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.PrimeiraDivisaoTabelaActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.adapter.AdapterAoVivo;
import com.ibititec.lffa.helpers.HttpHelper;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.helpers.UIHelper;
import com.ibititec.lffa.modelo.AoVivo;
import com.ibititec.lffa.modelo.Partida;
import com.ibititec.lffa.modelo.Usuario;
import com.ibititec.lffa.util.AnalyticsApplication;

import java.io.IOException;
import java.util.List;

public class PartidaTempoRealActivity extends AppCompatActivity {
    private Button btnSalvar;
    private TextView nomeTimePalpitePorJogoMandante, nomeTimePalpitePorJogoVisitante;
    private TextView txtGolsTimeMandante, txtGolsTimeVisitante;
    private SimpleDraweeView escudoMandante, escudoVisitante;
    private ListView lvComentarioAoVivo;
    private String jogosBolao, divisao, funcionalidade;
    private Partida partida;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_partida_tempo_real);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            lerIntent();
            carregarComponentes();
            executarAcoes();
            iniciarAppodeal();
            donwnloadFromUrl("aovivo", getString(R.string.url_partida_ao_vivo), "{\"id\": \"" + String.valueOf(partida.getId()) + "\"}");
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: onCreate PartidaTempoReal: " + ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApplication.enviarGoogleAnalitcs(this);
        lerIntent();
        iniciarAppodeal();
    }

    private void iniciarAppodeal() {
        try {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: iniciarAppodeal: " + ex.getMessage());
        }
    }

    private void lerIntent() {
        try {
            if (HttpHelper.existeConexao(this)) {
                Intent intent = getIntent();
                divisao = intent.getStringExtra("divisao");
                partida = (Partida) intent.getSerializableExtra("partida_tempo_real");
                funcionalidade = intent.getStringExtra("funcionalidade");
            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: lerIntent PartidaTempoReal: " + ex.getMessage());
        }
    }

    private void executarAcoes() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialoComentario();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {

            Intent intent = new Intent(this, PrimeiraDivisaoTabelaActivity.class);
            intent.putExtra("divisao", divisao);
            intent.putExtra("funcionalidade", funcionalidade);
            // add data to Intent
            setResult(PrimeiraDivisaoTabelaActivity.RESULT_OK, intent);
            Appodeal.show(this, Appodeal.NATIVE);
            super.onBackPressed();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro OnBack Classificacao : " + ex.getMessage());
        }
    }

    private void abrirDialoComentario() {
        try {
            final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_comentario, null);

            final EditText editComentarioAdicionar = ((EditText) viewDialog.findViewById(R.id.txtComentarioAdicionar));

            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(PartidaTempoRealActivity.this)
                    .setTitle("Comentário")
                            //.setIcon(R.mipmap.ic_launcher_auteasy)
                    .setPositiveButton("SALVAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!editComentarioAdicionar.getText().toString().equals("")) {
                                salvarComentarioBancoRemoto(editComentarioAdicionar.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", null)
                    .setView(viewDialog)
                    .show();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: abrirDialoComentario PartidaTempoReal: " + ex.getMessage());
        }
    }

    private void salvarComentarioBancoRemoto(final String texto) {

        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PartidaTempoRealActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;
                try {
                    String url = params[0];
                    //json = HttpHelper.POSTJson(url, parametro);

                    if (!HttpHelper.existeConexao(PartidaTempoRealActivity.this)) {
                        exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção");
                    } else {
                        json = HttpHelper.POSTJson(url, "{\"id\": \"" + partida.getId() +"\",\"comentario\": \"" + texto + "\"}");
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

                if (json.equals("")) {
                    exibirMensagemOK("Não foi possível enviar o comentário", "Ao Vivo");

                } else {
                    donwnloadFromUrl("aovivo", getString(R.string.url_partida_ao_vivo), "{\"id\": \"" + String.valueOf(partida.getId()) + "\"}");
                }
                progressDialog.dismiss();
            }
        }).execute(getString(R.string.url_comentario_adcionar));
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
        try {
//            txtGolsTimeMandante = (TextView) findViewById(R.id.txtTempoRealGolsTimeMandante);
//            txtGolsTimeVisitante = (TextView) findViewById(R.id.txtTempoRealGolsTimeVisitante);
//            escudoMandante = (SimpleDraweeView) findViewById(R.id.img_time_mandante_tempo_real);
//            escudoVisitante = (SimpleDraweeView) findViewById(R.id.img_time_visitante_tempo_real);
            nomeTimePalpitePorJogoMandante = (TextView) findViewById(R.id.nomeTimeMandanteTempoReal);
            nomeTimePalpitePorJogoVisitante = (TextView) findViewById(R.id.nomeTimeVisitanteTempoReal);
            lvComentarioAoVivo = (ListView) findViewById(R.id.lvTempoReal);
            fab = (FloatingActionButton) findViewById(R.id.fabAddComentario);

            //Uri imageUriMandante = Uri.parse(MainActivity.PATH_FOTOS + partida.getEscudoPequenoMandante().trim());
           // Uri imageUriVisitante = Uri.parse(MainActivity.PATH_FOTOS + partida.getEscudoPequenoVisitante().trim());

            nomeTimePalpitePorJogoMandante.setText(partida.getTimeMandante());
            nomeTimePalpitePorJogoVisitante.setText(partida.getTimeVisitante());

           // escudoMandante.setImageURI(imageUriMandante);
          //  escudoVisitante.setImageURI(imageUriVisitante);

            Usuario usuario = JsonHelper.ObterUsuarioBancoLocal(this);
            if (usuario== null || usuario.getTipoUsuario().equals("0") || usuario.getTipoUsuario().equals("3")) {
                fab.setVisibility(View.INVISIBLE);
            } else {
                fab.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: carregarComponente PartidaTempoReal: " + ex.getMessage());
        }
    }

    private void donwnloadFromUrl(final String nomeJsonParam, String urlJson, final String parametro) {
        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PartidaTempoRealActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;
                try {
                    String url = params[0];
                    //json = HttpHelper.POSTJson(url, parametro);

                    if (!HttpHelper.existeConexao(PartidaTempoRealActivity.this)) {
                        exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção");
                    } else {
                        json = HttpHelper.POSTJson(getString(R.string.url_partida_ao_vivo), parametro);
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


                if (json.equals("")) {
                    exibirMensagemOK("Nenhum comentário para a partida.", "Ao Vivo");

                } else {
                    carregaListaComentarioAoVivo(json);
                }
                progressDialog.dismiss();
            }
        }).execute(urlJson);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ao_vivo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_atualizar_aoVivo:
                donwnloadFromUrl("aovivo", getString(R.string.url_partida_ao_vivo), "{\"id\": \"" + String.valueOf(partida.getId()) + "\"}");
                return  true;
            default:
                onBackPressed();
                return true;
        }
    }

    private void carregaListaComentarioAoVivo(String json) {
        try {
            List<AoVivo> listComentariosAoVivo = JsonHelper.getList(json, AoVivo[].class);
            if (listComentariosAoVivo != null && listComentariosAoVivo.size() > 0) {

                AdapterAoVivo adapterAoVivo = new AdapterAoVivo(this, listComentariosAoVivo);
                lvComentarioAoVivo.setAdapter(adapterAoVivo);
                UIHelper.setListViewHeightBasedOnChildren(lvComentarioAoVivo);

                AoVivo aoVivo = listComentariosAoVivo.get(listComentariosAoVivo.size() - 1);
                String[] resultado = aoVivo.getComentario().split("_");
                //txtGolsTimeMandante.setText(resultado[0] == null ? "" : resultado[0]);
                //txtGolsTimeVisitante.setText(resultado[1] == null ? "" : resultado[1]);
            } else {
                exibirMensagemOK("Nenhum comentário para a partida.", "Ao Vivo");
            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao carregar lista de comentarios ao vivo.");
        }
    }


}
