package muramasa.antimatter.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import muramasa.antimatter.gui.GuiInstance;
import muramasa.antimatter.gui.IGuiElement;
import muramasa.antimatter.gui.Widget;

public class BackgroundWidget extends Widget {

    private final int xSize, ySize;

    public BackgroundWidget(GuiInstance instance, int xSize, int ySize) {
        super(instance);
        this.xSize = xSize;
        this.ySize = ySize;
        setDepth(-1);
    }

    @Override
    public void setParent(IGuiElement parent) {
        super.setParent(parent);
        this.setDepth(parent.depth()-1);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void renderBackground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack, mouseX, mouseY, partialTicks);
        drawTexture(stack, gui.handler.getGuiTexture(), realX(),realY(), 0,0, xSize, ySize);
    }
}
