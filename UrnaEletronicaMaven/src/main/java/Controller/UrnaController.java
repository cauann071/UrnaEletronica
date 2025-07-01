package Controller;

import javaapplication1.UrnaEletronica;
import javax.swing.JLabel;

public class UrnaController {
    private StringBuilder numDigitados = new StringBuilder();
     
    public StringBuilder getNumDigitados() {
        return numDigitados;
    }
    
    public void adicionarDigitos(String num){
        numDigitados.append(num);
        
    }
    
    public void limpar(){
        numDigitados.setLength(0);
    }
}
