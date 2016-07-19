package com.ibititec.lffa.noticia;

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
import android.widget.EditText;
import android.widget.ListView;

import com.appodeal.ads.Appodeal;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.adapter.AdapterNoticia;
import com.ibititec.lffa.helpers.HttpHelper;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.helpers.UIHelper;
import com.ibititec.lffa.modelo.Noticia;
import com.ibititec.lffa.modelo.Usuario;

import java.io.IOException;
import java.util.List;

public class FeedNoticiasActivity extends AppCompatActivity {

    private ListView lvFeedNoticias;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_feed_noticias);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            lerIntent();
            carregarComponentes();
            executarAcoes();
            iniciarAppodeal();
            donwnloadFromUrl("feedNoticias", getString(R.string.url_feed_noticias), "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "Erro onCreate FeedNoticias" + e.getMessage());
        }
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
            }else{
                exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "Erro onCreate FeedNoticias" + e.getMessage());
        }
    }

    private void executarAcoes() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogNoticia();
            }
        });
    }

    private void abrirDialogNoticia() {
        try {
            final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_noticia, null);

            final EditText editTituloAdicionar = ((EditText) viewDialog.findViewById(R.id.txtTituloNoticia));
            final EditText editCorpoAdicionar = ((EditText) viewDialog.findViewById(R.id.txtCorpoNoticia));

            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(FeedNoticiasActivity.this)
                    .setTitle("Comentário")
                            //.setIcon(R.mipmap.ic_launcher_auteasy)
                    .setPositiveButton("SALVAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!editTituloAdicionar.getText().toString().equals("") && !editCorpoAdicionar.getText().toString().equals("")) {
                                Noticia noticia = new Noticia();
                                noticia.setTitulo(editTituloAdicionar.getText().toString());
                                noticia.setCorpo(editCorpoAdicionar.getText().toString());
                                noticia.setUsuario(JsonHelper.ObterUsuarioBancoLocal(FeedNoticiasActivity.this));
                                salvarNoticiaBancoRemoto(noticia);
                            } else {
                                exibirMensagem("Título ou notícia em branco, favor preencher", "Atenção", false);
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

    private void salvarNoticiaBancoRemoto(final Noticia noticia) {

        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(FeedNoticiasActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;
                try {
                    String url = params[0];
                    //json = HttpHelper.POSTJson(url, parametro);

                    if (!HttpHelper.existeConexao(FeedNoticiasActivity.this)) {
                        exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção", true);
                    } else {
                        json = HttpHelper.POSTJson(url, JsonHelper.objectToJson(noticia));
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
                    exibirMensagemOK("Não foi possível gravar a notícia", "Notícia");
                } else {
                    donwnloadFromUrl("feedNoticias", getString(R.string.url_feed_noticias), "");
                }
                progressDialog.dismiss();
            }
        }).execute(getString(R.string.url_feed_adicionar_noticias));
    }


    private void exibirMensagem(String mensagem, String titulo, final boolean returnPrincipal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(mensagem);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (returnPrincipal) {
                    onBackPressed();
                } else {
                    return;
                }
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
            lvFeedNoticias = (ListView) findViewById(R.id.lvFeedNoticias);
            fab = (FloatingActionButton) findViewById(R.id.fabAddNoticia);

            Usuario usuario = JsonHelper.ObterUsuarioBancoLocal(this);
            if (usuario== null || usuario.getTipoUsuario().equals("0")) {
                fab.setVisibility(View.INVISIBLE);
            } else {
                fab.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "Erro carregarComponentes FeedNoticias" + e.getMessage());
        }
    }

    private void donwnloadFromUrl(final String nomeJsonParam, String urlJson, final String parametro) {
        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(FeedNoticiasActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;
                try {
                    String url = params[0];
                    //json = HttpHelper.POSTJson(url, parametro);

                    if (!HttpHelper.existeConexao(FeedNoticiasActivity.this)) {
                        exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção", true);
                    } else {
                        json = HttpHelper.POSTJson(url, parametro);
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
                    exibirMensagem("Nenhuma notícia cadastrada.", "Notícias", false);

                } else {
                    carregaListaNoticias(json);
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
                donwnloadFromUrl("feednoticias", getString(R.string.url_feed_noticias), "");
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

    private void carregaListaNoticias(String json) {
        try {
            List<Noticia> listNoticias = JsonHelper.getList(json, Noticia[].class);
            if (listNoticias != null && listNoticias.size() > 0) {

                AdapterNoticia adapterNoticia = new AdapterNoticia(this, listNoticias);
                lvFeedNoticias.setAdapter(adapterNoticia);
                UIHelper.setListViewHeightBasedOnChildren(lvFeedNoticias);
            } else {
                exibirMensagem("Nenhuma notícia cadastrada", "Notícias", false);
            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao carregar lista de notícias.");
        }
    }
}
