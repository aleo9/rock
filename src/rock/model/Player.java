package rock.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.InetSocketAddress;

/**
 *
 * @author Alex
 */
public class Player {
    
    private InetSocketAddress address;
    public int score = 0;
    private int lastScore = 0;
    private boolean hasPicked = false;
    private String choice;
    //private static final String ROCK = "rock";
    //private static final String PAPER = "paper";
    //private static final String SCISSOR = "scissor";
    
    
    public Player(){
    }
    
    public boolean getHasPicked(){
        return hasPicked;
    }
        public void setHasPicked(boolean val){
        hasPicked = val;
    }
    public String getChoice(){
        return this.choice;
    }
    
    public void registerChoice(String pick){
        this.hasPicked = true;
        this.choice = pick;
    }
    
    public void resetRound(){
        this.hasPicked = false;
        this.choice = null;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public void addScore(int added){
        this.lastScore = added;
        this.score = this.score+added;
    }
    
    public int getLastScore(){
        return this.lastScore;
    }
    
    public InetSocketAddress getSocketAddress(){
        return this.address;
    }
    
    public void setSocketAddress(InetSocketAddress address){
        this.address = address;
    }
    
}