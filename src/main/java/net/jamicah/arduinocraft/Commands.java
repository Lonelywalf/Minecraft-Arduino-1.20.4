package net.jamicah.arduinocraft;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;


import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands() {

        // /serial start <port name>
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("serial")
                .then(literal("start")
                        .then(CommandManager.argument("select port", StringArgumentType.string()).executes(context -> {
                            // For versions below 1.19, use ''new LiteralText''.
                            // For versions above 1.20, use directly the ''Text'' object instead of supplier.
                            final String value = StringArgumentType.getString(context, "select port");
                            context.getSource().sendFeedback(() -> Text.literal("Selected \'" + value + "\'"), false);

                            return 1;
                        }))


                )
        )));
    }
}
/*
.executes(context -> {
                            // For versions below 1.19, use ''new LiteralText''.
                            // For versions below 1.20, use directly the ''Text'' object instead of supplier.
                            context.getSource().sendFeedback(() -> Text.literal("Called foo with bar"), false);

                            return 1;
                        })
 */