/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

/**
 *
 * @author Dakota
 */
public class Main {
//    @Override
//    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
//        
//        Scene scene = new Scene(root);
//        
//        stage.setScene(scene);
//        stage.show();
//    }

    
    
    public static void main(String[] args) {
//        launch (args);

        Scanner input = new Scanner(System.in);

        ObservableList<Player> players = FXCollections.observableArrayList();
        Server game = new Server(players);
        game.update();//delete later
        
        players = game.getPlayers();
        Player main = players.get(0);
        
        ObservableList <FilmRequest> requests = main.getRequests();
        
        System.out.print("Welcome! Please enter your name: ");
        main.setName(input.next());
        
        System.out.println("Welcome " + main.getName() + "!");
        
        Thread t = new Thread(game);
        t.start();
        
        while(game.totalRequests()!=0){
            System.out.println("Please choose a request to fulfill or type \"close\" to close the game");
            if(requests.size()>0){    
                for (int i = 0; i < requests.size(); i++) {
                    System.out.println("" + i + ":");
                    requests.get(i).output();
                }
            }
            
            String in = input.next();
            if(in.compareToIgnoreCase("close") == 0){
                System.exit(0);
            }
            else{
                int choice = Integer.parseInt(in);
                
                if(choice<main.numRequests()){
                    main.acceptRequest(requests.get(choice));
                }
            }
        }
        System.out.println("You Win!!");
        System.exit(0);
    }
}
