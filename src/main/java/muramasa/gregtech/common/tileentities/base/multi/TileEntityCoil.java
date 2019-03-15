package muramasa.gregtech.common.tileentities.base.multi;

import muramasa.gregtech.api.capability.IComponentHandler;
import muramasa.gregtech.api.capability.impl.ComponentHandler;
import muramasa.gregtech.api.enums.Coil;
import muramasa.gregtech.common.blocks.BlockCoil;
import muramasa.gregtech.common.tileentities.base.TileEntityBase;

public class TileEntityCoil extends TileEntityBase implements IComponent {

    protected IComponentHandler componentHandler = new ComponentHandler("null", this) {
        @Override
        public String getId() {
            return ((BlockCoil) getState().getBlock()).getType().getName();
        }
    };

    public Coil getType() {
        return ((BlockCoil) getState().getBlock()).getType();
    }

    @Override
    public IComponentHandler getComponentHandler() {
        return componentHandler;
    }

    public int getHeatingCapacity() {
        return getType().getHeatingCapacity();
    }
}
