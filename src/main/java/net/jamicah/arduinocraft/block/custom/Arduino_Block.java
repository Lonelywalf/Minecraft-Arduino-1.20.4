package net.jamicah.arduinocraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.Chat;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.custom.entity.Arduino_BlockEntity;
import net.jamicah.arduinocraft.block.custom.entity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Arduino_Block extends BlockWithEntity {
    public static Boolean isInput = false;
    public static Boolean isDigital = false;
    public static Boolean isAnalog = false;
    public Arduino_Block(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // this gets the redstone power level to output to adjacent blocks
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // If the block is configured to output digital signals from the Arduino to Minecraft
        if (Boolean.TRUE.equals(isDigital) && !Boolean.TRUE.equals(isInput)) {
            int power = Boolean.TRUE.equals(SerialCom.isReceivingInput) ? 15 : 0;
            Arduinocraft.LOGGER.debug("Arduino_Block.getWeakRedstonePower (digital) at " + pos + " -> " + power);
            return power;
        }

        // If the block is configured to output analog signals from the Arduino to Minecraft
        if (Boolean.TRUE.equals(isAnalog) && !Boolean.TRUE.equals(isInput)) {
            int power = SerialCom.analogSignal;
            Arduinocraft.LOGGER.debug("Arduino_Block.getWeakRedstonePower (analog) at " + pos + " -> " + power);
            return power;
        }

        // Default: no redstone power (e.g., block is in input mode or not configured)
        Arduinocraft.LOGGER.debug("Arduino_Block.getWeakRedstonePower (default) at " + pos + " -> 0");
        return 0;


    }
    // this checks if the block is receiving redstone power and writes signal to the arduino
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {

            // if it is receiving redstone power while port is opened and the block is in input mode (Minecraft->Arduino)
        if (world.isReceivingRedstonePower(pos) && SerialCom.isOpened && Boolean.TRUE.equals(isInput)) {
            SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 1);

            // if not receiving redstone power while port is opened
        } else if (!world.isReceivingRedstonePower(pos) && SerialCom.isOpened && Boolean.TRUE.equals(isInput)) {
            SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 0);

            // if receiving redstone power when port isn't opened
        } else if (world.isReceivingRedstonePower(pos) && !SerialCom.isOpened && !SerialCom.hasSentArduinoMessage) {
            Chat.sendMessage(
                    "§cArduino communication is disabled." +
                            " Enable it by using /arduino start <port> <input/output> <baudrate>"
            );
            SerialCom.hasSentArduinoMessage = true;
        }

        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new Arduino_BlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.ARDUINO_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        int power = getWeakRedstonePower(state, world, pos, direction);
        Arduinocraft.LOGGER.debug("Arduino_Block.getStrongRedstonePower at " + pos + " -> " + power);
        return power;
    }
}
