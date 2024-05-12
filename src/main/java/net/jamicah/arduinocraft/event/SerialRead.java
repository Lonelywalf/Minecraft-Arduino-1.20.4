package net.jamicah.arduinocraft.event;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.minecraft.client.world.ClientWorld;

public class SerialRead implements ClientTickEvents.EndWorldTick {


    @Override
    public void onEndTick(ClientWorld world) {
        if (SerialCom.isOpened) {
            SerialCom.isReceivingInput = SerialCom.digitalRead(Arduinocraft.comPort.comPortIn);
        }
    }
}
