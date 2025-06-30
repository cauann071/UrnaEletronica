package br.com.poo.modelo;

public class Candidato {
    private Partido partidoCandidato;
    private String nome;
    private int numCandidato;
    
    public Candidato(String nome,Partido partidoCandidato, int numCandidato){
        this.nome = nome;
        this.partidoCandidato = partidoCandidato;
        this.numCandidato = numCandidato;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return nome; 
   }
    
    public void setNumCandidato(int numCandidato){
        this.numCandidato = numCandidato;
    }
    
    public int getNumCandidato(){
        return numCandidato; 
   }

    @Override
    public String toString() {
        
        return String.format("%d%03d", partidoCandidato.getNumero(), getNumCandidato());
    }
    
}
