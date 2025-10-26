package net.jamicah.arduinocraft.arduino;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class SerialComEvents implements ClientTickEvents.EndWorldTick, ServerTickEvents.EndWorldTick, ServerLifecycleEvents.ServerStopped {

    // Read the input from the arduino every tick and changes the redstone signal accordingly
    @Override
    public void onEndTick(ClientWorld world) {
        handleTick();
    }

    @Override
    public void onEndTick(ServerWorld world) {
        handleTick();
    }

    private void handleTick() {
        Arduinocraft.LOGGER.debug("SerialComEvents.handleTick: isOpened=" + SerialCom.isOpened + ", isDigital=" + Arduino_Block.isDigital + ", isInput=" + Arduino_Block.isInput);
        if (SerialCom.isOpened && !Arduino_Block.isInput && Arduino_Block.isDigital) {
            // the serial reader thread updates SerialCom.isReceivingInput; just log its current value
            Arduinocraft.LOGGER.debug("SerialComEvents.handleTick: current isReceivingInput=" + SerialCom.isReceivingInput);
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
