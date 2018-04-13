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
public class Film {
    private String title;
    private int bandConsum;
    private int duration;
    
    Film (String title, int bandConsum, int duration){
        this.title = title;
        this.bandConsum = bandConsum;
        this.duration = duration;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public int getBandConsum(){
        return this.bandConsum;
    }
    
    public int getDuration(){
        return this.duration;
    }
    
    @Override 
    public String toString(){
        String output = "";
        
        output += ("Title: " + this.title + "\n");
        
        output += ("Bandwidth consumption: " + this.bandConsum + "\n");
        
        output += ("Duration: " + this.duration);
        
        return output;
    }
}
