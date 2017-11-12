
package rock.server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Matchmaker implements Runnable{

    Queue<String> queuedPlayers;
    private DatagramSocket socket;
    private boolean running;
    int port = 999;
    int playersPerGame = 3;
    
    public Matchmaker(){
    queuedPlayers = new LinkedList<>();
    
    }
    
    public int bind(int port){
        try{
            this.socket = new DatagramSocket(port);
            
        }catch (SocketException e){
            System.out.println("failed to set socket to port 999");
        }
                return port;
    }
    
    @Override
    public void run(){
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	running = true;
	while(running){
            try{
		socket.receive(packet);
		String message = new String(buffer, 0, packet.getLength());
		interpretMessage(message);
                    if(enoughPlayers()){
                        setupGame();
                        
                    }
				
		} 
            catch (IOException e){
				break;
		}
		
        }
    }
          
    public void start(){
        this.bind(port);
	Thread thread = new Thread(this);
	thread.start();
                
    }
	
    public void stop(){
	running = false;
	socket.close();
    }
        
    private boolean enoughPlayers(){
            return (checkQueueCount() >= playersPerGame);
    }
        
    private void setupGame(){
        int count = queuedPlayers.size();
        
        //if there are more players in queue than allowed in one game
        if(count>playersPerGame){
            count=playersPerGame;
        }
            
        InetSocketAddress[] address = new InetSocketAddress[count];
        
        //will be a string representation of the collective connection info for all players
        //the players needs this info to contact eachother.
        String playersConnection = "";
            
            //players are removed from queue while setting up a game for them.
            //if they leave a game and want to play again, they have to connect again.
            for(int i = 0; i<count; i++){
                String playerConnection = queuedPlayers.remove();
                playersConnection +=" ";
                System.out.println(playersConnection);
                playersConnection +=playerConnection;
                
                String[] arr = playerConnection.split(" ");
                address[i] = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
                
            }
            
            String message = "setup " +count +" ";
            
            for(int i = 0; i<count; i++){
                try{
            
                send(address[i], message +i +playersConnection);
                }catch(IOException e){
                System.out.println("failed to send");
                }
            
            }
            
    }
        
        public void send(InetSocketAddress address, String message) throws IOException{
		byte[] buffer = message.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		this.socket.send(packet);
                System.out.println("game was setup");
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
        //String command = arr[0];
        
        //when a connect message is received, the player is put in a queue for matchmaking.
        if(arr[0].equals("connect")){
            
            String connectionInfo = arr[1] +" " + arr[2];   //info - ip port
            System.out.println("playerinfo received " +connectionInfo);
            addToQueue(connectionInfo);
            }
        
        }
        
        private void addToQueue(String connectionInfo){
            if(this.queuedPlayers.add(connectionInfo)){
                System.out.println("added to queue");
            }else{
                System.out.println("failed to add to queue");
            }
                
        }
        
        private int checkQueueCount(){
            return queuedPlayers.size();
        }
    
        public static void main(String[] args){
        Matchmaker m = new Matchmaker();
        m.start();
        }
    
    
}
