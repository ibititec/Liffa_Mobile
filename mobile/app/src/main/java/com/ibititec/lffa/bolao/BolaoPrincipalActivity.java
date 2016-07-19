package com.ibititec.lffa.bolao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.helpers.HttpHelper;

public class BolaoPrincipalActivity extends AppCompatActivity {

    private ImageButton btnClassificacao, btnPalpite, btnRegulamento;
    private TextView txtClassificacao, txtPalpite, txtRegulamento;
    private String divisao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_bolao_principal);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            lerIntent();
            carregarComponentes();
            executarAcoes();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro onCreate Bolao Principal: " + ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            lerIntent();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro ONResume Classficacao: " + ex.getMessage());
        }
    }

    private void lerIntent() {
        try {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
            Intent intent = getIntent();
            divisao = intent.getStringExtra("divisao");
            if (!HttpHelper.existeConexao(this)) {
                exibirMensagem("Não identificado conexão com a internet, verifique se sua conexão está ativa.", "Atenção");

            }
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro leIntent BolaoPrincipal : " + ex.getMessage());
        }
    }

    private void executarAcoes() {
        try {
            if (divisao.equals("primeira")) {
                this.setTitle("Bolão LiFFA");
            }

            btnPalpite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityPalpites(divisao);
                }
            });

            txtPalpite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityPalpites(divisao);
                }
            });

            btnClassificacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityClassificacao(divisao);
                }
            });
            txtClassificacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityClassificacao(divisao);
                }
            });

            btnRegulamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityRegulamento();
                }
            });
            txtRegulamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startarActivityRegulamento();
                }
            });
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro Bolao Principal : " + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent();
            intent.putExtra("divisao", divisao);

            // add data to Intent
            setResult(BolaoPrincipalActivity.RESULT_OK, intent);
            super.onBackPressed();
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro: onBackPressedPrimeiraDivisaoTabela: " + ex.getMessage());
        }
    }

    private void startarActivityRegulamento() {
        try {
            Intent intent = new Intent(this, RegrasActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro StartarActivityClassificacao: " + ex.getMessage());
        }
    }

    private void startarActivityClassificacao(String divisao) {
        try {
            Intent intent = new Intent(this, ClassificacaoActivity.class);
            intent.putExtra("divisao", divisao);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro StartarActivityClassificacao: " + ex.getMessage());
        }
    }

    private void startarActivityPalpites(String divisao) {
        try {
            Intent intent = new Intent(this, PalpiteActivity.class);
            intent.putExtra("divisao", divisao);
            startActivity(intent);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro statarActivityPalpites : " + ex.getMessage());
        }
    }

    private void carregarComponentes() {
        try {
            btnClassificacao = (ImageButton) findViewById(R.id.btnClassificaoBolao);
            btnPalpite = (ImageButton) findViewById(R.id.btnPalpitesBolao);
            txtClassificacao = (TextView) findViewById(R.id.txtClassificacaoBolao);
            txtPalpite = (TextView) findViewById(R.id.txtPalpitesBolao);
            btnRegulamento = (ImageButton) findViewById(R.id.btnRegrasBolao);
            txtRegulamento = (TextView) findViewById(R.id.txtRegrasBolao);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro Bolao Principal CarregagarComponetes: " + ex.getMessage());
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

    private void exibirMensagem(String mensagem, String titulo) {
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
}
