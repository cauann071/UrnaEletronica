package br.com.poo.mongodb.persistence;

import br.com.poo.mongodb.comoon.vo.Candidato;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date; // Importar para usar timestamps

/**
 *
 * @author nirto
 */
public class MongoDAO {

    // Método para estabelecer a conexão com o MongoDB e retornar o banco de dados
    // É uma boa prática fechar o cliente após o uso, ou gerenciar seu ciclo de vida
    // para aplicações maiores (ex: singleton). Para este exemplo, abrimos e fechamos por operação.
    private MongoClient mongoClient;
    private MongoDatabase database;

    private void connect() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("urnaBanco"); // Nome do seu banco de dados
        }
    }

    private void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

    /**
     * Busca um candidato pelo número.
     * @param num O número do candidato a ser buscado.
     * @return Um objeto Candidato se encontrado, ou null caso contrário.
     */
    public Candidato findByNumber(int num) {
        connect(); // Conecta ao banco de dados
        try {
            MongoCollection<Document> collection = database.getCollection("candidatos"); // Sua coleção de candidatos
            Bson filtro = Filters.eq("numero", num);
            Document candidatoDoc = collection.find(filtro).first();

            if (candidatoDoc != null) {
                Candidato vo = new Candidato();
                vo.setNome(candidatoDoc.getString("nome"));
                vo.setNumCandidato(candidatoDoc.getInteger("numero"));
                // Assumindo que você tem um campo 'partido' e 'imagePath' no seu Document
                //vo.setPartido(candidatoDoc.getString("partido")); 
               // vo.setImagePath(candidatoDoc.getString("imagePath"));
                return vo;
            } else {
                return null; // Retorna null se o candidato não for encontrado
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar candidato por número: " + e.getMessage());
            return null;
        } finally {
            close(); // Fecha a conexão
        }
    }

    /**
     * Registra um voto nulo no banco de dados.
     * @param numeroDigitado O número que foi digitado resultando em voto nulo.
     */
    public void registrarVotoNulo(String numeroDigitado) {
        connect(); // Conecta ao banco de dados
        try {
            MongoCollection<Document> collection = database.getCollection("votos"); // Coleção para armazenar votos
            Document votoNulo = new Document("tipoVoto", "NULO")
                                    .append("numeroDigitado", numeroDigitado)
                                    .append("dataHora", new Date()); // Adiciona um timestamp
            collection.insertOne(votoNulo);
            System.out.println("Voto Nulo registrado: " + numeroDigitado);
        } catch (Exception e) {
            System.err.println("Erro ao registrar voto nulo: " + e.getMessage());
        } finally {
            close(); // Fecha a conexão
        }
    }

    /**
     * Registra um voto em branco no banco de dados.
     */
    public void registrarVotoBranco() {
        connect(); // Conecta ao banco de dados
        try {
            MongoCollection<Document> collection = database.getCollection("votos"); // Coleção para armazenar votos
            Document votoBranco = new Document("tipoVoto", "BRANCO")
                                      .append("dataHora", new Date()); // Adiciona um timestamp
            collection.insertOne(votoBranco);
            System.out.println("Voto em Branco registrado.");
        } catch (Exception e) {
            System.err.println("Erro ao registrar voto em branco: " + e.getMessage());
        } finally {
            close(); // Fecha a conexão
        }
    }
}