package net.jamicah.arduinocraft.event;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;

public class SerialRead implements ClientTickEvents.EndWorldTick, ServerLifecycleEvents.ServerStopped {


    @Override
    public void onEndTick(ClientWorld world) {
        /* old code
        if (SerialCom.isOpened && !Arduino_Block.isInput) {
            SerialCom.isReceivingInput = SerialCom.digitalRead(Arduinocraft.comPort.comPortIn);

            if (SerialCom.isReceivingInput) {
                Arduinocraft.LOGGER.info("Pressed down!!");
            }
         */
        if (SerialCom.isOpened && !Arduino_Block.isInput) {
            Boolean isReceiving = SerialCom.digitalRead(Arduinocraft.comPort.comPortIn);
            if (isReceiving != null) {
                SerialCom.isReceivingInput = isReceiving;
            }
        }

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
