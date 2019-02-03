package muramasa.itech.api.capability.impl;

import muramasa.itech.common.tileentities.base.multi.TileEntityMultiMachine;

public class ControllerComponentHandler extends ComponentHandler {

    public ControllerComponentHandler(String componentId, TileEntityMultiMachine componentTile) {
        super(componentId, componentTile);
    }

    @Override
    public void linkController(TileEntityMultiMachine controllerTile) {
        //NOOP
    }

    @Override
    public void unlinkController(TileEntityMultiMachine controllerTile) {
        //NOOP
    }
}
