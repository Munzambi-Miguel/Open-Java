package com.developer.util;

import java.io.*;

public abstract class MeusFicheiros implements Serializable {

    
   

    /// Verificando se ficheiro tem conteudo ou nao
    public static boolean vazio(String path) {
        Object object = new Object();
        try (FileInputStream fis = new FileInputStream(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                object = ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            return true; // retorna verdadeiro caso nao tenha conteudo
        }
        return false;
    }

    public static void gravarDados(Object object, String path) {

        try (FileOutputStream fos = new FileOutputStream(path)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(object);
                oos.close();
            }
        } catch (Exception e) {
           
        }
    }

    public static Object carregarDados(String path) {
        Object object = new Object();
        try (FileInputStream fich = new FileInputStream(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(fich)) {

                object = ois.readObject();
                ois.close();
            }
        } catch (Exception e) {

           

        }
        return object;
    }

}
