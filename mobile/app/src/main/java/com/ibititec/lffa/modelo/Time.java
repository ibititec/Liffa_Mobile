package com.ibititec.lffa.modelo;

import java.util.Date;
import java.util.List;

/**
 * Created by JOAOCELSON on 28/04/2016.
 */
public class Time {
    private int Id ;

    private  String Nome ;

    private  String Presidente ;

    private  String Telefone ;

    private  String Descricao ;

    private Date DataFundacao ;

    private  Boolean SelecionadoCampeonato ;

    private  String EscudoPequeno ;

    private  String EscudoGrande ;

    private List<Jogador> ListaJogadores ;

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

    public String getPresidente() {
        return Presidente;
    }

    public void setPresidente(String presidente) {
        Presidente = presidente;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Date getDataFundacao() {
        return DataFundacao;
    }

    public void setDataFundacao(Date dataFundacao) {
        DataFundacao = dataFundacao;
    }

    public Boolean getSelecionadoCampeonato() {
        return SelecionadoCampeonato;
    }

    public void setSelecionadoCampeonato(Boolean selecionadoCampeonato) {
        SelecionadoCampeonato = selecionadoCampeonato;
    }

    public String getEscudoPequeno() {
        return EscudoPequeno;
    }

    public void setEscudoPequeno(String escudoPequeno) {
        EscudoPequeno = escudoPequeno;
    }

    public String getEscudoGrande() {
        return EscudoGrande;
    }

    public void setEscudoGrande(String escudoGrande) {
        EscudoGrande = escudoGrande;
    }

    public List<Jogador> getListaJogadores() {
        return ListaJogadores;
    }

    public void setListaJogadores(List<Jogador> listaJogadores) {
        ListaJogadores = listaJogadores;
    }
}
