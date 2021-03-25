package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Events.ErrorEvent;
import Utilities.Parser;
import Utilities.Settings;

/**
 * Class to consume error events from Scheduler
 * 
 * @author Diana Miraflor
 *
 */
public class ErrorEventConsumer implements Runnable {
    private ElevatorSubsystem parent;
    private DatagramSocket receiveSocket;
    
    public ErrorEventConsumer(ElevatorSubsystem elevatorSubsystem, int elevatorID) {
        parent = elevatorSubsystem;
        
        try {
            receiveSocket = new DatagramSocket(Settings.ELEVATOR_ERROR_ECP + elevatorID);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Method to consume the provided event.
     */
    public void consume(ErrorEvent event) {
        if (event.getErrorType() == true) {
            parent.shutDownElevator();
        } else {
            // handle error for elevator's door being closed
        }
    }
    
    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[8191], 8191);
        
        while (true) {
            try {
                receiveSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            consume((ErrorEvent)Parser.unpackDatagram(packet));
        }
    }
}
