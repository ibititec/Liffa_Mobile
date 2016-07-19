package com.ibititec.lffa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.ibititec.lffa.bolao.BolaoPrincipalActivity;

public class PrimeiraDivisaoActivity extends AppCompatActivity {

    private ImageButton btnTabela, btnArtilharia, btnClassificacao, btnAjuda, btnBolao;
    private TextView txtTabela, txtMaiorPontuador, txtClassificacao, txtAjuda, txtBolao;

    private String divisao;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira_divisao);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //btnArtilharia = (ImageButton) findViewById(R.id.btnPrimeiraDivisaoArtilharia);
        btnTabela = (ImageButton) findViewById(R.id.btnPrimeiraDivisaoTabela);
        btnClassificacao = (ImageButton) findViewById(R.id.btnPrimeiraDivisaoClassificacao);
        btnAjuda = (ImageButton) findViewById(R.id.btnPrimeiraDivisaoSobre);

        //txtMaiorPontuador = (TextView) findViewById(R.id.txtMaiorPontuador);
        txtTabela = (TextView) findViewById(R.id.txtTabela);
        txtClassificacao = (TextView) findViewById(R.id.txtClassificacao);
        txtAjuda = (TextView) findViewById(R.id.txtAjuda);

        txtBolao = (TextView) findViewById(R.id.txtBolao);
        btnBolao = (ImageButton) findViewById(R.id.btnPrimeiraDivisaoBolao);


        lerIntent();
        executarAcoes();
    }

    @Override
    public void onResume() {
        super.onResume();

        lerIntent();
//        AnalyticsApplication.enviarGoogleAnalitcs(this);
        iniciarAppodeal();
    }

    private void iniciarAppodeal() {
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("divisao", divisao);
        // add data to Intent
        setResult(PrimeiraDivisaoTabelaActivity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void executarAcoes() {

        if (divisao.equals("primeira")) {
            this.setTitle("NOPAD");
        } else {
            this.setTitle("FULLPAD");
        }

//        btnArtilharia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startarActivity(divisao, "artilharia");
//            }
//        });

        btnTabela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity(divisao, "tabela");
            }
        });

        btnClassificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity(divisao, "classificacao");
            }
        });

        btnBolao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityBolao(divisao);
            }
        });

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityHelp(divisao, "sobre");
            }
        });

//        txtMaiorPontuador.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startarActivity(divisao, "artilharia");
//            }
//        });

        txtTabela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity(divisao, "tabela");
            }
        });

        txtClassificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivity(divisao, "classificacao");
            }
        });

        txtAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityHelp(divisao, "sobre");
            }
        });

        txtBolao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startarActivityBolao(divisao);
            }
        });


    }

    private void startarActivityHelp(String divisao, String funcionalidade) {
        Intent intent = new Intent(this, SobreActivity.class);
        intent.putExtra("divisao", divisao);
        intent.putExtra("funcionalidade", funcionalidade);
        startActivity(intent);
    }

    private void startarActivityBolao(String divisao) {
        try {
            Intent intent = new Intent(this, BolaoPrincipalActivity.class);
            intent.putExtra("divisao", divisao);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: startarActivityBolao PrimeiraDivisao: " + ex.getMessage());
        }
    }

    private void startarActivity(String divisao, String funcionalidade) {
        Intent intent = new Intent(this, PrimeiraDivisaoTabelaActivity.class);
        intent.putExtra("divisao", divisao);
        intent.putExtra("funcionalidade", funcionalidade);
        startActivity(intent);
    }

    private void lerIntent() {
        Intent intent = getIntent();
        divisao = intent.getStringExtra("divisao");
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
        return true;
    }
}
