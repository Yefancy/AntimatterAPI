package muramasa.antimatter.tesseract;

import muramasa.antimatter.AntimatterConfig;
import muramasa.antimatter.tile.pipe.TileEntityPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import tesseract.Tesseract;
import tesseract.api.capability.TesseractGTCapability;
import tesseract.api.gt.GTConsumer;
import tesseract.api.gt.IEnergyHandler;
import tesseract.api.gt.IGTNode;

import java.util.function.Supplier;

public class EnergyTileWrapper implements IGTNode {

    private final TileEntity tile;
    private final IEnergyStorage storage;

    private final GTConsumer.State state = new GTConsumer.State(this);

    private EnergyTileWrapper(TileEntity tile, IEnergyStorage storage) {
        this.tile = tile;
        this.storage = storage;
    }

    public static void wrap(TileEntityPipe pipe, World world, BlockPos pos, Direction side, Supplier<TileEntity> supplier) {
        Tesseract.GT_ENERGY.registerNode(world, pos.toLong(), () -> {
            TileEntity tile = supplier.get();
            if (tile == null) {
                pipe.clearInteract(side);
                return null;
            }
            LazyOptional<IEnergyHandler> capability = tile.getCapability(TesseractGTCapability.ENERGY_HANDLER_CAPABILITY, side.getOpposite());
            if (capability.isPresent()) {
                capability.addListener(o -> pipe.onInvalidate(side));
                return capability.resolve().get();
            } else {
                LazyOptional<IEnergyStorage> cap = tile.getCapability(CapabilityEnergy.ENERGY);
                if (cap.isPresent()) {
                    EnergyTileWrapper node = new EnergyTileWrapper(tile, cap.orElse(null));
                    capability.addListener(o -> pipe.onInvalidate(side));
                    return node;
                }
            }
            pipe.clearInteract(side);
            return null;
        });
    }
    @Override
    public long insert(long maxReceive, boolean simulate) {
        return storage.receiveEnergy((int)(maxReceive * AntimatterConfig.GAMEPLAY.EU_TO_FE_RATIO), simulate);
    }

    @Override
    public long extract(long maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public long getEnergy() {
        return storage.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public int getOutputAmperage() {
        return 0;
    }

    @Override
    public int getOutputVoltage() {
        return 0;
    }

    @Override
    public int getInputAmperage() {
        return 1;
    }

    @Override
    public int getInputVoltage() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canOutput() {
        return false;
    }

    @Override
    public boolean canInput() {
        return storage.canReceive();
    }

    @Override
    public boolean canInput(Direction dir) {
        return canInput();
    }

    @Override
    public boolean canOutput(Direction direction) {
        return false;
    }

    @Override
    public GTConsumer.State getState() {
        return state;
    }
}
