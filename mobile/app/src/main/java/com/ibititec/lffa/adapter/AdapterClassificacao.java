package com.ibititec.lffa.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.modelo.Classificacao;

import java.util.List;

public class AdapterClassificacao extends BaseAdapter {
    private List<Classificacao> classificacaoList = null;
    private Activity activity;
    private boolean exibirPontuacao;

    public AdapterClassificacao(Activity activityParam, List<Classificacao> classificacaoList, boolean exibirPontuacao) {
        this.classificacaoList = classificacaoList;
        this.activity = activityParam;
        this.exibirPontuacao = exibirPontuacao;
    }

    public AdapterClassificacao() {
    }

    @Override
    public int getCount() {
        return classificacaoList.size();
    }

    @Override
    public Object getItem(int position) {
        return classificacaoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {

            final Classificacao classificacao = classificacaoList.get(position);
            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + classificacaoList.size() + " Position: " + position + " - Nome Rodada " + classificacao.getTime());

            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.adapter_classificacao, null);

            //TextView posicao = (TextView) layout.findViewById(R.id.txtPosicao);
            //TextView pontos = (TextView) layout.findViewById(R.id.txtPontos);
            TextView jogos = (TextView) layout.findViewById(R.id.txtJogos);
            TextView vitoria = (TextView) layout.findViewById(R.id.txtVitorias);
            TextView derrotas = (TextView) layout.findViewById(R.id.txtDerrotas);
            //TextView empates = (TextView) layout.findViewById(R.id.txtEmpates);

            TextView pontospp = (TextView) layout.findViewById(R.id.txtPP);
            TextView pontospc = (TextView) layout.findViewById(R.id.txtPC);
            //TextView time = (TextView) layout.findViewById(R.id.txtTime);

            if (exibirPontuacao) {
                //   posicao.setText(classificacao.getPosicao());
                //pontos.setText(classificacao.getPontos());
                jogos.setText(classificacao.getJogos());
                vitoria.setText(classificacao.getVitoria());
                derrotas.setText(classificacao.getDerrota());
                //empates.setText(classificacao.getEmpate());
                //empates.setVisibility(View.INVISIBLE);
                pontospp.setText(classificacao.getGolPro());
                pontospc.setText(classificacao.getGolContra());
                Uri imageUri = Uri.parse(MainActivity.PATH_FOTOS + classificacao.getTime() + "_escudo.png");
                SimpleDraweeView draweeView = (SimpleDraweeView) layout.findViewById(R.id.ivClassificacao);
                draweeView.setImageURI(imageUri);
                if (classificacao.getTime().equals("")) {
                    draweeView.setVisibility(View.INVISIBLE);
                }
            }


            if (!exibirPontuacao) {

                Uri imageUri = Uri.parse(MainActivity.PATH_FOTOS + classificacao.getTime() + ".png");
                SimpleDraweeView draweeView = (SimpleDraweeView) layout.findViewById(R.id.ivClassificacao);
                draweeView.setImageURI(imageUri);
                if (classificacao.getNomeTime().startsWith("GRUPO")) {
                    draweeView.setVisibility(View.INVISIBLE);
                }
                //pontos.setVisibility(View.INVISIBLE);
                jogos.setVisibility(View.INVISIBLE);
                vitoria.setVisibility(View.INVISIBLE);
                derrotas.setVisibility(View.INVISIBLE);
                //empates.setVisibility(View.INVISIBLE);
                pontospc.setVisibility(View.INVISIBLE);
                pontospp.setVisibility(View.INVISIBLE);
                ViewGroup.LayoutParams params = draweeView.getLayoutParams();
                params.width = ActionBar.LayoutParams.MATCH_PARENT;
                draweeView.setLayoutParams(params);
            }
            //Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
            return layout;
        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());
        }
        return convertView;

//            final Classificacao classificacao = classificacaoList.get(position);
//            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + classificacaoList.size() + " Position: " + position + " - Nome Rodada " + classificacao.getTime());
//
//            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            layout = inflater.inflate(R.layout.adapter_classificacao, null);
//
//            TextView posicao = (TextView) layout.findViewById(R.id.txtPosicao);
//            TextView pontos = (TextView) layout.findViewById(R.id.txtPontos);
//            TextView time = (TextView) layout.findViewById(R.id.txtTime);
//
//            posicao.setText(classificacao.getPosicao());
//            pontos.setText(classificacao.getPontos());
//            time.setText(classificacao.getTime());
//            return layout;
//        } catch (Exception e) {
//            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());
//            return  convertView;
    }
}
