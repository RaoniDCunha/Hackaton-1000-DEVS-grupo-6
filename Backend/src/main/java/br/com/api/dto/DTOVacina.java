package br.com.api.dto;

import java.util.List;

public class DTOVacina {
    private int id;
    private String vacina;
    private String descricao;
    private Integer limite_aplicacao;
    private String publico_alvo;
    private List<DTODose> doses; // -- lista de doses associadas

    // Construtor
    public DTOVacina(int id, String vacina, String descricao, Integer limite_aplicacao, String publico_alvo, List<DTODose> doses) {
        this.id = id;
        this.vacina = vacina;
        this.descricao = descricao;
        this.limite_aplicacao = limite_aplicacao;
        this.publico_alvo = publico_alvo;
        this.doses = doses;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVacina() {
        return vacina;
    }

    public void setVacina(String vacina) {
        this.vacina = vacina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLimite_aplicacao() {
        return limite_aplicacao;
    }

    public void setLimite_aplicacao(Integer limite_aplicacao) {
        this.limite_aplicacao = limite_aplicacao;
    }

    public String getPublico_alvo() {
        return publico_alvo;
    }

    public void setPublico_alvo(String publico_alvo) {
        this.publico_alvo = publico_alvo;
    }

    public List<DTODose> getDoses() {
        return doses;
    }

    public void setDoses(List<DTODose> doses) {
        this.doses = doses;
    }
}