/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author Alex
 */
public class RPS extends Application {
 
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/RPSFXML.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
        
    }
    
    public void sendInput(){
        
    }

    
    @Override
    public void stop(){
        //net.stop();
    }
    public static void main(String[] args) throws IOException{

        launch(args);
        
        
    }
    
}
