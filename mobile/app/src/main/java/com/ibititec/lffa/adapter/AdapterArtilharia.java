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
import com.ibititec.lffa.modelo.Artilharia;

import java.util.List;

public class AdapterArtilharia extends BaseAdapter {

    private List<Artilharia> artilhariaList = null;
    private Activity activity;

    public AdapterArtilharia(Activity activityParam, List<Artilharia> artilhariaList){
        this.artilhariaList = artilhariaList;
        this.activity = activityParam;
    }

    public AdapterArtilharia(){}

    @Override
    public int getCount() {
        return artilhariaList.size();
    }

    @Override
    public Object getItem(int position) {
        return artilhariaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final Artilharia artilharia = artilhariaList.get(position);
            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + artilhariaList.size() + " Position: " + position + " - Nome Rodada " + artilharia.getNome());

            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.adapter_artilharia, null);

            TextView nome = (TextView) layout.findViewById(R.id.txtNome);
            TextView gols = (TextView) layout.findViewById(R.id.txtGols);
            TextView time = (TextView) layout.findViewById(R.id.txtTime);

            nome.setText( artilharia.getNome());
            gols.setText(artilharia.getNumeroGols());
            time.setText(artilharia.getTime());

            //Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
            return layout;

        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());

        }
        return convertView;
    }
}
