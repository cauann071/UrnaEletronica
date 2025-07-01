package br.com.poo.mongodb.comoon.vo;

public class Candidato {
    //private Partido partidoCandidato;
    private String nome;
    private int numCandidato;
    
    
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

    //@Override
    //public String toString() {
        
       // return String.format("%d%03d", partidoCandidato.getNumero(), getNumCandidato());
    //}
    
}
