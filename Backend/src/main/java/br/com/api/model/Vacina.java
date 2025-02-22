package br.com.api.model;
import java.util.Arrays;
import java.util.List;

public class Vacina {
    private int id;
    private String vacina;
    private String descricao;
    private Integer limite_aplicacao;
    private String publico_alvo;

    // lista de valores válidos para publicoAlvo
    private static final List<String> PUBLICOS_VALIDOS = Arrays.asList("CRIANCA", "CRIANÇA","ADOLESCENTE", "ADULTO", "GESTANTE");


    public Vacina() {}


    public Vacina(int id, String vacina, String descricao, Integer limite_aplicacao, String publico_alvo) {
        this.id = id;
        this.vacina = vacina;
        this.descricao = descricao;
        this.limite_aplicacao = limite_aplicacao;
        this.setPublico_alvo(publico_alvo); // Usando o setter para validar
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
        if (publico_alvo != null && PUBLICOS_VALIDOS.contains(publico_alvo)) {
            this.publico_alvo = publico_alvo;
        } else {
            throw new IllegalArgumentException("Público-alvo inválido. Valores permitidos: CRIANCA,  ADOLESCENTE, ADULTO, GESTANTE.");
        }
    }
}
