/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import javafx.collections.*;

/**
 *
 * @author Dakota
 */
public class Film {
    private String title;
    private int bandConsum;
    private int duration;
    private ObservableList <FilmRequest> acceptedFilms;
    
    Film (String title, int bandConsum, int duration){
        this.title = title;
        this.bandConsum = bandConsum;
        this.duration = duration;
        
        this.acceptedFilms = FXCollections.observableArrayList();
    }
    
    //returns the film's title
    public String getTitle(){
        return this.title;
    }
    
    //returns the film's bandwidth consumption
    public int getBandConsum(){
        return this.bandConsum * this.acceptedFilms.size();
    }
    
    //returns the duration of the film
    public int getDuration(){
        return this.duration;
    }
    
    //adds an accepted request to its list
    public void addAcceptedRequest(FilmRequest accept){
        this.acceptedFilms.add(accept);
    }
    
    //returns a string of the film's title and, if it's been accepted, the
    //The film's bandwidth consumption, accepted clients and time remaing
    //Otherwise it add the film's base bandwitdth consumption and duration
    @Override 
    public String toString(){
        String output = "";
        
        output += ("Title: " + this.title + "\n");
        
        int size = this.acceptedFilms.size();
        if(this.acceptedFilms.size()>0){
            output += ("Current bandwidth consumption: " + (this.bandConsum*size) + "\n");
            output += ("Total accepted clients: " + this.acceptedFilms.size() + "\n");
            output += ("Time remaining: " + this.acceptedFilms.get(size - 1).timeRemaining);
        }
        else{
            output += ("Base bandwidth consumption: " + this.bandConsum + "\n");
            output += ("Duration: " + this.duration);
        }
        
        return output;
    }
    
    //updates the film, and, if it's request has been accepted and has no time renamifk
    public void update(){
        for (int i = 0; i < this.acceptedFilms.size(); i++) {
            FilmRequest f = this.acceptedFilms.get(i);
            if (f.isAccepted() && (f.getTime() <= 0)) {
                this.removeRequest(f);
            }
            f.update();
        }
    }
    
    //removes a request from it's list
    private boolean removeRequest(FilmRequest request){
        return this.acceptedFilms.remove(request);
    }
    
}
