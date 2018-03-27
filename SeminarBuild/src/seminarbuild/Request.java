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
public class Request {
    private String movie;
    private long timeCreate;
    private boolean accepted;
    
    Request(String movie){
        this.movie = movie;
        this.timeCreate = System.currentTimeMillis();
        this.accepted = false;
    }
    
    public int getAge(){
        return (int)(System.currentTimeMillis()-this.timeCreate);
    }
    
    public String getMovie(){
        return this.movie;
    }
    
    public boolean accept(){
        if (!this.accepted){
            this.accepted = true;
            return true;
        }
        else{
            return false;
        }
    }
}
