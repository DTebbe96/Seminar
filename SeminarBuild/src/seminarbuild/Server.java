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
public class Server implements Runnable{

    private ObservableList<Player> activePlayers;
    private Film[] movieList;
    private int uptime;
    private int downtime;
    private String[] users;
    private int round;
    
    private BufferedWriter out;
    
    private FXMLController ui;

    Server(ObservableList<Player> players, FXMLController ui) {
        this.activePlayers = players;
        this.pullFilms();
        this.pullUsers();
        this.uptime = 0;
        this.downtime = 1;
        
        this.ui = ui;

        this.round = 0;

        try {
            this.activePlayers.add(new PcPlayer(InetAddress.getLocalHost().getHostAddress()));
            this.out = new BufferedWriter(new FileWriter("log.txt"));
        } catch (Exception e) {
            System.out.println("Unable to find Ip address");;
        }
        for (int i = 1; i < 8; i++) {
            this.activePlayers.add(new NpcPlayer("Not Available"));
        }
        this.assignFilms();
        this.addRequests();
    }
    
    private void pullUsers(){
        ArrayList<String> tempList = new ArrayList();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader("Users.txt"));

            String name;
            while ((name = in.readLine()) != null) {
                tempList.add(name);
            }
        } catch (Exception e) {
            System.out.println("Error reading from file");
        }
        this.users = tempList.toArray(new String[tempList.size()]);
    }
    

    private void pullFilms() {
        ArrayList<Film> tempList = new ArrayList();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader("Films.txt"));

            String name;
            while ((name = in.readLine()) != null) {
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

        Random randnum = new Random();

        for (int i = 0; i < this.activePlayers.size(); i++) {
            Player tempPlay = (Player) this.activePlayers.get(i);

            while (tempPlay.cacheAvail() > 0) {
                Film tempFilm = this.movieList[randnum.nextInt(this.movieList.length)];
                try {
                    this.out.write("Giving player " + tempPlay.getName() + " Film " + tempFilm.getTitle());
                } catch (IOException ex) {
                    System.out.println("Couldn't output to file");
                }
                boolean success = tempPlay.addMovie(tempFilm);
                if (!success) {
                    try {
                        this.out.write("Already has film in cache");
                    } catch (IOException ex) {
                        System.out.println("Couldn't output to file");
                    }
                }
            }
        }
    }

    public void accept(FilmRequest request) {
        System.out.println("Accepting " + request.getFilm().getTitle());
        this.activePlayers.get(0).acceptRequest(request);
    }

    private void addRequests() {
        Random randnum = new Random();

        for (int i = 0; i < this.activePlayers.size(); i++) {
            while (this.activePlayers.get(i).numRequests() < 5) {
                Film tempFilm = this.movieList[randnum.nextInt(this.movieList.length)];
                FilmRequest tempReq = new FilmRequest(tempFilm, this.users[randnum.nextInt(this.users.length)]);
                Player temp = (Player) this.activePlayers.get(i);
                temp.addRequest(tempReq);
            }
        }

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
            if (this.activePlayers.get(i).bandAvail() > 0) {
                increase++;
            } else {
                increase--;
            }
        }
        this.uptime += increase;

        return (int)Math.ceil((1-(this.downtime/this.uptime))*100);
    }

    public int totalRequests() {
        int total = 0;

        for (int i = 0; i < this.activePlayers.size(); i++) {
            total += this.activePlayers.get(i).numRequests();
        }
        
        if (total == 0){
            System.exit(0);
        }

        return total;
    }

    public void update() {
        try {
            this.out.write("Tick " + this.round + "\n");
        } catch (Exception e) {
            System.out.println("Unable to write to file");
        }
        for (int i = 0; i < this.activePlayers.size(); i++) {
            try {
                this.out.write(this.activePlayers.get(i).getName());
                this.out.write("Available Cache: " + this.activePlayers.get(1).cacheAvail()+ "\n");
                this.out.write("Available Bandwith: " + this.activePlayers.get(1).bandAvail()+ "\n");
                this.out.write("uptime: " + this.getUptime()+ "\n");
                this.out.write("Requests:\n");
                
                Player temp = (Player) this.activePlayers.get(i);
                ObservableList<FilmRequest> Requests = temp.getRequests();
                for (FilmRequest request : Requests) {
                    Film film = request.getFilm();
                    this.out.write("------------------\n");
                    this.out.write(" -Title: " + film.getTitle()+ "\n");
                    this.out.write("  -Bandwidth Consumption: " + film.getBandConsum()+ "\n");
                    this.out.write("  -Duration: " + request.getTime()+ "\n");
                }
                temp.update();
            } catch (Exception e) {
                System.out.println("Unable to write to file");
            }
        }

        ui.update();

    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Sleep failed");;
            }
            this.update();
        }
    }

}
