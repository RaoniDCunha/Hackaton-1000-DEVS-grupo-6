package br.com.api.dto;

import java.sql.Date;

public class DTOImunizacao {
    private int id;
    private String nomePaciente; // Novo campo
    private String nomeVacina;   // Novo campo
    private String nomeDose;     // Novo campo
    private Date dataAplicacao;
    private String fabricante;
    private String lote;
    private String localAplicacao;
    private String profissionalAplicador;

    // Construtor
    public DTOImunizacao(int id, String nomePaciente, String nomeVacina, String nomeDose, Date dataAplicacao, String fabricante, String lote, String localAplicacao, String profissionalAplicador) {
        this.id = id;
        this.nomePaciente = nomePaciente;
        this.nomeVacina = nomeVacina;
        this.nomeDose = nomeDose;
        this.dataAplicacao = dataAplicacao;
        this.fabricante = fabricante;
        this.lote = lote;
        this.localAplicacao = localAplicacao;
        this.profissionalAplicador = profissionalAplicador;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public String getNomeDose() {
        return nomeDose;
    }

    public void setNomeDose(String nomeDose) {
        this.nomeDose = nomeDose;
    }

    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLocalAplicacao() {
        return localAplicacao;
    }

    public void setLocalAplicacao(String localAplicacao) {
        this.localAplicacao = localAplicacao;
    }

    public String getProfissionalAplicador() {
        return profissionalAplicador;
    }

    public void setProfissionalAplicador(String profissionalAplicador) {
        this.profissionalAplicador = profissionalAplicador;
    }
}