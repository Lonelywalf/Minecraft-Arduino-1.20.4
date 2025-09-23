package net.jamicah.arduinocraft.arduino;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;

public class SerialComEvents implements ClientTickEvents.EndWorldTick, ServerLifecycleEvents.ServerStopped {

    // Read the input from the arduino every tick and changes the redstone signal accordingly
    @Override
    public void onEndTick(ClientWorld world) {
        if (SerialCom.isOpened && !Arduino_Block.isInput && Arduino_Block.isDigital) {
            Boolean isReceiving = SerialCom.digitalRead(Arduinocraft.comPort.comPortIn);
            if (isReceiving != null) {
                SerialCom.isReceivingInput = isReceiving;
            }
        }
//        else if (SerialCom.isOpened && !Arduino_Block.isInput && Arduino_Block.isAnalog) {
//            SerialCom.analogRead();
//        }

    }

    // Closing the port when the player quits
    @Override
    public void onServerStopped(MinecraftServer server) {
        if (SerialCom.isOpened) {
            try {
                Arduinocraft.comPort.comPort.closePort();
                Arduinocraft.LOGGER.info("Port successfully closed.");
            } catch (Exception e) {
                Arduinocraft.LOGGER.error("An error has occurred while trying to close the port. Debug info: " + e);
            }
            SerialCom.isOpened = false;
        }

    }


}
