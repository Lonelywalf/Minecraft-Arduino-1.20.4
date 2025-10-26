package net.jamicah.arduinocraft.block.custom.entity;

import net.jamicah.arduinocraft.Arduinocraft;
import net.jamicah.arduinocraft.block.custom.Arduino_Block;
import net.jamicah.arduinocraft.arduino.SerialCom;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Arduino_BlockEntity extends BlockEntity {

    private Boolean lastDigitalState = null;
    private Integer lastAnalogValue = null;

    public Arduino_BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARDUINO_BLOCK_ENTITY, pos, state);
    }

    @SuppressWarnings("unused")
    public void tick(World world, BlockPos pos, BlockState state, Arduino_BlockEntity blockEntity) {
        if (world == null || world.isClient) return; // only run on server

        Arduinocraft.LOGGER.debug("Arduino_BlockEntity.tick at " + pos + " (server). isDigital=" + Arduino_Block.isDigital + ", isAnalog=" + Arduino_Block.isAnalog + ", isInput=" + Arduino_Block.isInput);

        // If the block outputs digital signals, check for changes
        if (Boolean.TRUE.equals(Arduino_Block.isDigital) && !Boolean.TRUE.equals(Arduino_Block.isInput)) {
            Boolean current = Boolean.TRUE.equals(SerialCom.isReceivingInput);
            Arduinocraft.LOGGER.debug("Arduino_BlockEntity.tick (digital) at " + pos + " current=" + current + " last=" + lastDigitalState);
            if (lastDigitalState == null || !lastDigitalState.equals(current)) {
                // state changed, notify neighbors (force update)
                Arduinocraft.LOGGER.info("Arduino_BlockEntity: digital state changed at " + pos + " -> " + current + ", calling updateNeighborsAlways");
                world.updateNeighborsAlways(pos, state.getBlock());
                world.updateListeners(pos, state, state, 3);
                lastDigitalState = current;
            }
        }

        // If the block outputs analog signals, check for changes
        if (Boolean.TRUE.equals(Arduino_Block.isAnalog) && !Boolean.TRUE.equals(Arduino_Block.isInput)) {
            int currentAnalog = SerialCom.analogSignal;
            Arduinocraft.LOGGER.debug("Arduino_BlockEntity.tick (analog) at " + pos + " currentAnalog=" + currentAnalog + " lastAnalog=" + lastAnalogValue);
            if (lastAnalogValue == null || lastAnalogValue != currentAnalog) {
                Arduinocraft.LOGGER.info("Arduino_BlockEntity: analog value changed at " + pos + " -> " + currentAnalog + ", calling updateNeighborsAlways");
                world.updateNeighborsAlways(pos, state.getBlock());
                world.updateListeners(pos, state, state, 3);
                lastAnalogValue = currentAnalog;
            }
        }

        // keep existing behavior as fallback
        world.updateNeighbors(pos, state.getBlock());
    }
}
