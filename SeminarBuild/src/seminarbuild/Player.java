/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;
import java.util.*;
/**
 *
 * @author Dakota
 */
public class Player {
    private HashMap<String, Movie> availableMovies;
    private int maxCache;
    private int bandwidth;
    private Request[] requests;
    private Request[] currentAcceptedRequests;
    private String ip;
    
    Player(String ip){
        this.ip = ip;
        Random randnum = new Random();
        this.bandwidth = 5 + randnum.nextInt()%10;
        this.maxCache = 3 + randnum.nextInt()%4;
        this.availableMovies = new HashMap<>();
        this.requests = new Request[10];
        this.currentAcceptedRequests = new Request[10];
    }
    
    public int cacheAvail(){
        return this.maxCache - this.availableMovies.size();
    }
    
    public int bandAvail(){
        int usedBand = 0;
        for (int i = 0; i < this.currentAcceptedRequests.length; i++) {
            if (this.currentAcceptedRequests[i]!=null){
                usedBand += this.availableMovies.get(this.currentAcceptedRequests[i].getMovie()).getBandConsum();
            }
        }
        return this.bandwidth-usedBand;
    }
    
    public boolean addMovie(Movie movie){
        if(!this.availableMovies.containsValue(movie)){
            this.availableMovies.put(movie.getTitle(), movie);
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean removeMovie(Movie movie){
        if(this.availableMovies.containsValue(movie)){
            this.availableMovies.remove(movie.getTitle(), movie);
            return true;
        }
        else{
            return false;
        }
    }
    
    private int findRequest(Request request){
        int location = -1;
        
        for (int i = 0; i < this.requests.length; i++) {
            if(this.requests[i]==request){
                location = i;
                break;
            }
        }
        
        return location;
    }
    
    public void addRequest(Request request){
        int location = this.findRequest(null);
        if(location != -1){
            this.requests[location] = request;
        }
    }
    
    public void removeRequest(Request request){
        int location = this.findRequest(request);
        if(location != -1){
            this.requests[location] = null;
        }
    }
    
    public boolean acceptRequest(Request request){
        boolean accepted = false;
        
        int location = this.findRequest(request);
        
        if(location != -1){
            request.accept();
            this.removeRequest(request);
            
            for (int i = 0; i < this.currentAcceptedRequests.length; i++) {
                if(this.currentAcceptedRequests[i] == null){
                    this.currentAcceptedRequests[i] = request;
                }
            }
        }
        
        return accepted;
    }

    public String getIp() {
        return ip;
    }
}
