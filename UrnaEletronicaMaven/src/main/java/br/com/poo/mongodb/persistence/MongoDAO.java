package br.com.poo.mongodb.persistence;

import br.com.poo.mongodb.comoon.vo.Candidato;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Updates;
import java.util.Date; 
import java.util.HashMap;
import java.util.Map;
import com.mongodb.client.AggregateIterable; 
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
            
        try {
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                database = mongoClient.getDatabase("urnaBanco"); // Nome do seu banco de dados
                // Garante que a coleção 'votos' existe. É boa prática criá-la se não existir.
                createCollectionIfNotExists("votos");
                // Garante que a coleção 'candidatos' existe.
                createCollectionIfNotExists("candidatos");
            } catch (Exception e) {
                System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
                // Lidar com o erro de conexão, talvez relançar uma RuntimeException
                // ou ter um mecanismo de retry.
            }
        } 
    }

    private void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

    private void createCollectionIfNotExists(String collectionName) {
        boolean exists = false;
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            database.createCollection(collectionName);
            System.out.println("Coleção '" + collectionName + "' criada com sucesso.");
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
    public void registrarVoto(Candidato candidato) {
        connect();
        try {
            // Opção 1: Registrar o voto individual na coleção 'votos'
            MongoCollection<Document> votosCollection = database.getCollection("votos");
            Document votoDoc = new Document("tipoVoto", "CANDIDATO")
                                    .append("numeroCandidato", candidato.getNumCandidato())
                                    .append("nomeCandidato", candidato.getNome())
                                    .append("dataHora", new Date());
            votosCollection.insertOne(votoDoc);
            System.out.println("Voto individual para " + candidato.getNome() + " registrado na coleção 'votos'.");

            // Opção 2: Incrementar o contador de votos na coleção 'candidatos'
            // Isso é útil para ter um total de votos por candidato diretamente no perfil dele.
            MongoCollection<Document> candidatosCollection = database.getCollection("candidatos");
            Bson filtro = Filters.eq("numero", candidato.getNumCandidato());
            Bson atualizacao = Updates.inc("votos", 1); // Incrementa o campo 'votos' em 1.
                                                        // Se 'votos' não existir, ele será criado com valor 1.
            candidatosCollection.updateOne(filtro, atualizacao);
            System.out.println("Contador de votos do candidato " + candidato.getNome() + " incrementado.");

        } catch (Exception e) {
            System.err.println("Erro ao registrar voto para o candidato " + candidato.getNome() + ": " + e.getMessage());
        } finally {
            close();
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
    
     public Map<String, Long> gerarRelatorioVotos() {
        connect();
        Map<String, Long> relatorio = new HashMap<>();
        try {
            MongoCollection<Document> votosCollection = database.getCollection("votos");
            MongoCollection<Document> candidatosCollection = database.getCollection("candidatos");

            // Contar votos brancos
            long votosBrancos = votosCollection.countDocuments(Filters.eq("tipoVoto", "BRANCO"));
            relatorio.put("votosBrancos", votosBrancos);

            // Contar votos nulos
            long votosNulos = votosCollection.countDocuments(Filters.eq("tipoVoto", "NULO"));
            relatorio.put("votosNulos", votosNulos);

            // Contar votos válidos (somando os campos 'votos' dos candidatos)
            // Ou, alternativamente, contando os documentos com tipoVoto CANDIDATO
            long votosValidosPorRegistro = votosCollection.countDocuments(Filters.eq("tipoVoto", "CANDIDATO"));
            relatorio.put("votosValidos", votosValidosPorRegistro);

            // Exemplo de como obter votos por candidato diretamente da coleção de candidatos:
            // Isso assume que o campo "votos" na coleção "candidatos" é atualizado corretamente.
            System.out.println("\n--- Votos por Candidato (Detalhado) ---");
            candidatosCollection.find().forEach(doc -> {
                String nome = doc.getString("nome");
                int numero = doc.getInteger("numero");
                // Garante que o campo 'votos' existe e é um número (Long, Integer, Double)
                // Usar get("votos", Long.class) é mais seguro para diferentes tipos numéricos
                Long votos = doc.get("votos", Long.class);
                if (votos == null) {
                    votos = 0L; // Se o campo não existir, assume 0 votos
                }
                relatorio.put("votosCandidato_" + numero + "_" + nome, votos);
                System.out.println("Candidato " + numero + " - " + nome + ": " + votos + " votos.");
            });

        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de votos: " + e.getMessage());
        } finally {
            close();
        }
        return relatorio;
    }
   
}