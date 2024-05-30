package net.jamicah.arduinocraft;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Chat {
    // send messages to chat, but easier
    public static void sendMessage(String message) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("§f[ArduinoCraft]§r " + message), false);
    }
}
