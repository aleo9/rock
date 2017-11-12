/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rock.controller;

import rock.Net.Net;
import java.io.IOException;
import java.net.InetSocketAddress;
import rock.model.Model;
import rock.Updater;


/**
 *
 * @author Alex
 */

public class Controller{
    Model model;
    Net net;
    Updater updater;
    
    public Controller(){
       
       model = new Model(); 
       System.out.println("model set");
       net = new Net();
       giveNetAccess();
       net.userPort = net.bind(net.userPort);
       if(net.userPort != 1000){
            //net.address = new InetSocketAddress("192.168.1.75", 1000);
             //System.out.println("new userport " +net.userPort + " address port set to " +1000);
                    
        }
                
	net.start(); // Start Receive
        System.out.println("net Started.");
       
    }
    
    private void giveNetAccess(){
        net.setMyController(this);
    }
    
    //Interface to tell FXMLController when to update scores etc.
    public void setUpdater(Updater c){
        this.updater =c;
    }
    
    public void interpretMessage(String message){
        
        //format of messages
        //
        //user messages
        //ask server to connect to players - connect myip myport 
        //send your choice to players - pick playerid choice 
        //
        //server/matchmaker messages
        //server setup a game between 2-3 players - setup playercount yourid player1ip player1port player2ip player2port (player3ip player3port)
        //id is given to player, assigned by matchmaker
        
        String[] arr = message.split(" ");    
        //int messageLength = arr.length;
        
        //means a already connected player sends a pick message
        if(arr[0].equals("pick")){
            int id = Integer.parseInt(arr[1]);
            String choice = arr[2];
            recordInput(id, choice);
        
        //means server setup message
        }else if(arr[0].equals("setup")){
            
            int playerCount = Integer.parseInt(arr[1]);
            //model.setPlayerCount(players);
            //model.setMyId(Integer.parseInt(arr[2]));
            
            InetSocketAddress[] connections = new InetSocketAddress[playerCount];
            
            connections[0] = new InetSocketAddress(arr[3], Integer.parseInt(arr[4]));
            connections[1] = new InetSocketAddress(arr[5], Integer.parseInt(arr[6]));
            if(playerCount==3){
                connections[2] = new InetSocketAddress(arr[7], Integer.parseInt(arr[8]));
            }
                //sends array with addresses to players, including your own data, and your id, which tells which index is you.
                model.createPlayers(connections, Integer.parseInt(arr[2]));
                
                //tells FXMLController how many players there are and your id.
                updater.connected(playerCount, model.getMyId());
            }
        
    }
    
    //connect to server who then connects you to queued up players.
    public void connectToPlayers() throws IOException{
        String message = "connect " +net.myIp +" " +net.userPort;
        
        net.send(net.server, message);
        System.out.println("sent request");
        
    }
    
    public int getPlayerCount(){
        return model.getPlayerCount();
    }
    
    public void close(){
        
        net.stop();
    }
    
    public int getScore(int id){
        
    return model.getPlayerScore(id);
    }
    
    public int getLastScore(int id){
        
    return model.getLastPlayerScore(id);
    }
    
    
    //remove
    public void addScore(int id, int value){
        model.addScore(id, value);
    }
    
    public void sendInput(String choice) throws IOException{
        System.out.println("attempts sending input");
        
        //message format - pick playerid choice 
        String message = "pick " +model.getMyId() +" " +choice;
        for(int i = 0; i<model.getPlayerCount(); i++){
            if(i!=model.getMyId()){
                net.send(model.getSocketAddress(i), message);
            }
        }
        recordInput(model.getMyId(), choice);
        
    }
    
    //stores what you or another player have inputted.
    private void recordInput(int id, String choice){
        model.registerChoice(id, choice);

        if(haveAllInputs()){
            countScore();
        }
    }
    
    //count and apply scores of a round, then call to reset and call FXMLController to update.
    public void countScore(){
        
        model.addScore(0, 2);
        model.addScore(1, 1);
        /*
        int players = model.getPlayerCount();
        String[] choice = new String[players];
        int[] score = new int[players];
        for (int i = 0; i<players; i++){
            choice[i]=model.getPlayerChoice(i);
        }
        
        if(choice[0].equals(choice[1])){
            score[0]++;
            score[1]++;
        }else if(choice[0].equals("rock") &&  choice[1]){
            score[0]
        }
*/      
        reset();
        updater.newRound();
    }
    
    private void reset(){
        model.resetPlayers();
    }
    
    public boolean haveAllInputs(){
        boolean all = true;
        for(int i = 0; i<model.getPlayerCount(); i++){
            
            if(!(model.hasPicked(i))){
                
                all = false;
                //break;
            }
        }
        
        return all;
    }
   

    
}
