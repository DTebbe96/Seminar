/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import java.util.*;
import javafx.collections.*;

/**
 *
 * @author Dakota
 */
public abstract class Player {

    private String username;
    private ObservableList<Film> availableFilms;
    private int maxCache;
    private int bandwidth;
    protected ObservableList<FilmRequest> requests;
    private String ip;
    public boolean transfer = false;
    public FilmRequest transFilm = null;

    Player(String ip) {
        this.ip = ip;
        this.username = "Player " + (int) new Random().nextInt(500);
        Random randnum = new Random();
        this.bandwidth = 5;
        this.maxCache = 4;
        this.availableFilms = FXCollections.observableArrayList();
        this.requests = FXCollections.observableArrayList();
    }
    
    
    //returns the number of requests for the current player
    public int numRequests(){
        int num = 0;
        
        if(this.requests.size()>0){
            num+= this.requests.size();
        }
        
        return num;
    }
    
    //Returns the Player's name
    public String getName() {
        return this.username;
    }
    
    //Returns the player's filmCache
    public ObservableList getFilmCache() {
        return this.availableFilms;
    }
    
    //Sets the player's name
    public void setName(String name) {
        this.username = name;
    }

    //Returns an observable list of the player's requests
    public ObservableList<FilmRequest> getRequests() {
        return requests;
    }
    
    //Sets the player's requests to a given ObservableList of FilmRequests
    public void setRequests(ObservableList<FilmRequest> requests) {
        this.requests = requests;
    }

    //returns the current available film cache
    public int cacheAvail() {
        return this.maxCache - this.availableFilms.size();
    }

    //calculates and returns the available bandwidth for the player
    public int bandAvail() {
        int usedBand = 0;

        for (int i = 0; i < this.availableFilms.size(); i++) {
            usedBand += this.availableFilms.get(i).getBandConsum();
        }
        return this.bandwidth - usedBand;
    }
    
    //Returns the location of a film in the cache
    public int findFilm(Film f){
        return this.availableFilms.indexOf(f);
    }

    //Adds a new film to the cache
    public boolean addMovie(Film film) {
        if (!this.availableFilms.contains(film)) {
            this.availableFilms.add(film);
            return true;
        } else {
            return false;
        }
    }
    //code for removing a film from the cache, currently unused
//    public boolean removeMovie(Film film){
//        if(this.availableFilms.containsValue(film)){
//            this.availableFilms.remove(film.getTitle(), film);
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
    
    //returns the location of a FilmRequest
    public int findRequest(FilmRequest request) {
        return this.requests.indexOf(request);
    }

    //Adds a new request to the list
    public void addRequest(FilmRequest request) {

        this.requests.add(request);

    }
    
    //removes a quest
    public void removeRequest(FilmRequest request) {
        this.requests.remove(request);
    }
    
    //This function locates a request in the FilmRequests and, if it's found,
    //accepts it
    public boolean acceptRequest(FilmRequest request) {
        boolean accepted = false;

        int location = this.findRequest(request);
        if (location != -1) {
            Film temp = this.requests.get(location).getFilm();
            location = this.findFilm(temp);
            if(location != -1){
                accepted = true;
                System.out.println("accepted");
                this.requests.remove(request);
                request.accept();
                this.availableFilms.get(location).addAcceptedRequest(request);
            }
        }

        return accepted;
    }
    
    //returns the players IP address (unused)
    public String getIp() {
        return ip;
    }
    
    //Calls the update method for each of its cached films
    public void update() {

        for (int i = 0; i < this.availableFilms.size(); i++) {
            this.availableFilms.get(i).update();
        }
    }

}
