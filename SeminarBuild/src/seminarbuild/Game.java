/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import java.net.*;
import java.util.*;

/**
 *
 * @author Dakota
 */
public class Game {

    private Player[] activePlayers;
    private Movie[] movieList;
    private Request[] mainRequests;
    private int uptime;

    Game() {
        this.activePlayers = new Player[8];
        this.movieList = new Movie[10];
        this.mainRequests = new Request[20];
        this.uptime = 0;
    }

    public void assignMovies() {
        Random randnum = new Random();
        
        for (int i = 0; i < 10; i++) {
            Player temp = null; 
            
            try{
                temp = new Player(InetAddress.getLocalHost().getHostAddress());
            }catch(Exception e){}
            
            for (int j = 0; j < 3; j++) {
                temp.addMovie(this.movieList[randnum.nextInt()%10]);
            }
            
            this.activePlayers[i] = temp;
        }
    }
    
    public void generateRequests(){
        Random randnum = new Random();
        
        int i = randnum.nextInt()%8;
        
        this.activePlayers[i].addRequest(new Request(this.movieList[randnum.nextInt()%10].getTitle()));
    }
    
    public void sendMovie(Player p1, Player p2, Movie movie){
        if(p1.removeMovie(movie)){
            p2.addMovie(movie);
        }
    }

    public int getUptime() {
        int increase = 0;
        for (int i = 0; i < 8; i++) {
            if(this.activePlayers[i].bandAvail()>0){
                increase++;
            }
            else{
                increase--;
            }
        }
        this.uptime += increase;
        
        return this.uptime;
    }
}
