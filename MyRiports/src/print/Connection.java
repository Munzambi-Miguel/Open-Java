/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author hdglo
 */
public abstract class Connection {

    //static Configuracoes config = new Configuracoes();
    public final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static String DATABASE_URL = "jdbc:mysql://localhost/jcomerci";
    public static java.sql.Connection connection;
    public static Statement statement = null;
    public static String ROOT = "root";
    public static String PASSWORD = "";

    public static void openConnetion() throws SQLException {

       // DATABASE_URL = "jdbc:mysql://" + config.getIp() +  "/jcomerci";

      //  ROOT = config.getUsername();
      //  PASSWORD = config.getPassword();

        connection = DriverManager.getConnection(DATABASE_URL, ROOT, PASSWORD);
        statement = connection.createStatement();
    }

    public static void closeConnection() throws SQLException {
        connection.close();
        statement.close();
    }

    public static java.sql.Connection getConnection() throws SQLException {
        openConnetion();
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    /*
    public void lerMeuArquivos(String Path) {
        try {

            FileInputStream arquivo = new FileInputStream(Path);
            InputStreamReader input = new InputStreamReader(arquivo);
            BufferedReader br = new BufferedReader(input);
            String linha;
            int i = 0;
            do {
                linha = br.readLine();
                if (linha != null) {

                    if (i == 0) {
                        config.setIp(linha.replace("ip:", ""));
                    }

                    if (i == 1) {
                        config.setPorta(linha.replace("port:", ""));
                    }
                    if (i == 2) {
                        config.setUsername(linha.replace("user:", ""));
                    }
                    if (i == 3) {
                        config.setPassword(linha.replace("pass:", "").replace("null", ""));
                    }

                    i++;
                }

            } while (linha != null);

        } catch (Exception e) {

        }

    }

    */

 

    
}
