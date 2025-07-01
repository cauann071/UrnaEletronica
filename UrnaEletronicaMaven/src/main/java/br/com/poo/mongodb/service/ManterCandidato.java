/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.poo.mongodb.service;

import br.com.poo.mongodb.comoon.vo.Candidato;
import br.com.poo.mongodb.persistence.MongoDAO;

/**
 *
 * @author nirto
 */
public class ManterCandidato {

    MongoDAO dao = new MongoDAO();

    public Candidato findByNumber(int num) {

        return dao.findByNumber(num);
    }

}
