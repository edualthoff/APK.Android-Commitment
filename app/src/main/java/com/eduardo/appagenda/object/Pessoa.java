package com.eduardo.appagenda.object;

public class Pessoa {

    private int id_pessoa;
    private String nome;
    private String sobrenome;
    private String email;
    private Long tel;

    public  Pessoa(){}

    public Pessoa(int id, String s, String s1, String s2, Long i) {
        this.id_pessoa = id;
        this.nome = s;
        this.sobrenome = s1;
        this.email = s2;
        this.tel = i;
    }
    public Pessoa( String s, String s1, String s2, Long i) {
        this.nome = s;
        this.sobrenome = s1;
        this.email = s2;
        this.tel = i;
    }
    public int getId_pessoa() {
        return id_pessoa;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id_pessoa=" + id_pessoa +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", tel=" + tel +
                '}';
    }
}
