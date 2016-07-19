package com.ibititec.lffa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;
import com.ibititec.lffa.aovivo.PartidaTempoRealActivity;
import com.ibititec.lffa.modelo.Noticia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JOAOCELSON on 04/05/2016.
 */
public class AdapterNoticia extends BaseAdapter {
    private Activity activity;
    private List<Noticia> noticiaList = null;

    public AdapterNoticia(Activity activityParam, List<Noticia> noticiaList) {
        this.noticiaList = noticiaList;
        this.activity = activityParam;
    }

    public AdapterNoticia() {
    }

    @Override

    public int getCount() {
        return noticiaList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticiaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final Noticia noticia = noticiaList.get(position);
            Log.i(MainActivity.TAG, "Vai setar o Adapter, número de registro: " + noticiaList.size() + " Position: " + position + " - Titulo Noticia " + noticia.getTitulo()+ " - Corpo Noticia " + noticia.getCorpo());

            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.adapter_noticia, null);

            TextView titulo = (TextView) layout.findViewById(R.id.txtTituloFeedNoticias);
            titulo.setTypeface(null, Typeface.BOLD);
            TextView Corpo = (TextView) layout.findViewById(R.id.txtCorpoFeedNoticias);
            if(noticia.getDataNoticia()!=null) {
                titulo.setText("Título: " + noticia.getTitulo());

                Corpo.setText(noticia.getCorpo() + " Data: " + noticia.getDataNoticia().substring(0, 10));
            }
            return layout;

        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro ao preecnher o getView: " + e.getMessage());

        }
        return convertView;
    }

    private void startarActivityPalpite(View v, Noticia noticia) {
        Intent intent = new Intent(activity.getApplication(), PartidaTempoRealActivity.class);
        intent.putExtra("noticia", (Serializable) noticia);
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
