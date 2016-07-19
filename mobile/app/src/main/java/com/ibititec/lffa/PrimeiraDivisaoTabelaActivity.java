package com.ibititec.lffa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.appodeal.ads.Appodeal;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ibititec.lffa.adapter.AdapterArtilharia;
import com.ibititec.lffa.adapter.AdapterClassificacao;
import com.ibititec.lffa.adapter.AdapterRodada;
import com.ibititec.lffa.helpers.JsonHelper;
import com.ibititec.lffa.helpers.UIHelper;
import com.ibititec.lffa.modelo.Artilharia;
import com.ibititec.lffa.modelo.Classificacao;
import com.ibititec.lffa.modelo.Rodada;
import com.ibititec.lffa.util.AnalyticsApplication;

import java.util.ArrayList;
import java.util.List;

public class PrimeiraDivisaoTabelaActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ListView lvTabela;
    private LinearLayout cabecalhoLayout;
    private String funcionalidade, divisao, tabela, classificacao, artilharia;
    static final String TAG = "LFFA";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira_divisao_tabela);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress_primeira_tabela);
        lvTabela = (ListView) findViewById(R.id.listview_primeira_tabela);
        progressBar.setVisibility(View.GONE);

        lerIntent();
        executarAcoes();

        // //INICIALIZACAO DO FRESCO
        Fresco.initialize(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApplication.enviarGoogleAnalitcs(this);
        iniciarAppodeal();
    }

    private void iniciarAppodeal() {
        try {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: iniciarAppodeal: " + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("divisao", divisao);

        // add data to Intent
        setResult(PrimeiraDivisaoTabelaActivity.RESULT_OK, intent);
        Appodeal.show(this, Appodeal.NATIVE);
        super.onBackPressed();
    }

    private void lerIntent() {
        Intent intent = getIntent();
        divisao = intent.getStringExtra("divisao");
        funcionalidade = intent.getStringExtra("funcionalidade");
    }

    private void executarAcoes() {
        switch (funcionalidade) {
            case "tabela":
                if (divisao.equals("primeira")) {
                    atualizarTabelaPrimeiraDivisao();
                } else {
                    atualizarTabelaSegundaDivisao();
                }
                break;
            case "classificacao":
                if (divisao.equals("primeira")) {
                    atualizarClassificacaoPrimeiraDivisao();
                } else {
                    atualizarClassificacaoSegundaDivisao();
                }
                break;
            case "artilharia":
                if (divisao.equals("primeira")) {
                    atualizarArtilhariaPrimeiraDivisao();
                } else {
                    atualizarArtilhariaSegundaDivisao();
                }
                break;
            case "equipes":
                if (divisao.equals("primeira")) {
                    atualizarEquipesPrimeiraDivisao();
                }
                break;
        }
    }

    private void atualizarEquipesPrimeiraDivisao() {
        try {
            //cabecalhoLayout = (LinearLayout) findViewById(R.id.cabecalho_classificacao);
            //cabecalhoLayout.setVisibility(View.VISIBLE);
            this.setTitle("Equipes");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.PDCLASSIFICACAO, this);
            String tabelaFullpad = JsonHelper.leJsonBancoLocal(MainActivity.SDCLASSIFICACAO, this);
            List<Classificacao> listClassificacao = JsonHelper.getList(tabela, Classificacao[].class);
            List<Classificacao> listClassificacaoFullpad = JsonHelper.getList(tabelaFullpad, Classificacao[].class);

            ArrayList<Classificacao> list = new ArrayList<Classificacao>();

            for (Classificacao classificacao : listClassificacao) {
                if (!classificacao.getNomeTime().startsWith("GRUPO")) {
                    list.add(classificacao);
                }
            }

            for (Classificacao classificacao : listClassificacaoFullpad) {
                if (!classificacao.getNomeTime().startsWith("GRUPO") && !classificacao.getNomeTime().equals("")) {
                    list.add(classificacao);
                }
            }

            AdapterClassificacao adapterClassificacao = new AdapterClassificacao(this, list, false);
            lvTabela.setAdapter(adapterClassificacao);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarArtilhariaSegundaDivisao() {
        try {
            cabecalhoLayout = (LinearLayout) findViewById(R.id.cabecalho_artilahria);
            cabecalhoLayout.setVisibility(View.VISIBLE);
            this.setTitle("Artilharia 2ª Divisão");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.SDARTILHARIA, this);
            List<Artilharia> listArtilharia = JsonHelper.getList(tabela, Artilharia[].class);
            AdapterArtilharia adapterArtilharia = new AdapterArtilharia(this, listArtilharia);
            lvTabela.setAdapter(adapterArtilharia);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarArtilhariaPrimeiraDivisao() {
        try {
            cabecalhoLayout = (LinearLayout) findViewById(R.id.cabecalho_artilahria);
            cabecalhoLayout.setVisibility(View.VISIBLE);
            this.setTitle("Maior Pontuador");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.PDARTILHARIA, this);
            List<Artilharia> listArtilharia = JsonHelper.getList(tabela, Artilharia[].class);
            AdapterArtilharia adapterArtilharia = new AdapterArtilharia(this, listArtilharia);
            lvTabela.setAdapter(adapterArtilharia);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarClassificacaoSegundaDivisao() {
        try {
            // cabecalhoLayout = (LinearLayout) findViewById(R.id.cabecalho_classificacao);
            // cabecalhoLayout.setVisibility(View.VISIBLE);
            this.setTitle("Classificação FULLPAD");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.SDCLASSIFICACAO, this);
            List<Classificacao> listClassificacao = JsonHelper.getList(tabela, Classificacao[].class);
            AdapterClassificacao adapterClassificacao = new AdapterClassificacao(this, listClassificacao, true);
            lvTabela.setAdapter(adapterClassificacao);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarClassificacaoPrimeiraDivisao() {
        try {
            //cabecalhoLayout = (LinearLayout) findViewById(R.id.cabecalho_classificacao);
            //cabecalhoLayout.setVisibility(View.VISIBLE);
            this.setTitle("Classificação Geral");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.PDCLASSIFICACAO, this);
            List<Classificacao> listClassificacao = JsonHelper.getList(tabela, Classificacao[].class);
            AdapterClassificacao adapterClassificacao = new AdapterClassificacao(this, listClassificacao, true);
            lvTabela.setAdapter(adapterClassificacao);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarTabelaSegundaDivisao() {
        try {
            this.setTitle("Tabela FULLPAD");
            tabela = JsonHelper.leJsonBancoLocal(MainActivity.SDTABELA, this);
            List<Rodada> listRodada = JsonHelper.getList(tabela, Rodada[].class);
            AdapterRodada adapterRodada = new AdapterRodada(this, listRodada, divisao, funcionalidade);
            lvTabela.setAdapter(adapterRodada);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);

        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
    }

    private void atualizarTabelaPrimeiraDivisao() {
        try {
            this.setTitle("Tabela Geral");
            String tabela = JsonHelper.leJsonBancoLocal(MainActivity.PDTABELA, this);
            List<Rodada> listRodada = JsonHelper.getList(tabela, Rodada[].class);
            AdapterRodada adapterRodada = new AdapterRodada(this, listRodada, divisao, funcionalidade);
            lvTabela.setAdapter(adapterRodada);
            UIHelper.setListViewHeightBasedOnChildren(lvTabela);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ao preencher listView: " + ex.getMessage());
        }
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
