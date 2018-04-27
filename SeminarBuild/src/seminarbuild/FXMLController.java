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
    ListView<FilmRequest> filmRequests = new ListView();
    @FXML
    ListView<Film> films = new ListView();
    @FXML
    ListView<Player> npcs = new ListView();

    ObservableList<Player> players = FXCollections.observableArrayList();
    ObservableList<FilmRequest> requests;

    @FXML
    TextField name = new TextField();
    @FXML
    Label totalRequests = new Label();
    @FXML
    Label uptime = new Label();

    @FXML
    Button start = new Button();
    @FXML
    Button accept = new Button();
    @FXML
    Button transfer = new Button();

    ObservableList<Film> mainFilms;

    FilmRequest tempRequest;

    Boolean canTransfer = false;

    NumberAxis x = new NumberAxis(0, 9, 1);

    NumberAxis y = new NumberAxis(0, 9, 1);

    int test = 0;

    @FXML
    LineChart<Integer, Integer> bandAvail = new LineChart(x, y);

    Player main;
    Server game;

    //This function is for handling when a request is clicked on, storing the 
    //selected request and making the accept and transfer buttons clickable
    @FXML
    public void onRequestClick(Event e) {
        this.accept.setDisable(false);
        this.transfer.setDisable(false);
        this.tempRequest = this.filmRequests.getSelectionModel().getSelectedItems().get(0);
    }

    //This function handles when the accept button has been clicked. It will
    //search the available films for the one associated to it and accept the
    //request if it is found. Otherwise it disables the accept and transfer
    //buttons and sets tempRequest to null for the next selection
    @FXML
    public void accept(Event e) {
        if (this.tempRequest != null) {
            if (this.mainFilms.indexOf(this.tempRequest.getFilm()) != -1) {
                this.tempRequest.accept();
                int location = this.mainFilms.indexOf(this.tempRequest.getFilm());
                this.mainFilms.get(location).addAcceptedRequest(this.tempRequest);
                this.requests.remove(this.tempRequest);
            }

        }
        this.tempRequest = null;
        this.canTransfer = false;

        this.accept.setDisable(true);
        this.transfer.setDisable(true);
    }

    //This function handles the start button, disabling it from being pressed
    //and initializing all the appropriate attributes for the game to begin
    @FXML
    public void handleStart(ActionEvent e) {
        this.start.setDisable(true);

        this.filmRequests.setItems(this.requests);

        if (this.name.getText().isEmpty()) {
            this.name.setText("UnnamedPlayer");
        }
        this.game = new Server(players, this);
        this.game.update();

        this.main = this.players.get(0);

        this.requests = this.main.getRequests();

        this.mainFilms = this.players.get(0).getFilmCache();

        this.films.setItems(this.mainFilms);
        ObservableList<Player> tempPlayers = FXCollections.observableList(this.players.subList(1, this.players.size() - 1));

        this.npcs.setItems(tempPlayers);

        this.setRequests();

        this.filmRequests.setItems(this.requests);

        this.name.setDisable(true);

        Thread t = new Thread(game);
        t.start();
    }

    //This handles the transfer button and enable the player to now transfer a
    //request to another player
    @FXML
    public void transfer(Event e) {
        this.canTransfer = true;
        this.accept.setDisable(true);
    }

    //Once transfer has pressed this function now handles another player being
    //selected and transfers the request from one player to the other
    @FXML
    public void receiveTransfer(Event e) {
        if ((this.npcs.getSelectionModel() != null) && (this.tempRequest != null) && this.canTransfer) {
            this.main.removeRequest(this.tempRequest);
            this.npcs.getSelectionModel().getSelectedItems().get(0).addRequest(this.tempRequest);
        }
        this.tempRequest = null;
        this.canTransfer = false;

        this.accept.setDisable(true);
        this.transfer.setDisable(true);
    }

    //The initialize function is called at the start of the program and starts
    //the preparations for the game to begin
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        XYChart.Series<Integer, Integer> data = new XYChart.Series();

        data.setName("Total Bandwidth");
        for (this.test = 0; this.test < 10; test++) {
            data.getData().add(new XYChart.Data<Integer, Integer>(this.test, 5));
        }
        this.bandAvail.getData().add(data);

        this.accept.setDisable(true);
        this.transfer.setDisable(true);
    }

    //this function is for setting the requests for the UI to see
    public void setRequests() {
        ObservableList<FilmRequest> films = this.main.getRequests();
        this.filmRequests.setItems(films);
    }

    //This function is for updating the Available Bandwidth chart with the most
    //recent data
    public void updateChart() {
        XYChart.Series<Integer, Integer> temp = this.bandAvail.getData().get(0);
        temp.getData().add(new LineChart.Data<>(this.test, this.main.bandAvail()));
        this.test++;
    }

    //The update method, like other update methods in this program, is used by
    //the server to update the game every tick, in this case updating the
    //available bandwidth chart, the total requests, the uptime, and the films
    public void update() {
        //https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateChart();
                totalRequests.setText("Total Requests: " + game.totalRequests());
                uptime.setText("Uptime: " + game.getUptime() + "%");
                main.update();
                films.refresh();
            }
        });
    }

}
