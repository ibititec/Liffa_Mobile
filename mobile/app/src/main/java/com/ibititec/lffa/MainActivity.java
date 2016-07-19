package com.ibititec.lffa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ibititec.lffa.helpers.HttpHelper;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.noticia.FeedNoticiasActivity;
import com.ibititec.lffa.util.RegistrationIntentService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //DECLARACAO DOS OBJETOS DE TELA
    private ImageButton btnPrimeiraDivisao, btnSegundaDivisao, btnAliga,btnNoticias,btnFullpad;
    private TextView txtEquipes, txtNopad, txtALiga,txtNoticias, txtSair, txtFullpad;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;

    // private ProgressDialog progressDialog;

    //CONSTANTES NOME DO JSON NA BASE DE DADOS
    //CONSTANTES NOME DO JSON NA BASE DE DADOS
    public static final String PDARTILHARIA = "pdartilharia_lffa", PDTABELA = "pdtabela_lffa", PDCLASSIFICACAO = "pdclassificacao_lffa",
            SDARTILHARIA = "sdartilharia_lffa", SDTABELA = "dstabela_lffa", SDCLASSIFICACAO = "sdclassificacao_lffa",
            PDCLASSIFICACAOBOLAO = "pdclassificacaobolao_lffa", SDCLASSIFICACAOBOLAO = "sdclassificacaobolao_lffa",
            PDJOGOSBOLAO = "pdjogosbolao_lffa", SDJOGOSBOLAO = "sdjogosbolao_lffa0", USUARIO = "usuario", PDJOGOSRODADA = "pdjogosRODADA", SDJOGOSRODADA = "sdjogosRODADA";


    public static final String TAG = "CAMPEONATOLD";
    public static final String PATH_FOTOS = "http://52.37.37.207:96/Campeonato/Image?nomeimagem=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        //AS DUAS LINHAS ABAIXO DESABILITAR A BARRA DE MENU
        toggle.setDrawerIndicatorEnabled(false);
        //toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //IDENTIFICACAO DOS OBJETOS DE LAYOUT
        btnPrimeiraDivisao = (ImageButton) findViewById(R.id.btnPrimeiraDivisao);
        btnSegundaDivisao = (ImageButton) findViewById(R.id.btnSegundaDivisao);
        btnAliga= (ImageButton) findViewById(R.id.btnAliga);

        txtALiga = (TextView) findViewById(R.id.txtALiga);
        txtEquipes = (TextView) findViewById(R.id.txtEquipes);
        txtNopad = (TextView) findViewById(R.id.txtNopad);

        inicializarPushMessage();
        btnAliga= (ImageButton) findViewById(R.id.btnAliga);
        btnNoticias = (ImageButton) findViewById(R.id.btnNoticias);
        txtNoticias = (TextView) findViewById(R.id.txtNoticias);
        txtSair = (TextView) findViewById(R.id.txtSair);

        txtFullpad = (TextView) findViewById(R.id.txtFullpad);
        btnFullpad = (ImageButton) findViewById(R.id.btnSegundaFullpad);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("LOG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

     private void inicializarPushMessage() {
         if(checkPlayServices()) {
             Intent intent = new Intent(this, RegistrationIntentService.class);
             startService(intent);
         }
    }

    private void iniciarAppodeal() {
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    @Override
    public void onResume() {
        super.onResume();
        //METODOS DOS CLICKS
        executarAcoes();

        atualizarBaseDados(false);
       // AnalyticsApplication.enviarGoogleAnalitcs(this);
        iniciarAppodeal();
    }

    private void atualizarBaseDados(boolean atualizar) {
        try {

            if (JsonHelper.leJsonBancoLocal(MainActivity.PDTABELA, this) == "" || atualizar == true) {
                if (existeConexao()) {
                    donwnloadFromUrl(PDTABELA, getString(R.string.url_pdtabela), "");
                    donwnloadFromUrl(PDARTILHARIA, getString(R.string.url_pdartilharia), "{\"id\": \"1\"}");
                    donwnloadFromUrl(PDCLASSIFICACAO, getString(R.string.url_pdclassificacao), "{\"id\": \"1\"}");

                    donwnloadFromUrl(SDTABELA, getString(R.string.url_sdtabela), "{\"id\": \"2\"}");
                    donwnloadFromUrl(SDARTILHARIA, getString(R.string.url_sdartilharia), "{\"id\": \"2\"}");
                    donwnloadFromUrl(SDCLASSIFICACAO, getString(R.string.url_sdclassificacao), "{\"id\": \"2\"}");

                    donwnloadFromUrl(PDCLASSIFICACAOBOLAO, getString(R.string.url_pdclassificacaobolao), "");

                    donwnloadFromUrl(SDCLASSIFICACAOBOLAO, getString(R.string.url_sdclassificacaobolao), "");
                } else {
                    Log.i(TAG, "Sem conexão com a internet.");
                }
            }
        } catch (Exception ex) {
            Log.i(TAG, "Não foi possível atualizar  base de dados." + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_atualizar) {
            atualizarBaseDados(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void executarAcoes() {

        btnPrimeiraDivisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VOU CHAMAR TEAL DE EQUIPES
                startarActivityTabela("primeira", "equipes");
            }
        });

        txtEquipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VOU CHAMAR TEAL DE EQUIPES
                startarActivityTabela("primeira", "equipes");
            }
        });

        btnAliga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VOU CHAMAR TEAL DE EQUIPES
                startarActivityALiga();

            }
        });


        txtALiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VOU CHAMAR TEAL DE EQUIPES
                startarActivityALiga();

            }
        });

        btnSegundaDivisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity("primeira", "");
            }
        });

        txtNopad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity("primeira", "");
            }
        });

        txtNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityNoticia();
            }
        });
        btnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityNoticia();
            }
        });

        txtFullpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity("fullpad", "");
            }
        });
        btnFullpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity("fullpad", "");
            }
        });

        txtSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  
                    finish();

                } catch (Exception exx) {
                    Log.i("ERRO", "Erro ao finish");
                }
            }
        });

    }

    @Override
    public void onDestroy(){
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    private void startarActivityALiga() {

        Intent intent = new Intent(this, ALigaActivity.class);
        startActivity(intent);
    }
    private void startarActivity(String divisao, String funcionalidade) {
        Intent intent = new Intent(this, PrimeiraDivisaoActivity.class);
        intent.putExtra("divisao", divisao);
        intent.putExtra("funcionalidade", funcionalidade);
        startActivity(intent);
    }

    private void startarActivityTabela(String divisao, String funcionalidade) {
        Intent intent = new Intent(this, PrimeiraDivisaoTabelaActivity.class);
        intent.putExtra("divisao", divisao);
        intent.putExtra("funcionalidade", funcionalidade);
        startActivity(intent);
    }

    private void startarActivityNoticia() {
        try {
            Intent intent = new Intent(this, FeedNoticiasActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: startartActivityNoticia MainActivity: " + ex.getMessage());
        }
    }

    private void gravarJson(String nomeJson, String json) {
        try {
            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                    .putString(nomeJson + ".json", json)
                    .apply();
            Log.i(TAG, "Gravou Json: " + json);
        } catch (Exception ex) {
            Log.i(TAG, "Erro no metodo:  gravarJson: " + nomeJson + " - Erro: " + ex.getMessage());
        }
    }

    private void donwnloadFromUrl(final String nomeJsonParam, String urlJson, final String param) {
        (new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Atualizando dados");
            }

            @Override
            protected String doInBackground(String... params) {
                String json = null;

                try {
                    String url = params[0];
                    if (param.equals("")) {
                        json = HttpHelper.downloadFromURL(url);
                    } else {
                        json = HttpHelper.POSTJson(url, param);
                    }
                    Log.i(TAG, json);
                    if (json == null) {
                        Log.w(TAG, "JSON veio nulo na url : " + url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, String.format(getString(R.string.msg_erro_json), e.getMessage()));
                }
                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                progressDialog.dismiss();

                if (json == null) {
                    //Log.w(TAG, "JSON veio nulo!");
                    return;
                }

                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                        .putString(nomeJsonParam + ".json", json)
                        .apply();
            }
        }).execute(urlJson);
    }

    private void exibirMensagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Não identificado conexão com a internet, verifique sua conexão está ativa.");
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

    public boolean existeConexao() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            if (!haveConnectedWifi && !haveConnectedMobile) {
                exibirMensagem();
            }
            return haveConnectedWifi || haveConnectedMobile;

        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao verificar conexao com a intenet." + ex.getMessage());
            return false;
        }
    }

}
