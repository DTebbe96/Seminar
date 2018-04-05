/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.*;

/**
 *
 * @author Dakota
 */
public class Server {

    private ObservableList<Player> activePlayers;
    private Film[] movieList;
    private int uptime;

    Server(ObservableList<Player> players) {
        this.activePlayers = players;
        this.pullFilms();
        this.uptime = 0;
        
        try {
            this.activePlayers.add(new PcPlayer(InetAddress.getLocalHost().getHostAddress()));
        } catch (Exception e) {
            System.out.println("Unable to find Ip address");;
        }
        for (int i = 1; i < 8; i++) {
            this.activePlayers.add(new NpcPlayer("Not Available"));
        }
        this.assignFilms();
    }
    
    private void pullFilms(){
        ArrayList <Film> tempList = new ArrayList();
        BufferedReader in = null;
        
        try {
            in = new BufferedReader(new FileReader("Films.txt"));
            
            String name;
            while ((name = in.readLine()) != null){
                int bandCons = Integer.parseInt(in.readLine());
                int dur = Integer.parseInt(in.readLine());
                
                Film tempFilm = new Film(name, bandCons, dur);
                tempList.add(tempFilm);
            }
        } catch (Exception e) {
            System.out.println("Error reading from file");
        }
        this.movieList = tempList.toArray(new Film[tempList.size()]);
    }

    private void assignFilms() {
//        PcPlayer test = null;
//        try{
//            test = new PcPlayer(InetAddress.getLocalHost().getHostAddress());
//        }catch(Exception e){}
//        test.addMovie(new Film("Lilt", 1, 20));
//        test.addMovie(new Film("Lilt", 1, 10));
//        test.addMovie(new Film("Lilt", 1, 50));
//        
//        activePlayers.add(test);
        
        Random randnum = new Random();
        
        for (int i = 0; i < this.activePlayers.size(); i++) {
            Player tempPlay = (Player)this.activePlayers.get(i);
            
            while(tempPlay.cacheAvail() > 0){
                Film tempFilm = this.movieList[randnum.nextInt(this.movieList.length)];
                System.out.println("Giving " + tempPlay.getName() + " Film " + tempFilm.getTitle());
                boolean success = tempPlay.addMovie(tempFilm);
                if(!success){
                    System.out.println("Already has film in cache");
                }
            }
        }
    }
    
    public void accept(FilmRequest request){
        System.out.println("Accepting " + request.getFilm().getTitle());
        this.activePlayers.get(0).acceptRequest(request);
    }
    
    private void addRequests(){
        Random randnum = new Random();
        
        int i = 0;
        Film tempFilm = this.movieList[randnum.nextInt(this.movieList.length)];
        FilmRequest tempReq = new FilmRequest(tempFilm, tempFilm.getTitle());
        Player temp = (Player)this.activePlayers.get(i);
        temp.addRequest(tempReq);
    }
    
//    public void sendMovie(PcPlayer p1, PcPlayer p2, Film movie){
//        if(p1.removeMovie(movie)){
//            p2.addMovie(movie);
//        }
//    }

    public ObservableList<Player> getPlayers() {
        return activePlayers;
    }

    public int getUptime() {
        int increase = 0;
        for (int i = 0; i < activePlayers.size(); i++) {
            if(this.activePlayers.get(i).bandAvail()>0){
                increase++;
            }
            else{
                increase--;
            }
        }
        this.uptime += increase;
        
        return this.uptime;
    }
    
    public void update(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this.activePlayers.get(0).getRequests().size() < 2){
            this.addRequests();
            this.addRequests();
        }
        
        System.out.println(this.activePlayers.get(0).getName());
        System.out.println("Available Cache: " + this.activePlayers.get(0).cacheAvail());
        System.out.println("Available Bandwith: " + this.activePlayers.get(0).bandAvail());
        System.out.println("uptime: " + this.getUptime());
        System.out.println("Requests:");
        
        Player temp = (Player)this.activePlayers.get(0);
        ObservableList<FilmRequest> Requests = temp.getRequests();
        for (FilmRequest request : Requests) {
            Film film = request.getFilm();
            System.out.println("------------------");
            System.out.println(" -Title: " + film.getTitle());
            System.out.println("  -Bandwidth Consumption: " + film.getBandConsum());
            System.out.println("  -Duration: " + request.getTime());
        }
        temp.update();
        
    }

}
