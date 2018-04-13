/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seminarbuild;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;

/**
 *
 * @author Dakota
 */
public class FXMLController implements Initializable {
    
    @FXML
    ListView <FilmRequest> filmRequests = new ListView();
    @FXML
    ListView <Film> films = new ListView();
    @FXML
    ListView <Player> npcs = new ListView();
    
    @FXML
    ObservableList<Player> players = FXCollections.observableArrayList();
    @FXML
    ObservableList<FilmRequest> requests;
    
    @FXML
    TextField name = new TextField();
    @FXML
    Label totalRequests = new Label();
    @FXML
    Label uptime = new Label();
    @FXML
    Button start = new Button();
    
    FilmRequest transfer;
    
    NumberAxis x = new NumberAxis(0,9,1);
        
    NumberAxis y = new NumberAxis(0,9,1);
    
    int test = 0;
    
    @FXML
    LineChart<Integer, Integer> bandAvail = new LineChart(x,y);
    
    Player main;
    Server game;
    
    @FXML
    public void handleRequests(Event e){
        if(this.filmRequests.getSelectionModel()!=null){
            FilmRequest temp = this.filmRequests.getSelectionModel().getSelectedItems().get(0);
            temp.accept();
        }
    }
    
    @FXML
    public void handleExit(ActionEvent e) {
        System.exit(0);
    }
    
    @FXML
    public void handleStart(ActionEvent e){
        this.start.setDisable(true);
        
        this.filmRequests.setItems(this.requests);
        
        if(this.name.getText().isEmpty()){
            this.name.setText("UnnamedPlayer");
        }
        this.game = new Server(players, this);
        this.game.update();
        
        this.main = this.players.get(0);

        this.requests = this.main.getRequests();
        
        Film[] temp = this.players.get(0).getFilmCache();
        
        this.films.setItems(FXCollections.observableList(Arrays.asList(temp)));
        ObservableList<Player> tempPlayers = FXCollections.observableList(this.players.subList(1, this.players.size()-1));
        
        this.npcs.setItems(tempPlayers);
        
        this.setRequests();
        
        this.filmRequests.setItems(this.requests);
        
        
        Thread t = new Thread(game);
        t.start();

        
    }
    
    @FXML
    public void drag(Event e){
        if(this.filmRequests.getSelectionModel()!=null){
            FilmRequest temp = this.filmRequests.getSelectionModel().getSelectedItems().get(0);
            this.transfer = temp;
        }
    }
    
    @FXML
    public void drop(Event e){
        if ((this.npcs.getSelectionModel()!=null)&&(this.transfer!=null)){
            this.main.removeRequest(this.transfer);
            this.npcs.getSelectionModel().getSelectedItems().get(0).addRequest(this.transfer);
        }
        this.transfer=null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        XYChart.Series<Integer, Integer> data = new XYChart.Series();
        
        data.setName("Total Bandwidth");
        for (this.test = 0; this.test < 10; test++) {
            data.getData().add(new XYChart.Data<Integer, Integer>(this.test, 5));
        }
        this.bandAvail.getData().add(data);
    }
    
    public void setRequests(){
        ObservableList<FilmRequest> films = this.main.getRequests();
        this.filmRequests.setItems(films);
    }
    
    public void updateChart(){
        XYChart.Series <Integer, Integer> temp = this.bandAvail.getData().get(0);
        temp.getData().add(new LineChart.Data<>(this.test, this.main.bandAvail()));
        this.test++;
    }
    
    public void update(){
        //https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                updateChart();
                totalRequests.setText("Total Requests: " + game.totalRequests());
                uptime.setText("Uptime: " + game.getUptime() + "%");
            }
        });
    }
    
    
 }

