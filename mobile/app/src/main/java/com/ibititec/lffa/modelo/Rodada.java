package com.ibititec.lffa.modelo;

/**
 * Created by JOAOCELSON on 03/04/2016.
 */
public class Rodada {
    private String Numero;
    private String Data ;
    private String Campo ;
    private String HoraJogo1 ;
    private String Jogo1 ;
    private String HoraJogo2 ;
    private String Jogo2 ;
    private Partida Partida1 ;
    private Partida Partida2 ;

    public Partida getPartida1() {
        return Partida1;
    }

    public void setPartida1(Partida partida1) {
        Partida1 = partida1;
    }

    public Partida getPartida2() {
        return Partida2;
    }

    public void setPartida2(Partida partida2) {
        Partida2 = partida2;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCampo() {
        return Campo;
    }

    public void setCampo(String campo) {
        Campo = campo;
    }

    public String getHoraJogo1() {
        return HoraJogo1;
    }

    public void setHoraJogo1(String horaJogo1) {
        HoraJogo1 = horaJogo1;
    }

    public String getJogo1() {
        return Jogo1;
    }

    public void setJogo1(String jogo1) {
        Jogo1 = jogo1;
    }

    public String getHoraJogo2() {
        return HoraJogo2;
    }

    public void setHoraJogo2(String horaJogo2) {
        HoraJogo2 = horaJogo2;
    }

    public String getJogo2() {
        return Jogo2;
    }

    public void setJogo2(String jogo2) {
        Jogo2 = jogo2;
    }
}
