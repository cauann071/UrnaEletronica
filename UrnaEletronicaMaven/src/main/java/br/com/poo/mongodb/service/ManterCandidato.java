package br.com.poo.mongodb.service;

import br.com.poo.mongodb.comoon.vo.Candidato;
import br.com.poo.mongodb.persistence.MongoDAO;

public class ManterCandidato {

    private MongoDAO dao;

    public ManterCandidato() {
        dao = new MongoDAO();
    }

    public Candidato findByNumber(int num) {
        return dao.findByNumber(num);
    }

    // Novo método para registrar voto nulo
    public void registrarVotoNulo(String numeroDigitado) {
        dao.registrarVotoNulo(numeroDigitado);
    }

    // Novo método para registrar voto em branco
    public void registrarVotoBranco() {
        dao.registrarVotoBranco();
    }
}