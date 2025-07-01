/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.poo.mongodb.persistence;

import br.com.poo.mongodb.comoon.vo.Candidato;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author nirto
 */
public class MongoDAO {

    private MongoDatabase conection() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("urnaBanco");
        return database;
    }

    public Candidato findByNumber(int num) {
        MongoDatabase database = conection();
        MongoCollection<Document> collection = database.getCollection("candidatos");
        Bson filtro = Filters.eq("numero", num);
        Document candidatoDoc = collection.find(filtro).first();

        Candidato vo = new Candidato();
        vo.setNome(candidatoDoc.getString("nome"));
        vo.setNumCandidato(candidatoDoc.getInteger("numero"));
        return vo;
    }

}
