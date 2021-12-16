/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AL Lewaa Company
 */
public class ServerMulti {
    
    public ServerMulti()
    {
        ServerSocket ss;
        Socket mysocket;
        try {
            ss = new ServerSocket(5005);
            while(true)
            {
                mysocket = ss.accept();
                new ChatHandler(mysocket);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   
    public static void main(String args[])
    {
        new ServerMulti();
    }
    
}
