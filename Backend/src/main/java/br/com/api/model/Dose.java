package br.com.api.model;

public class Dose {
    private int id;
    private int idVacina;
    private String dose;
    private int idade_recomendada_aplicacao;


    public Dose() {}


    public Dose(int id, int idVacina, String dose, int idade_recomendada_aplicacao) {
        this.id = id;
        this.idVacina = idVacina;
        this.dose = dose;
        this.idade_recomendada_aplicacao = idade_recomendada_aplicacao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
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
