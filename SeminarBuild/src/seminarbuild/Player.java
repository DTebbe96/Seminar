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
    private ArrayList<Film> availableFilms;
    private int maxCache;
    private int bandwidth;
    protected ObservableList<FilmRequest> requests;
    private String ip;

    Player(String ip) {
        this.ip = ip;
        this.username = "" + (int) (System.currentTimeMillis() / 10000);
        Random randnum = new Random();
        this.bandwidth = 5;
        this.maxCache = 4;
        this.availableFilms = new ArrayList<>();
        this.requests = FXCollections.observableArrayList();
    }
    
    public int numRequests(){
        int num = 0;
        
        if(this.requests.size()>0){
            num+= this.requests.size();
        }
        
        return num;
    }

    public String getName() {
        return this.username;
    }

    public Film[] getFilmCache() {
        Film[] tempFilms = (Film[]) this.availableFilms.toArray(new Film[this.availableFilms.size()]);
        return tempFilms;
    }

    public void setName(String name) {
        this.username = name;
    }

    public ObservableList<FilmRequest> getRequests() {
        return requests;
    }

    public void setRequests(ObservableList<FilmRequest> requests) {
        this.requests = requests;
    }

    public int cacheAvail() {
        return this.maxCache - this.availableFilms.size();
    }

    public int bandAvail() {
        int usedBand = 0;

        for (int i = 0; i < this.requests.size(); i++) {
            if (this.requests.get(i).isAccepted() && !this.requests.get(i).isComplete()) {
                Film tempFilm = this.requests.get(i).getFilm();
                usedBand += tempFilm.getBandConsum();
            }

        }
        return this.bandwidth - usedBand;
    }

    public boolean addMovie(Film film) {
        if (!this.availableFilms.contains(film)) {
            this.availableFilms.add(film);
            return true;
        } else {
            return false;
        }
    }

//    public boolean removeMovie(Film film){
//        if(this.availableFilms.containsValue(film)){
//            this.availableFilms.remove(film.getTitle(), film);
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
    private int findRequest(FilmRequest request) {
        return this.requests.indexOf(request);
    }

    public void addRequest(FilmRequest request) {

        this.requests.add(request);

    }

    public void removeRequest(FilmRequest request) {
        this.requests.remove(request);
    }

    public boolean acceptRequest(FilmRequest request) {
        boolean accepted = false;

        int location = this.findRequest(request);
        if (location != -1) {
            System.out.println("accepted");
            accepted = this.requests.get(location).accept();
        }

        return accepted;
    }

    public String getIp() {
        return ip;
    }

    public void update() {

        for (int i = 0; i < this.requests.size(); i++) {
            FilmRequest f = this.requests.get(i);
            if (f.isAccepted() && (f.getTime() <= 0)) {
                this.removeRequest(f);
            }
            f.update();
        }
    }

}
