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
//        Scanner in = new Scanner(System.in);
//        
//        FilmRequest reqTest0 = new FilmRequest(new Film("Lilt", 1, 20), "John");
//        FilmRequest reqTest1 = new FilmRequest(new Film("Trend", 1, 10), "Taylor");
//        FilmRequest reqTest2 = new FilmRequest(new Film("Corn", 1, 5), "Boris");
//        
//        ObservableList<FilmRequest> requests = FXCollections.observableArrayList();
//        requests.add(reqTest0);
//        requests.add(reqTest1);
//        requests.add(reqTest2);
//        
//        Server game = new Server();
//        game.assignFilms();
//        
//        System.out.println("Welcome to Series of Tubes!!");
//
//        while(true){
//            String lnBrk = "-----------------";
//            System.out.println(lnBrk + "\nPlease select a film or type ''close'' to close:");
//            for (int i = 0; i < requests.size(); i++) {
//                System.out.println("--" + i + " " + requests.get(i).getFilmName() +
//                        " Requested by " + requests.get(i).getName());
//            }
//            String input = in.nextLine();
//            if(input.compareToIgnoreCase("Close")==0){
//                System.exit(0);
//            }
//            int choice = Integer.parseInt(input);
//            System.out.println("Accepting " + requests.get(choice).getFilmName());
//            game.accept(requests.get(choice));
//        }
        ObservableList<Player> players = FXCollections.observableArrayList();
        Server game = new Server(players);
        game.update();
        players = game.getPlayers();
        Player temp = players.get(0);
        
        ObservableList <FilmRequest> requests = temp.getRequests();
        
        boolean acceptRequest = temp.acceptRequest(requests.get(0));
        
        while(temp.bandAvail()==4){
            game.update();
        }
    }
}
