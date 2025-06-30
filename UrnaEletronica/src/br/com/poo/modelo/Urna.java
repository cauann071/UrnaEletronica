package br.com.poo.modelo;

import br.com.poo.modelo.Partido;
import br.com.poo.modelo.SistemaCandidatos;
import br.com.poo.modelo.Candidato;
import br.com.poo.modelo.Candidato;
import br.com.poo.modelo.Partido;

public class Urna {

    public static void main(String[] args) {
        
        SistemaCandidatos sistema = new SistemaCandidatos();
        
        Partido partido = new Partido(91, "PEsp");
        Candidato candidato1 = new Candidato("Baisebol", partido, 001);
        
        Partido partido2 = new Partido(91, "PEsp");
        Candidato candidato2 = new Candidato("Basquete", partido2, 002);
        
        Partido partido3 = new Partido(91, "PEsp");
        Candidato candidato3 = new Candidato("Futebol", partido3, 003);
        
        Partido partido4 = new Partido(91, "PEsp");
        Candidato candidato4 = new Candidato("Futemesa", partido4, 004);
        
        sistema.adicionarVereador(candidato1);
        sistema.adicionarVereador(candidato2);
        sistema.adicionarVereador(candidato3);
        sistema.adicionarVereador(candidato4);
        
        sistema.listarVereadores();
    }
}
