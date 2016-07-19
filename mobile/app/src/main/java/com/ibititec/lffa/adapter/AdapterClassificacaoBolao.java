package com.ibititec.lffa.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.modelo.Classificacao;

public class AdapterClassificacaoBolao extends ArrayAdapter<Classificacao> {
    private final Classificacao classificacaoList[];
    private Context activity;
    private boolean exibirPontuacao;

    public AdapterClassificacaoBolao(Context activityParam, Classificacao[] classificacaoList) {
        super(activityParam, 0, classificacaoList);
        this.classificacaoList = classificacaoList;
        this.activity = activityParam;
        this.exibirPontuacao = exibirPontuacao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        try {

        final Classificacao classificacao = classificacaoList[position];
        Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + classificacaoList.length + " Position: " + position + " - Nome Rodada " + classificacao.getTime());

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.adapter_classificacao_bolao, null);

        //TextView posicao = (TextView) layout.findViewById(R.id.txtPosicao);
        //TextView pontos = (TextView) layout.findViewById(R.id.txtPontos);
        TextView nome = (TextView) layout.findViewById(R.id.txtNomeUsuarioBolao);
        TextView pontos = (TextView) layout.findViewById(R.id.txtPontosUsuarioBolao);
        TextView posicao = (TextView) layout.findViewById(R.id.txtPosicaoUsuarioBolao);
        //TextView empates = (TextView) layout.findViewById(R.id.txtEmpates);
        nome.setText(classificacao.getNomeTime());
        pontos.setText(classificacao.getPontos());
        posicao.setText(classificacao.getPosicao());

        //Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
        return layout;
//        } catch (Exception e) {
//            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());
//        }
//        return convertView;

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
