/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rock.Net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import rock.controller.Controller;

public class Net implements Runnable
{
	private DatagramSocket socket;
	private boolean running;
	Controller myController;
        public static int serverPort = 999;
        public static String serverIp = "localhost";
        
        private String myIp = "localhost";
        private InetAddress inet;
        public InetSocketAddress server;
        private int userPort = 1000;
        
        public void setMyController(Controller c){
            this.myController = c;
        }
        
        public int getMyPort(){
            return userPort;
        }
        
        public String getMyIp(){
            return myIp;
        }
        
        public DatagramSocket getSocket(){
        return socket;
        }
        
	public int bind(){
            
            server = new InetSocketAddress(serverIp, serverPort);
            System.out.println("server " +serverIp +" " +serverPort);
            
            try{
            inet = InetAddress.getLocalHost();
            myIp = inet.getHostAddress();
            }catch(UnknownHostException e){
            }
            
            int finalPort = userPort;
            
            boolean foundPort = false;
            int counter = 0;
            while(!foundPort && counter<20){//counter that tests 20 ports, to prevent infinite loop.
                
                try{
		this.socket = new DatagramSocket(userPort);
                finalPort=userPort;
                foundPort=true;
                }            
                catch (SocketException e){
         
		}
                userPort++;
                counter++; 
                
            }
                userPort = finalPort;
                return finalPort;
	}
	
	public void start(){
		Thread thread = new Thread(this);
		thread.start();
                
	}
	
	public void stop(){
		running = false;
		socket.close();
	}

	@Override
	public void run(){
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
		running = true;
		while(running){
			try{
				socket.receive(packet);
				
				String msg = new String(buffer, 0, packet.getLength());
                                
				myController.interpretMessage(msg);
			} 
			catch (IOException e){
				break;
			}
		}
	}

        public void send(InetSocketAddress address, String message) throws IOException{
                Thread send = new Thread(this);
                send.start();
		byte[] buffer = message.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		getSocket().send(packet);
                
	}
}