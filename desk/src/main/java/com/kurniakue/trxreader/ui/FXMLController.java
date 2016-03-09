/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author harun1
 */
public class FXMLController extends Application implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private Stage theStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXML.fxml"));

            Scene scene = new Scene(root, 300, 275);

            stage.setTitle("FXML Welcome");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
            
            theStage = stage;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @FXML private void saveCustomer(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
