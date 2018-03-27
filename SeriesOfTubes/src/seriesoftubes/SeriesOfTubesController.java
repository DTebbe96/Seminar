/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seriesoftubes;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author Dakota
 */
public class SeriesOfTubesController implements Initializable {

    private Connection conn;
    private String ip;
    private Thread thread;

    @FXML
    private Label userIp;

    @FXML
    private CheckBox host;

    @FXML
    private TextArea connectionOutput;

    @FXML
    private TextField ipAddressField;

    @FXML
    private void handleStart(ActionEvent Event) {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception exception) {
        }

        if (host.isSelected()) {
            this.conn = new Connection();
            this.thread = new Thread(this.conn);

            this.thread.start();
            conn.setOutput("host test");
            this.conn.output();
        } else {
            this.conn = new Connection(this.ipAddressField.getText());
            this.thread = new Thread(this.conn);

            this.thread.start();
            String output = null;
            while(output==null){
                this.conn.input();
                output = conn.getInput();
            }
            this.connectionOutput.setText(conn.getInput());
        }

        this.userIp.setText(ip);
    }
    
    public void start(){
        
    }
    
    @FXML
    public void handleSend(){
        
    }
    
    @FXML
    public void handleExit(ActionEvent e) {
        System.exit(0);
    }

    @FXML
    public void update(ActionEvent Event) {
        this.connectionOutput.setText(conn.getInput());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
            //Thread.sleep(1000);
            this.userIp.setText(this.ip);
            this.ipAddressField.setText(this.ip);

        } catch (Exception exception) {
        }
    }

}
