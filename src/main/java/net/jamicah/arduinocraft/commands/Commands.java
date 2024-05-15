package net.jamicah.arduinocraft.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.World;


import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.minecraft.server.command.CommandManager.callWithContext;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands() {

        // /serial start <port name>
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("arduino")
                        .executes(context -> {
                            if (!SerialCom.isReceivingInput) {
                                SerialCom.isReceivingInput = true;

                            } else {
                                SerialCom.isReceivingInput = false;
                            }

                            return 1;
                        })
                .then(literal("start")
                        .then(CommandManager.argument("select port", StringArgumentType.string())
                                .then(literal("output")
                                        .executes(context -> {

                                            // open selected port & make the block OUTPUT only (Arduino to Minecraft)
                                            if (!SerialCom.isOpened) {
                                                final String input = StringArgumentType.getString(context, "select port");
                                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] §oSelected \'" + input + "\'"), false);
                                                Arduinocraft.comPort = new SerialCom(input);

                                            } else {
                                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] Error. Arduino communication has already started"), false);
                                            }
                                            Arduino_Block.isInput = false;
                                            return 1;
                                }))
                                .then(literal("input")
                                        .executes(context -> {
                                            // open selected port & make the block INPUT only (Minecraft -> Arduino)
                                            if (!SerialCom.isOpened) {
                                                final String input = StringArgumentType.getString(context, "select port");
                                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] §oSelected \'" + input + "\'"), false);
                                                Arduinocraft.comPort = new SerialCom(input);
                                            } else {
                                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] Error. Arduino communication has already started"), false);
                                            }
                                            Arduino_Block.isInput = true;
                                            return 1;
                                        })))
                ).then(literal("stop")
                        .executes(context -> {
                            if (SerialCom.isOpened) {
                                SerialCom.closePort(Arduinocraft.comPort.comPort);
                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] §aClosed Arduino communication"), false);
                                return 1;
                            } else {
                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] §cError. Arduino communication wasn't started, so it can't be stopped"), false);
                            }


                            return 1;
                        })
        ))));
    }
}