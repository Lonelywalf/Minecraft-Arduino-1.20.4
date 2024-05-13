package net.jamicah.arduinocraft.arduino;


import com.fazecast.jSerialComm.SerialPort;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.Chat;

import java.io.IOException;
import java.io.InputStream;

public class SerialCom {
    public SerialPort comPort;
    public InputStream comPortIn;
    public static Boolean isOpened = false;
    public static Boolean isReceivingInput = false;

    // set up the serial port
    public SerialCom(String port) {
        this.comPort = SerialPort.getCommPort(port);
        comPort.setComPortParameters(9600, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        comPortIn = comPort.getInputStream();

        if (comPort.openPort()) {
            Chat.sendMessage("§aCommunication successfully started");
            isOpened = true;
        } else {
            Chat.sendMessage("§cSomething went wrong, the inputted port might be wrong");
            isOpened = false;
        }
    }

    // write to the serial port
    public static void digitalWrite(SerialPort com, Integer signal) {
        try {
            com.getOutputStream().write(signal.byteValue());
        } catch (IOException e) {
            Chat.sendMessage("An error has occurred. Debug info: " + e);
        }
        try {
            com.getOutputStream().flush();
        } catch (IOException e) {
            Chat.sendMessage("An error has occurred. Debug info: " + e);
        }
    }



    public static Boolean digitalRead(InputStream in) {
        int read;
        try {
            if (in.available() > 0) {
                read = in.read();
                Arduinocraft.LOGGER.info(String.valueOf(read));
                return true;
                // FIXME: in.read() receives every single button press. maybe fix it by making the button only send a single serial print when pressed down, instead of every single time
                // FIXME: also, find a way to optimize the reading
            }
        } catch (IOException e) {
            Chat.sendMessage("§cAn error has occurred while trying to read Arduino's signal");
        }
        return false;
    }

    public static void closePort(SerialPort comPort) {
        if (comPort.closePort()) {
            Arduinocraft.LOGGER.info("Successfully closed Serial Communication");
            isOpened = false;
        } else {
            Arduinocraft.LOGGER.info("An error has occurred while trying to close the Serial Communication");
        }
    }
}
