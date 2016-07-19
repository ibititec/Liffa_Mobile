package com.ibititec.lffa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.aovivo.PartidaTempoRealActivity;
import com.ibititec.lffa.modelo.Partida;
import com.ibititec.lffa.modelo.Rodada;

import java.io.Serializable;
import java.util.List;

public class AdapterRodada extends BaseAdapter {
    private Activity activity;
    private List<Rodada> rodadaList = null;
    String divisao, funcionalidade;

    public AdapterRodada(Activity activityParam, List<Rodada> rodadaListParam, String divisao, String funcionalidade) {
        this.rodadaList = rodadaListParam;
        this.activity = activityParam;
        this.funcionalidade = funcionalidade;
        this.divisao = divisao;
    }

    public AdapterRodada() {
    }

    @Override
    public int getCount() {
        return rodadaList.size();
    }

    @Override
    public Object getItem(int position) {
        return rodadaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final Rodada rodadaObj = rodadaList.get(position);
            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + rodadaList.size() + " Position: " + position + " - Nome Rodada " + rodadaObj.getNumero());

            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.adapter_rodada, null);

            //TextView rodada = (TextView) layout.findViewById(R.id.txtRodada);
            TextView Data = (TextView) layout.findViewById(R.id.txtData);
            TextView Local = (TextView) layout.findViewById(R.id.txtCampo);
            //TextView hrJogo1 = (TextView) layout.findViewById(R.id.txtHrJogo1);
            //TextView vsJogo1 = (TextView) layout.findViewById(R.id.txtversus);
            //TextView jogo1 = (TextView) layout.findViewById(R.id.txtJogo1);
            //TextView hrJogo2 = (TextView) layout.findViewById(R.id.txtHrJogo2);
            TextView vsJogo2 = (TextView) layout.findViewById(R.id.txtversus2);
            //TextView jogo2 = (TextView) layout.findViewById(R.id.txtJogo2);

            //rodada.setText("RODADA: " + rodadaObj.getNumero() + " - ");
            Data.setText("DATA: " + rodadaObj.getData());
            Local.setText("LOCAL: " + rodadaObj.getCampo());
            // hrJogo1.setText("Hr: " + rodadaObj.getHoraJogo1());
            // jogo1.setText(rodadaObj.getJogo1());
            // hrJogo2.setText("Hr: " + rodadaObj.getHoraJogo2());
            // jogo2.setText(rodadaObj.getJogo2());

            //setando nome da imagem a ser exibida
            String[] jogo1Array = splitString(rodadaObj.getJogo1());
            //String[] jogo2Array = splitString(rodadaObj.getJogo2());

            if(jogo1Array == null){
                jogo1Array[0] = rodadaObj.getJogo1().trim();
                jogo1Array[1] =rodadaObj.getJogo1().trim();
                jogo1Array[2] =rodadaObj.getJogo1().trim();

            }


            // vsJogo1.setText(jogo1Array[1]);
            vsJogo2.setText(jogo1Array[1]);

            Log.i(MainActivity.TAG, "URL position: " + position + " - "+ MainActivity.PATH_FOTOS + jogo1Array[0].trim()+".jpg");
            Log.i(MainActivity.TAG, "jogo1Array[0]: " + jogo1Array[0]);
            //Log.i(MainActivity.TAG, "jogo1Array[1]: " +jogo1Array[1]);
            //Log.i(MainActivity.TAG, "jogo2Array[0]: " +jogo2Array[0]);
            //Log.i(MainActivity.TAG, "jogo2Array[1]: " +jogo2Array[1]);

            Uri imageUri = Uri.parse(MainActivity.PATH_FOTOS + jogo1Array[0].trim()+".png");
            SimpleDraweeView draweeView = (SimpleDraweeView) layout.findViewById(R.id.imageView2);
            draweeView.setImageURI(imageUri);

            Uri imageUri2 = Uri.parse(MainActivity.PATH_FOTOS + jogo1Array[2].trim()+".png");
            SimpleDraweeView draweeView2 = (SimpleDraweeView) layout.findViewById(R.id.imageView4);
            draweeView2.setImageURI(imageUri2);

//            ImageButton btnTempoReal = (ImageButton) layout.findViewById(R.id.btnTempoRealPartida1);
//
//            // btnTempoReal.setImageResource(R.drawable.temporeal);
//            btnTempoReal.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startarActivityTempoReal(v, rodadaObj.getPartida1());
//                }
//            });

//            ImageButton btnTempoReal2 = (ImageButton) layout.findViewById(R.id.btnTempoRealPartida2);
//            // btnTempoReal2.setImageResource(R.drawable.temporeal);
//            btnTempoReal2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startarActivityTempoReal(v, rodadaObj.getPartida2());
//                }
//            });

            //Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
            return layout;

        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());

        }
        return convertView;
    }

    private void startarActivityTempoReal(View v, Partida partida) {
        Intent intent = new Intent(activity.getApplication(), PartidaTempoRealActivity.class);
        intent.putExtra("partida_tempo_real", (Serializable) partida);
        intent.putExtra("divisao", divisao);
        intent.putExtra("funcionalidade", funcionalidade);

        activity.startActivity(intent);
    }

    public String[] splitString(String param) {
        try {
            return param.split("-");
        } catch (Exception ex) {
            ex.getMessage();
            return null;
        }
    }
}
