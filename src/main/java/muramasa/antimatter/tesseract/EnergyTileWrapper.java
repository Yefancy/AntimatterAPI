package muramasa.antimatter.tesseract;

import muramasa.antimatter.AntimatterConfig;
import muramasa.antimatter.cover.Cover;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import tesseract.Tesseract;
import tesseract.api.electric.IElectricNode;
import tesseract.util.Dir;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnergyTileWrapper implements IElectricNode, ITileWrapper {

    private TileEntity tile;
    private boolean removed;
    private IEnergyStorage storage;
    
    private EnergyTileWrapper(TileEntity tile, IEnergyStorage storage) {
        this.tile = tile;
        this.storage = storage;
    }

    @Nullable
    public static EnergyTileWrapper of(TileEntity tile) {
        LazyOptional<IEnergyStorage> capability = tile.getCapability(CapabilityEnergy.ENERGY);
        if (capability.isPresent()) {
            EnergyTileWrapper node = new EnergyTileWrapper(tile, capability.orElse(null));
            capability.addListener(o -> node.onRemove(null));
            Tesseract.ELECTRIC.registerNode(tile.getWorld().getDimension().getType().getId(), tile.getPos().toLong(), node);
            return node;
        }
        return null;
    }

    @Override
    public void onRemove(@Nullable Direction side) {
        if (side == null && tile.isRemoved()) {
            Tesseract.ELECTRIC.remove(tile.getWorld().getDimension().getType().getId(), tile.getPos().toLong());
            removed = true;
        }
    }

    @Override
    public void onUpdate(Direction side, Cover cover) {

    }

    @Override
    public boolean isRemoved() {
        return removed;
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
    public boolean canOutput(Dir direction) {
        return false;
    }

    @Override
    public boolean connects(Dir direction) {
        return true;
    }
}
