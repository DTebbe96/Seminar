/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seriesoftubes;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Dakota
 */
public class Connection implements Runnable{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String inputString;
    private String outputString;
    
    Connection(){
        try{
            ServerSocket serv = new ServerSocket(8080);
            this.socket = serv.accept();
            System.out.println("connected");
        }catch(Exception exc){
            System.out.println("Can't find client");
        }
    }
    
    Connection(String ip){
        try{
            this.socket = new Socket (ip, 8080);
            System.out.println("connected");
        }catch(Exception exc){
            System.out.println("Can't find host");
        }
    }
    
    @Override
    public void run(){
        try{
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
            
            while(true){
                //this.output();
                //this.input();
                
                //this.inputString = null;
                //this.outputString = null;
            }
            
        }catch(Exception exc){
            System.out.println("Error connecting");
        }
    }
    
    public void output(){
        if(this.outputString==null){
            return;
        }
        try{
            byte[] bytes = this.outputString.getBytes();
            this.out.writeInt(bytes.length);
            this.out.write(bytes);
        }catch(Exception exc){
            System.out.println("Can't output");
            exc.printStackTrace();
        }
    }
    
    public void setOutput(String output){
        this.outputString = output;
    }
    
    public void input(){
        
        try{
            int read = in.readInt();
            byte[] bytes = new byte[read];
            in.readFully(bytes, 0, bytes.length);
            this.inputString = new String(bytes);
            
        }catch(Exception exc){
            System.out.println("Can't input");
            exc.printStackTrace();
        }
    }
    
    public String getInput(){
        return this.inputString;
    }
}
