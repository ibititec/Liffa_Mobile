package com.ibititec.lffa.modelo;

import java.io.Serializable;

/**
 * Created by JOAOCELSON on 28/04/2016.
 */
public class Bolao implements Serializable {

    private  int Id ;
    private  String Nome ;
    private  int GolVisitante ;
    private  int GolMandante ;
    private  int PontosAdquiridos ;
    private  Usuario Usuario ;
    private  Partida Partida ;
    private  Campeonato Campeonatos ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getGolVisitante() {
        return GolVisitante;
    }

    public void setGolVisitante(int golVisitante) {
        GolVisitante = golVisitante;
    }

    public int getGolMandante() {
        return GolMandante;
    }

    public void setGolMandante(int golMandante) {
        GolMandante = golMandante;
    }

    public int getPontosAdquiridos() {
        return PontosAdquiridos;
    }

    public void setPontosAdquiridos(int pontosAdquiridos) {
        PontosAdquiridos = pontosAdquiridos;
    }

    public com.ibititec.lffa.modelo.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(com.ibititec.lffa.modelo.Usuario usuario) {
        Usuario = usuario;
    }

    public com.ibititec.lffa.modelo.Partida getPartida() {
        return Partida;
    }

    public void setPartida(com.ibititec.lffa.modelo.Partida partida) {
        Partida = partida;
    }

    public Campeonato getCampeonatos() {
        return Campeonatos;
    }

    public void setCampeonatos(Campeonato campeonatos) {
        Campeonatos = campeonatos;
    }
}
