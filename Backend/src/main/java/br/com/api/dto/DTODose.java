package br.com.api.dto;

public class DTODose {
    private int id;
    private String dose;
    private int idade_recomendada_aplicacao;

    // construct
    public DTODose(int id, String dose, int idade_recomendada_aplicacao) {
        this.id = id;
        this.dose = dose;
        this.idade_recomendada_aplicacao = idade_recomendada_aplicacao;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public int getIdade_recomendada_aplicacao() {
        return idade_recomendada_aplicacao;
    }

    public void setIdade_recomendada_aplicacao(int idade_recomendada_aplicacao) {
        this.idade_recomendada_aplicacao = idade_recomendada_aplicacao;
    }
}