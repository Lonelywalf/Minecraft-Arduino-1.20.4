package net.jamicah.arduinocraft.arduino;


import com.fazecast.jSerialComm.SerialPort;
import net.jamicah.arduinocraft.Arduinocraft;
import java.io.IOException;
public class SerialCom {
    public SerialPort comPort;
    public SerialCom(String port) {
        this.comPort = SerialPort.getCommPort(port);
        comPort.setComPortParameters(9600, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (comPort.openPort()) {
            System.out.println("Port is opened");
        } else {
            System.out.println("Something went wrong");
            return;
        }
    }
    private static void digitalWrite(SerialPort com, Integer in) {
        try {
            com.getOutputStream().write(in.byteValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            com.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendToArduino(int signal, SerialPort comPort) {




        /*
        for (Integer i = 50; i < 53; i++) {
            comPort.getOutputStream().write(i.byteValue());
            comPort.getOutputStream().flush();
            System.out.println("Input: " + i);
            Thread.sleep(1000);
        }
         */

        digitalWrite(comPort, signal);






    }
    public static void closePort(SerialPort comPort) {
        if (comPort.closePort()) {
            System.out.println("Port closed.");
        } else {
            System.out.println("uhhhh idk closing didn't work");
        }
    }
}
