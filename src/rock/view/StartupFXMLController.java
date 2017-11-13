package rock.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import rock.Net.Net;
import rock.RPS;


public class StartupFXMLController implements Initializable {

    @FXML
    private Button playButton;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    
    private RPS parent;
    //private static Controller controller;
    ////@FXML
    ////private GridPane rootPane;
    
    @FXML
    private AnchorPane rootPane;
    
    public void setParent(RPS rps){
        this.parent = rps;
    }
    
    @FXML
    private void play(ActionEvent ae) throws IOException{
        //set ip for host
        Net n = new Net();
        if(!(ipField.getText().equals(""))){
            n.serverIp = ipField.getText();
        }
        if(!(portField.getText().equals(""))){
            n.serverPort = Integer.parseInt(portField.getText());
        }    
        
        
        
        //System.out.println("x");
    //connectButton.setText("waiting");
    ////////GridPane pane = FXMLLoader.load(getClass().getResource("/rock/view/RPSFXML.fxml"));
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/rock/view/RPSFXML.fxml"));
    //GridPane pane = FXMLLoader.load(getClass().getResource("/rock/view/RPSFXML.fxml"));
    rootPane.getChildren().setAll(pane);
    //parent.changeToGameView();
            
    
    }
    
    
    //public InetSocketAddress server = new InetSocketAddress("localhost", 999);
    @Override
    public void initialize(URL url, ResourceBundle rb) {


// TODO
    }    
    
}
