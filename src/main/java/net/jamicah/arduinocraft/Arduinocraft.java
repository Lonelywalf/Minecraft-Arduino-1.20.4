package net.jamicah.arduinocraft;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.ModBlocks;
import net.jamicah.arduinocraft.block.custom.entity.ModBlockEntities;
import net.jamicah.arduinocraft.commands.Commands;
import net.jamicah.arduinocraft.event.SerialRead;
import net.jamicah.arduinocraft.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Arduinocraft implements ModInitializer {
	public static final String MOD_ID = "arduinocraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static SerialCom comPort;

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModBlockEntities.registerModBlockEntities();

		ClientTickEvents.END_WORLD_TICK.register(new SerialRead());

		ServerLifecycleEvents.SERVER_STOPPED.register(new SerialRead());


		Commands.registerCommands();

		// close port
		// SerialCom.closePort(comPort.comPort);
	}
}