package net.jamicah.arduinocraft.arduino;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.Chat;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;

import java.io.IOException;
import java.io.InputStream;

public class SerialCom {
    public SerialPort comPort;
    public InputStream comPortIn;
    public static Boolean isOpened = false;
    public static Boolean isReceivingInput = false;
    public static int analogSignal = 0;
    public static Boolean hasSentArduinoMessage = false;
    public static StringBuilder messageBuffer = new StringBuilder();

    // set up the serial port
    public SerialCom(String port, int baudrate) {

        this.comPort = SerialPort.getCommPort(port);
        comPort.setComPortParameters(baudrate, 8, 1, 0);
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


    // reads the serial port (digital)
    public static Boolean digitalRead(InputStream in) {
        int read;

        try {
            if (in.available() > 0) {
                read = in.read();
                if (read == 51) {
                    Arduinocraft.LOGGER.info("receiving signal HIGH");
                    return true;
                } else if (read == 52) {
                    Arduinocraft.LOGGER.info("receiving signal LOW");
                    return false;
                }


            }
        } catch (IOException e) {
            Chat.sendMessage("§cAn error has occurred while trying to read Arduino's signal");
        }
        return null;
    }

    // reads the serial port (analog)
    public static void analogRead() {
        while (Arduino_Block.isAnalog && isOpened) {
            InputStream in = Arduinocraft.comPort.comPortIn;
            int read;
            char readChar;
            String message;
            try {
                if (in.available() > 0) {
                    read = in.read();
                    readChar = (char) read;
                    messageBuffer.append(readChar);

                    if (readChar == '\n') {
                        // Process the complete message (excluding the delimiter)
                        message = messageBuffer.toString().trim();

                        // Reset the buffer for the next message
                        messageBuffer.setLength(0);

                        // subtracting 3 because the arduino sends the value + 3
                        SerialCom.analogSignal = Integer.parseInt(message) - 3;
                    }
                }
            } catch (SerialPortIOException ignored) {

            } catch (IOException e) {
                Chat.sendMessage("§cAn error has occurred while trying to read Arduino's signal");
                Chat.sendMessage("§cDebug info: " + e);
            }
        }

    }

    // close the serial port
    public static void closePort(SerialPort comPort) {
        if (comPort.closePort()) {
            Arduinocraft.LOGGER.info("Successfully closed Serial Communication");
            isOpened = false;
        } else {
            Arduinocraft.LOGGER.info("An error has occurred while trying to close the Serial Communication");
        }
    }
}
