package net.jamicah.arduinocraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.Chat;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.jamicah.arduinocraft.block.custom.entity.Arduino_BlockEntity;
import net.jamicah.arduinocraft.block.custom.entity.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Arduino_Block extends BlockWithEntity {
    public static Boolean isInput = null;
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

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // this checks if the block is receiving redstone power

        if (SerialCom.isReceivingInput) {
            return 15;
        }
        return 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        // this checks if the block is receiving redstone power
            // if it is receiving redstone power while port is opened
        if (world.isReceivingRedstonePower(pos) && SerialCom.isOpened && isInput) {
            SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 1);

            // if not receiving redstone power while port is opened
        } else if (!world.isReceivingRedstonePower(pos) && SerialCom.isOpened && isInput) {
            SerialCom.digitalWrite(Arduinocraft.comPort.comPort, 0);

            // if receiving redstone power when port isn't opened
        } else if (world.isReceivingRedstonePower(pos) && !SerialCom.isOpened && !SerialCom.hasSentArduinoMessage) {
            Chat.sendMessage("§cArduino communication is disabled. Enable it by using /arduino start <port>");
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
}
