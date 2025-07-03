package br.com.poo.mongodb.service;

import br.com.poo.mongodb.comoon.vo.Candidato;
import br.com.poo.mongodb.persistence.MongoDAO;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
public class ManterCandidato {

    private MongoDAO dao;

    public ManterCandidato() {
        dao = new MongoDAO();
    }

    public Candidato findByNumber(int num) {
        return dao.findByNumber(num);
    }

    public void registrarVoto(Candidato candidato) {
        if (candidato != null) {
            dao.registrarVoto(candidato);
        } else {
            System.err.println("Erro: Tentativa de registrar voto para candidato nulo.");
        }
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