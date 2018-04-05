/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

/**
 *
 * @author Dakota
 */
public class FilmRequest {

    private Film film;
    private String name;
    private int timeRemaining;
    private boolean accepted;
    private boolean filmComplete;
    
    FilmRequest(Film film, String name){
        this.film = film;
        this.name = name;
        this.timeRemaining = this.film.getDuration();
        this.accepted = false;
        this.filmComplete = false;
    }

    public String getName() {
        return this.name;
    }

    public Film getFilm() {
        return this.film;
    }
    
    public int getTime(){
        return this.timeRemaining;
    }
    
    public boolean isAccepted(){
        return this.accepted;
    }
    
    public boolean isComplete(){
        return this.filmComplete;
    }
    
    public boolean accept(){
        boolean accept = false;
        try{
            this.accepted = true;
            accept = true;
        }catch(Exception e){
            System.out.println("couldn't accept film");
        }
        return accept;
    }
    
    private void decTime(){
        this.timeRemaining--;
    }
    
    public void output(){
        System.out.println("Film: " + this.film.getTitle());
        System.out.println("Requested by: " + this.getName());
        System.out.println("Bandwidth Consumption: " + this.film.getBandConsum());
        if(this.isAccepted()){
            System.out.println("Time remaining: " + this.timeRemaining);
        }
        else{
            System.out.println("Duration: " + this.film.getDuration());
        }
    }
    
    public void update(){
        if(this.accepted && this.timeRemaining!=0){
            this.decTime();
        }
    }
}
