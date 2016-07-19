package com.ibititec.lffa.helpers;

import android.location.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pedro on 12/02/16.
 */
public class LocationHelper {
    public static String formataLatitude(double coordenada) {
        String orientacao = coordenada > 0 ? "N" : (coordenada < 0 ? "S" : "");
        return formataCoordenada(coordenada, orientacao);
    }

    public static String formataLongitude(double coordenada) {
        String orientacao = coordenada > 0 ? "E" : (coordenada < 0 ? "W" : "");
        return formataCoordenada(coordenada, orientacao);
    }

    private static String formataCoordenada(double coordenada, String orientacao) {
        Pattern p = Pattern.compile("(\\d+):(\\d+):(\\d+)");

        Matcher m = p.matcher(Location.convert(coordenada, Location.FORMAT_SECONDS));

        String result = "";

        if (m.find()) {
            result = String.format("%s° %s′ %s″", m.group(1), m.group(2), m.group(3));

            if (!orientacao.isEmpty())
                result += " " + orientacao;
        }

        return result;
    }
}