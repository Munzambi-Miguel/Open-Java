/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author hdglo
 */
public class MyRiports {

    public static void printRecibo(int id, Empresa em) throws JRException, SQLException {

        // print.jasperFile
        URL path = MyRiports.class.getResource("jasperFile/factura_termica.jasper");
        HashMap map = new HashMap();
        if (em.getEmail() == null) {
            map.put("id", "" + id);
            map.put("nome_loja", "" + em.getNome());
            map.put("n_contribuente", "" + em.getnContribuente());
            map.put("n_telefonico", "" + em.getTelefone());
            map.put("email_loja", "" + em.getEmail());
            map.put("localizacao_rua", "" + em.getLocalizacao());
        }

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(path);

        JasperPrint jp = JasperFillManager.fillReport(jasperReport, map, Connection.getConnection());

        int numeroPaginas = jp.getPages().size();
        JasperPrintManager.printPages(jp, 0, numeroPaginas - 1, false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            printRecibo(1, new Empresa());
        } catch (SQLException | JRException ex) {
            Logger.getLogger(MyRiports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
