package net.jamicah.arduinocraft.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands() {

        // /arduino start <port> <output/input> <baudrate>
        // /arduino stop
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("arduino")
                        .executes(context -> {
                            SerialCom.isReceivingInput = !SerialCom.isReceivingInput;
                            return 1;
                        })
                .then(literal("start")
                        .then(CommandManager.argument("select port", StringArgumentType.string())
                                .then(literal("output")
                                                .then(literal("digital")
                                                        .then(CommandManager.argument("baudrate", IntegerArgumentType.integer())
                                                                .executes(context -> {

                                                                    // open selected port & make the block OUTPUT only (Arduino to Minecraft)
                                                                    if (!SerialCom.isOpened) {
                                                                        final int baudrate = IntegerArgumentType.getInteger(context, "baudrate");
                                                                        final String input = StringArgumentType.getString(context, "select port");
                                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                                        "[ArduinoCraft] §oSelected '" + input + "' with the baudrate of " + baudrate),
                                                                                false
                                                                        );
                                                                        Arduinocraft.comPort = new SerialCom(input, baudrate);

                                                                    } else {
                                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                                        "[ArduinoCraft] Error. Arduino communication has already started"),
                                                                                false);
                                                                    }
                                                                    Arduino_Block.isDigital = true;
                                                                    Arduino_Block.isAnalog = false;
                                                                    Arduino_Block.isInput = false;
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                                .then(literal("analog")
                                                        .then(CommandManager.argument("baudrate", IntegerArgumentType.integer())
                                                                .executes(context -> {

                                                                    // open selected port & make the block OUTPUT only (Arduino to Minecraft)
                                                                    if (!SerialCom.isOpened) {
                                                                        final Integer baudrate = IntegerArgumentType.getInteger(context, "baudrate");
                                                                        final String input = StringArgumentType.getString(context, "select port");
                                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                                        "[ArduinoCraft] §oSelected '" + input + "' with the baudrate of " + baudrate),
                                                                                false
                                                                        );
                                                                        Arduinocraft.comPort = new SerialCom(input, baudrate);

                                                                    } else {
                                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                                        "[ArduinoCraft] Error. Arduino communication has already started"),
                                                                                false
                                                                        );
                                                                    }
                                                                    Arduino_Block.isAnalog = true;
                                                                    Arduino_Block.isDigital = false;
                                                                    Arduino_Block.isInput = false;
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                .then(literal("input")
                                        .then(CommandManager.argument("Baudrate", IntegerArgumentType.integer())
                                                .executes(context -> {

                                                    // open selected port & make the block OUTPUT only (Arduino to Minecraft)
                                                    if (!SerialCom.isOpened) {
                                                        final int baudrate = IntegerArgumentType.getInteger(context, "baudrate");
                                                        final String input = StringArgumentType.getString(context, "select port");
                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                "[ArduinoCraft] §oSelected '" + input + "' with the baudrate of " + baudrate),
                                                                false
                                                        );
                                                        Arduinocraft.comPort = new SerialCom(input, baudrate);

                                                    } else {
                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                "[ArduinoCraft] Error. Arduino communication has already started"),
                                                                false
                                                        );
                                                    }
                                                    Arduino_Block.isInput = true;
                                                    return 1;
                                                })
                                        )
                                )
                        )
                ).then(literal("stop")
                        .executes(context -> {
                            if (SerialCom.isOpened) {
                                SerialCom.closePort(Arduinocraft.comPort.comPort);
                                context.getSource().sendFeedback(() -> Text.literal(
                                        "[ArduinoCraft] §aClosed Arduino communication"),
                                        false
                                );
                                SerialCom.hasSentArduinoMessage = false;
                            } else {
                                context.getSource().sendFeedback(() -> Text.literal(
                                        "[ArduinoCraft] §cError. Arduino communication wasn't started, so it can't be stopped"),
                                        false
                                );
                            }


                            return 1;
                        })
                    )
                )
            )
        );
    }
}