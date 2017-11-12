/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rock;

/**
 *
 * @author Alex
 */
public interface Updater {
	//void received(Net net, String msg);
        //void update(int id);
        void connected(int players, int id);
        //void disconnected(int players);
        void newRound();
}
