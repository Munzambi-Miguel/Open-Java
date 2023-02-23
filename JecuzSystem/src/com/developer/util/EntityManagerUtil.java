/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Sebastiao
 */
public class EntityManagerUtil {

//este eh o classe de conexao
//no main soh precisas chamar a linha abaixo
//EntityManager em = EntityManagerUtil.getEntityManager();
//    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory( "pro_vendasPU" );
    private static EntityManagerFactory emfv = null;
    private static Configuracoes config = new Configuracoes();

    public static EntityManager getEM() {
        try {
             return emfv.createEntityManager();
        } catch (Exception e) {
        }
       
        return null;
    }

    public static EntityManagerFactory getEntityManager() {
        Map<String, String> properties = new HashMap<String, String>();
        lerMeuArquivos(Util.configPath);

        properties.clear();

        //properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + config.getIp() + ":" + config.getPorta() + "/jcomerci");
        //properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost/g_vendas");
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

        properties.put("javax.persistence.jdbc.password", config.getPassword());

        //properties.put("javax.persistence.jdbc.password", "root");
        properties.put("javax.persistence.jdbc.user", config.getUsername());
        //properties.put("javax.persistence.jdbc.user", "root");

        print.Connection.ROOT = config.getUsername();
        print.Connection.PASSWORD = config.getPassword();
        print.Connection.DATABASE_URL = "jdbc:mysql://" + config.getIp() + ":" + config.getPorta() + "/jcomerci";

        Util.configs = config;

        // System.err.println("ola estava aqui com voce entende");
        // System.err.println(properties.toString());
        try {

            emfv = Persistence.createEntityManagerFactory("SystemPU", properties);
            return emfv;
        } catch (javax.persistence.PersistenceException n) {

            //            new EntityManagerUtil().handleLogin();
            // "Ocorreu Algum Problema na Comunição com a Base de Dados \n Verifique as Configuraçoes do Servidor"
            return null;

        }

    }

    public static void lerMeuArquivos(String Path) {
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

                    if (i == 4) {
                        config.setDirector(linha.replace("file:", "").replace("null", ""));
                    }
                    
                    i++;
                }

            } while (linha != null);

        } catch (Exception e) {

        }

    }
}
