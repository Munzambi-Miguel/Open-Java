/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class GetMacAdress extends JFrame {

    private static final long serialVersionUID = 1L;

    public static String getMack() {
        try {
            // Obtém o objeto que representa o endereço da máquina local
            InetAddress ip = InetAddress.getLocalHost();

            // Através do objeto obtido, obtém o objeto que representa a interface de rede
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            // Recupera o endereço físico
            byte[] mac = network.getHardwareAddress();

            // Obtém o endereço do byte obtido
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                // Essa parte, o "mágica" está no format utilizando o formato "%02X%s"
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }

            // Imprime o endereço obtido
            return sb.toString();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}
