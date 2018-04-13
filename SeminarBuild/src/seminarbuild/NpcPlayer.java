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
public class NpcPlayer extends Player {

    public NpcPlayer(String ip) {
        super(ip);
    }
    
    @Override
    public String toString(){
        String output = "";
        
        output += ("Name: " + this.getName() + "\n");
        output += ("Cache available: " + this.cacheAvail() + "\n");
        output += ("Bandwidth Available: " + this.bandAvail());
        
        return output;
    }

    @Override
    public void update() {
        Random rand = new Random();
        int choice = rand.nextInt(super.requests.size() + 1);

        if ((choice != super.requests.size()) && (super.bandAvail()==5)) {
            FilmRequest r = super.requests.get(choice);
            r.accept();
        }
        
        for (int i = 0; i < super.requests.size(); i++) {
            FilmRequest f = super.requests.get(i);
            if (f.isAccepted() && (f.getTime() == 0)) {
                super.removeRequest(f);
            }
            f.update();
        }
    }

}
