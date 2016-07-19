package com.ibititec.lffa.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.modelo.AoVivo;

import java.util.List;

/**
 * Created by JOAOCELSON on 03/05/2016.
 */
public class AdapterAoVivo extends BaseAdapter {

    private Activity activity;
    private List<AoVivo> aoVivoList = null;

    public AdapterAoVivo(Activity activityParam, List<AoVivo> aoVivoList){
        this.aoVivoList = aoVivoList;
        this.activity = activityParam;
    }

    public AdapterAoVivo(){}

    @Override

    public int getCount() {
        return aoVivoList.size();
    }

    @Override
    public Object getItem(int position) {
        return aoVivoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final AoVivo aoVivo = aoVivoList.get(position);
            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + aoVivoList.size() + " Position: " + position + " - Nome Rodada " + aoVivo.getData());

            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.adapter_aovivo, null);

            TextView data = (TextView) layout.findViewById(R.id.txtDataAoVivo);
            TextView comentario  = (TextView) layout.findViewById(R.id.txtComentatioAoVivo);

            if(!aoVivo.getData().startsWith("Resultado")) {
                data.setText(aoVivo.getData());
                comentario.setText(aoVivo.getComentario());
            }

            //Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
            return layout;

        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro ao preecnher o getView - Ao Vivo: " + e.getMessage());
        }
        return convertView;
    }
}
