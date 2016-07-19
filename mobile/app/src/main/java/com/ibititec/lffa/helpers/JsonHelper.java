package com.ibititec.lffa.helpers;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.modelo.Usuario;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pedro on 11/02/16.
 */
public class JsonHelper {
    public static <T> List<T> getList(String json, Class<T[]> tClass) {
        return Arrays.asList((new Gson()).fromJson(json, tClass));
    }

    public static <T> T getObject(String json, Class<T> tClass) {
        try {
            return (new Gson()).fromJson(json, tClass);
        } catch (Exception ex) {
            return  null;
        }
    }

    public static String objectToJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static String leJsonBancoLocal(String nomeJson, Activity activity) {
        try {
            String json = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(nomeJson + ".json", "");
            Log.i(MainActivity.TAG, "Lendo preferences: " + json);
            return json;
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro no metodo:  nomeJson: " + nomeJson + " - Erro: " + ex.getMessage());
            return null;
        }
    }

    public static Usuario ObterUsuarioBancoLocal(Activity activity) {
        try {
            String json = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(MainActivity.USUARIO + ".json", "");
            Usuario usuario = JsonHelper.getObject(json, Usuario.class);
            Log.i(MainActivity.TAG, "Lendo preferences: " + json);
            return usuario;
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Erro no metodo:  ObterUsuarioBancoLocal: " + ex.getMessage());
            return null;
        }
    }
}
