package muramasa.antimatter.tile.pipe;

import muramasa.antimatter.Ref;
import muramasa.antimatter.capability.Dispatch;
import muramasa.antimatter.capability.pipe.PipeCoverHandler;
import muramasa.antimatter.capability.pipe.PipeFluidHandler;
import muramasa.antimatter.pipe.types.FluidPipe;
import muramasa.antimatter.tesseract.FluidTileWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tesseract.Tesseract;
import tesseract.api.GraphWrapper;
import tesseract.api.capability.TesseractFluidCapability;
import tesseract.api.fluid.FluidController;
import tesseract.api.fluid.IFluidNode;
import tesseract.api.fluid.IFluidPipe;

import java.util.List;

public class TileEntityFluidPipe<T extends FluidPipe<T>> extends TileEntityPipe<T> implements IFluidPipe, Dispatch.Sided<IFluidHandler> {

    protected LazyOptional<PipeFluidHandler> fluidHandler;

    public TileEntityFluidPipe(T type, boolean covered) {
        super(type, covered);
        if (fluidHandler == null) {
            fluidHandler = FluidController.SLOOSH ? LazyOptional.of(() -> new PipeFluidHandler(this, 1000 * (getPipeSize().ordinal() + 1), 1000, 1, 0)) : LazyOptional.empty();
        }
        pipeCapHolder.set(() -> this);
    }

    @Override
    protected GraphWrapper getWrapper() {
        return Tesseract.FLUID;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains(Ref.KEY_MACHINE_FLUIDS))
            fluidHandler.ifPresent(t -> t.deserializeNBT(tag.getCompound(Ref.KEY_MACHINE_FLUIDS)));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        fluidHandler.ifPresent(t -> tag.put(Ref.KEY_MACHINE_FLUIDS, t.serializeNBT()));
        return nbt;
    }

    @Override
    public void addNode(Direction side) {
        Tesseract.FLUID.registerNode(getWorld(), pos.offset(side).toLong(), side.getOpposite(), (pos, dir) -> {
            TileEntity tile = world.getTileEntity(BlockPos.fromLong(pos));
            if (tile == null) {
                return null;
            }
            LazyOptional<IFluidHandler> capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);
            if (capability.isPresent()) {
                FluidTileWrapper node = new FluidTileWrapper(tile, capability.orElse(null));
                capability.addListener(o -> this.onSideCapInvalidate(side));
                return node;
            } else {
                return null;
            }
        });
    }

    @Override
    public void onRemove() {
        fluidHandler.ifPresent(t -> t.onRemove());
        fluidHandler.invalidate();
        super.onRemove();
    }

    @Override
    public boolean isGasProof() {
        return getPipeType().isGasProof();
    }

    @Override
    public int getCapacity() {
        return getPipeType().getCapacity(getPipeSize());
    }

    @Override
    public int getPressure() {
        return getPipeType().getPressure(getPipeSize()) * 20;
    }

    @Override
    public int getTemperature() {
        return getPipeType().getTemperature();
    }

    @Override
    public boolean connects(Direction direction) {
        return canConnect(direction.getIndex());
    }

    @Override
    public boolean validate(Direction dir) {
        if (!super.validate(dir)) return false;
        TileEntity tile = world.getTileEntity(getPos().offset(dir));
        if (tile == null) return false;
        return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite()).isPresent();
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public LazyOptional<? extends IFluidHandler> forSide(Direction side) {
        if (FluidController.SLOOSH) {
            if (fluidHandler == null) {
                fluidHandler = LazyOptional.of(() -> new PipeFluidHandler(this, 1000 * (getPipeSize().ordinal() + 1), 1000, 1, 0));
            }
        } else {
            return LazyOptional.of(() -> new TesseractFluidCapability(this, side));
        }
        return fluidHandler;
    }

    @Override
    public LazyOptional<? extends IFluidHandler> forNullSide() {
        return forSide(null);
    }

    @Override
    public void refresh() {

    }

    public static class TileEntityCoveredFluidPipe<T extends FluidPipe<T>> extends TileEntityFluidPipe<T> implements ITickablePipe {

        public TileEntityCoveredFluidPipe(T type) {
            super(type, true);
        }

        @Override
        public LazyOptional<PipeCoverHandler<?>> getCoverHandler() {
            return this.coverHandler;
        }
    }

    @Override
    public IFluidNode getNode() {
        return this.fluidHandler.orElse(null);
    }

    @Override
    public List<String> getInfo() {
        List<String> list = super.getInfo();
        fluidHandler.ifPresent(t -> {
            for (int i = 0; i < t.getTanks(); i++) {
                FluidStack stack = t.getFluidInTank(i);
                list.add(stack.getFluid().getRegistryName().toString() + " " + stack.getAmount() + " mb.");
            }
        });
        list.add("Pressure: " + getPipeType().getPressure(getPipeSize()));
        list.add("Capacity: " + getPipeType().getCapacity(getPipeSize()));
        list.add("Max temperature: " + getPipeType().getTemperature());
        list.add(getPipeType().isGasProof() ? "Gas proof." : "Cannot handle gas.");
        return list;
    }
}
