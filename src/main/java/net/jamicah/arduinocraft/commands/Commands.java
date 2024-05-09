package net.jamicah.arduinocraft.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.minecraft.server.command.CommandManager.callWithContext;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands() {

        // /serial start <port name>
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("arduino")
                .then(literal("start")
                        .then(CommandManager.argument("select port", StringArgumentType.string())
                                .executes(context -> {

                            // open selected port
                            if (!SerialCom.isOpened) {
                                final String input = StringArgumentType.getString(context, "select port");
                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] §oSelected \'" + input + "\'"), false);
                                Arduinocraft.comPort = new SerialCom(input);
                            } else {
                                context.getSource().sendFeedback(() -> Text.literal("[ArduinoCraft] Error. Arduino communication has already started"), false);
                            }

                            return 1;
                        }))
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