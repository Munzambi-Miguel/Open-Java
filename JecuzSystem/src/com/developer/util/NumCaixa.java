/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author hdglo
 */
public class NumCaixa {

    public static int gatNunCaixa() throws UnknownHostException {

        String ip = InetAddress.getLocalHost().getHostAddress();
        String num = "0";
        for (int i = ip.length() - 1; i > 0; i--) {
            if (ip.charAt(i) != '.') {

                num += ip.charAt(i);
                return Integer.parseInt(num);
            }

        }
        return 0;

    }

    
}
