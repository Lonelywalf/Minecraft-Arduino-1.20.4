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
    public static volatile Boolean isReceivingInput = false;
    public static volatile int analogSignal = 0;
    public static Boolean hasSentArduinoMessage = false;
    public static StringBuilder messageBuffer = new StringBuilder();

    // instance-level reader thread
    private Thread readerThread;

    // set up the serial port
    public SerialCom(String port, int baudrate) {

        this.comPort = SerialPort.getCommPort(port);
        comPort.setComPortParameters(baudrate, 8, 1, 0);
        // Use non-blocking read mode so available()/read() won't block
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (comPort.openPort()) {
            try {
                comPortIn = comPort.getInputStream();
            } catch (Exception e) {
                Chat.sendMessage("§cFailed to get input stream: " + e.getMessage());
                isOpened = false;
                return;
            }
            Chat.sendMessage("§aCommunication successfully started");
            isOpened = true;

            // start instance reader thread to process incoming serial data
            readerThread = new Thread(this::readerLoop, "Arduino-Serial-Reader");
            readerThread.setDaemon(true);
            readerThread.start();
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

    // continuous reader loop that handles both digital (3/4) and analog (line-terminated) messages
    private void readerLoop() {
        Arduinocraft.LOGGER.info("SerialCom.readerLoop started");
        try {
            while (isOpened && comPortIn != null) {
                try {
                    // read all available bytes
                    while (comPortIn.available() > 0) {
                        int b = comPortIn.read();
                        if (b == -1) break;
                        Arduinocraft.LOGGER.debug("SerialCom.readerLoop raw byte: " + b);

                        // handle newline-terminated analog messages
                        if (b == '\n') {
                            String msg;
                            synchronized (messageBuffer) {
                                msg = messageBuffer.toString().trim();
                                messageBuffer.setLength(0);
                            }
                            if (!msg.isEmpty()) {
                                try {
                                    int parsed = Integer.parseInt(msg);
                                    // analog payload is value + 3 (per Arduino sketch)
                                    int computed = parsed - 3;
                                    // clamp to redstone range 0..15
                                    int clamped = Math.max(0, Math.min(computed, 15));
                                    analogSignal = clamped;
                                    Arduinocraft.LOGGER.info("SerialCom.readerLoop parsed analog value: " + computed + " -> clamped: " + analogSignal);
                                } catch (NumberFormatException nfe) {
                                    Arduinocraft.LOGGER.warn("SerialCom.readerLoop failed to parse analog message: '" + msg + "'");
                                }
                            }
                        } else if (b == 3 || b == 4 || b == 51 || b == 52) {
                            // digital raw 3/4 or ascii '3'/'4'
                            if (Boolean.TRUE.equals(Arduino_Block.isDigital) && !Boolean.TRUE.equals(Arduino_Block.isInput)) {
                                if (b == 3 || b == 51) {
                                    isReceivingInput = true;
                                    Arduinocraft.LOGGER.info("SerialCom.readerLoop detected DIGITAL HIGH");
                                } else if (b == 4 || b == 52) {
                                    isReceivingInput = false;
                                    Arduinocraft.LOGGER.info("SerialCom.readerLoop detected DIGITAL LOW");
                                }
                            } else {
                                // If not digital mode, nevertheless append to buffer for analog parsing if numeric
                                synchronized (messageBuffer) {
                                    messageBuffer.append((char) b);
                                }
                            }
                        } else {
                            // append any other bytes to buffer for potential analog numeric messages
                            synchronized (messageBuffer) {
                                messageBuffer.append((char) b);
                            }
                        }
                    }

                    Thread.sleep(2);
                } catch (IOException e) {
                    Arduinocraft.LOGGER.error("Error reading serial input", e);
                    break;
                } catch (InterruptedException ie) {
                    break;
                }
            }
        } finally {
            Arduinocraft.LOGGER.info("SerialCom.readerLoop stopped");
        }
    }

    // legacy digitalRead kept for compatibility (not used by readerThread)
    public static Boolean digitalRead(InputStream in) {
        if (in == null) {
            Arduinocraft.LOGGER.warn("SerialCom.digitalRead called with null InputStream");
            return null;
        }

        try {
            if (in.available() > 0) {
                int read = in.read();
                if (read == -1) {
                    // end of stream
                    return null;
                }
                Arduinocraft.LOGGER.info("SerialCom.digitalRead raw byte: " + read);
                if (read == 3 || read == 51) {
                    return true;
                } else if (read == 4 || read == 52) {
                    return false;
                }
            }
        } catch (IOException e) {
            Chat.sendMessage("§cAn error has occurred while trying to read Arduino's signal");
            Arduinocraft.LOGGER.error("Error reading from serial input stream", e);
        }
        return null;
    }

    // reads the serial port (analog)
    @Deprecated
    public static void analogRead() {
        Arduinocraft.LOGGER.info("SerialCom.analogRead() (deprecated) called — use readerThread instead");
        // Keep backward compatibility: start a simple loop that waits for readerThread to do the work
        while (Arduino_Block.isAnalog && isOpened) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }

    // close the serial port
    public static void closePort(SerialPort comPort) {
        // stop reader thread if running
        try {
            if (Arduinocraft.comPort != null && Arduinocraft.comPort.readerThread != null) {
                Arduinocraft.comPort.readerThread.interrupt();
                Arduinocraft.comPort.readerThread = null;
            }
        } catch (Exception ignored) {
        }

        if (comPort.closePort()) {
            Arduinocraft.LOGGER.info("Successfully closed Serial Communication");
            isOpened = false;
        } else {
            Arduinocraft.LOGGER.info("An error has occurred while trying to close the Serial Communication");
        }
    }
}
