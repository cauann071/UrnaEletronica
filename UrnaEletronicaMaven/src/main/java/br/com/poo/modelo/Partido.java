package br.com.poo.modelo;

public class Partido {
    private int numero;
    private String nome;
    
    public Partido(int numero, String nome){
        this.numero = numero;
        this.nome = nome;
    }
    
    public void setNumero(int numero){
        this.numero = numero;
    }
    
    public int getNumero(){
        return numero;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    
}
