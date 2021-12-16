/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AL Lewaa Company
 */
public class ChatHandler extends Thread{
    
    PrintStream ps;
    DataInputStream dis;
    static Vector<ChatHandler> clients = new Vector<ChatHandler>();
    static Vector<String> messages = new Vector<String>();
    
    public ChatHandler(Socket cli)
    {
        try {
            dis = new DataInputStream(cli.getInputStream());
            ps = new PrintStream(cli.getOutputStream());
            if(clients.isEmpty())
            {
                clients.add(this);
            }
            else
            {
                clients.add(this);
                AddPrvMsgs(this);
            }
            
            start();
        } catch (IOException ex) {
            Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    void AddPrvMsgs(ChatHandler temp)
    {
        for(String s : messages)
        {
            temp.ps.println(s);
        }
    }
    
    void SendMsgToAll(String msg)
    {
        
        for(ChatHandler temp : clients)
        {
            temp.ps.println(msg);
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try {
                String msg = dis.readLine();
                messages.add(msg);
                SendMsgToAll(msg);
            } catch (IOException ex) {
                Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
  
    
}
