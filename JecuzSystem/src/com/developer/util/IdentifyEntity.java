/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import com.developer.java.entity.Autentication;
import static com.developer.util.AES.decrypt;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author hdglo
 */
public abstract class IdentifyEntity {

    public static Autentication validation(String name, String pass) {

        try {

            Autentication autenicatio = null;

            String queryComands = "SELECT en FROM Autentication en "
                    + "WHERE en.username=:username";

            Query querys = Util.enti.createQuery(queryComands, Autentication.class).setHint(QueryHints.REFRESH, true);

            querys.setParameter("username", name);
            Autentication aut = (Autentication) querys.getSingleResult();

            String passe = AES.decrypt(decrypt(aut.getPasssword(), Util.key), Util.key);

            if (passe.toLowerCase().equals(pass.toLowerCase())) {

                autenicatio = aut;
            }

            return autenicatio;
        } catch (Exception e) {
            return null;
        }

    }

}
