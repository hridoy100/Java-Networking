/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hridoy100;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Hridoy
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server Started...");
        System.out.println(InetAddress.getLocalHost());
        HashMap<String, Information> clientList = new HashMap<String, Information>();

        while (true) {
            Socket socket = serverSocket.accept();
            NetworkConnection nc = new NetworkConnection(socket);

            new Thread(new CreateConnection(clientList, nc)).start();

        }

    }
}
