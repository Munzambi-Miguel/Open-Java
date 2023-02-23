
import com.developer.java.controller.AutenticationJpaController;
import com.developer.java.controller.CaixaJpaController;
import com.developer.java.entity.Caixa;
import com.developer.util.Util;
import java.math.BigDecimal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hdglo
 */
public class Text {

    public static void main(String[] args) {
        
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        Caixa caixa = new Caixa();
        
        // INSERT INTO 
        // `jcomerci`.`caixa` (`initial_value`, `current_value`, `caixa_typo`, `autentication_id`, `administrador`) 
        // VALUES ('0', '0', '1', '1', '1');
        
        

        new CaixaJpaController(Util.emf).create(caixa);        
        System.out.println( new CaixaJpaController(Util.emf).findCaixaEntities() );
        // -- -- -- -- -- -- ( --~~|> -||- <|~~-- ) :-:-:-:
    }

}
