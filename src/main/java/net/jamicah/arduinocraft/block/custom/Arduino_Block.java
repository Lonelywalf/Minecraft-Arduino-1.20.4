package net.jamicah.arduinocraft.block.custom;

import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.Chat;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Arduino_Block extends Block {
    public Arduino_Block(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {

        if (world.isReceivingRedstonePower(pos)) {

            if (SerialCom.isOpened) {
                SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 1);
            } else {
                Chat.sendMessage("§cArduino communication is disabled. Enable it by using /arduino start <port>");
            }


        } else if (!world.isReceivingRedstonePower(pos) && SerialCom.isOpened) {

            SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 0);

        }

        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

}
