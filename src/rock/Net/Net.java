/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rock.Net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import rock.controller.Controller;

public class Net implements Runnable
{
	private DatagramSocket socket;
	private boolean running;
	Controller myController;
        public String myIp = "192.168.1.75"; //use localhost?
        //public InetSocketAddress address = new InetSocketAddress("192.168.1.75", 1001);
        public InetSocketAddress server = new InetSocketAddress("192.168.1.75", 999);
        public int userPort = 1000;
        public void setMyController(Controller c){
            this.myController = c;
        }
        
        public DatagramSocket getSocket(){
        return socket;
        }
        
	public int bind(int port){
            int finalPort;
            System.out.println("socket " +port);
            try{
		this.socket = new DatagramSocket(port);
                finalPort=port;
                }
            catch (SocketException e){
                            finalPort=port;
                            finalPort++;
                            try{
				this.socket = new DatagramSocket(finalPort);
                            }catch (SocketException ee){
                            System.out.println("socket failed to set again " +finalPort);
                            }
                                System.out.println("socket set at " +finalPort);
		}
                System.out.println("socket binded");
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
                                System.out.println(msg);
				myController.interpretMessage(msg);
			} 
			catch (IOException e){
				break;
			}
		}
	}

        public void send(InetSocketAddress address, String message) throws IOException{
            
		byte[] buffer = message.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setSocketAddress(address);
		getSocket().send(packet);
                System.out.println("input was sent");
                //recordInput(0, choice);
	}
}