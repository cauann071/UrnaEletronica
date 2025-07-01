package br.com.poo.modelo;

import br.com.poo.mongodb.comoon.vo.Candidato;
import br.com.poo.mongodb.comoon.vo.Candidato;
import java.util.ArrayList;
import java.util.List;

public class SistemaCandidatos {
    private List<Candidato> candidatos = new ArrayList<>();
    
    public void adicionarVereador(Candidato vereador){
        candidatos.add(vereador);
    }
    
    public void listarVereadores(){
        
        for (int i = 0; i < candidatos.size(); i++) {
            Candidato allVereadores = candidatos.get(i);
            
            System.out.println("=================================================");
            System.out.println("Nome:" + candidatos.get(i).getNome());
            System.out.println("Partido:" + candidatos.get(i).toString());
        }
    }
}
