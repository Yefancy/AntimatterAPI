package muramasa.antimatter.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import muramasa.antimatter.gui.ButtonBody;
import muramasa.antimatter.gui.ButtonOverlay;
import muramasa.antimatter.gui.GuiInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractSwitchWidget extends ButtonWidget {

    private final ResourceLocation res;
    private ButtonBody on, off;
    private ButtonOverlay body;
    private boolean state;

    protected final AbstractSwitchWidget.ISwitchable onSwitch;

    protected AbstractSwitchWidget(GuiInstance instance, ResourceLocation res, ButtonBody on, ButtonBody off, ISwitchable onSwitch) {
        super(instance, res, on, off, null);
        this.res = res;
        this.on = on;
        this.off = off;
        this.onSwitch = onSwitch;
        this.setClick(b -> onPress());
    }

    protected AbstractSwitchWidget(GuiInstance instance, ResourceLocation res, ButtonOverlay body, ISwitchable onSwitch, boolean defaultState) {
        super(instance, res, null, body, null);
        this.res = res;
        this.body = body;
        this.onSwitch = onSwitch;
        this.state = defaultState;
        this.setClick(b -> onPress());
    }

    protected AbstractSwitchWidget(GuiInstance instance, ResourceLocation res, ButtonOverlay body, String text, ISwitchable onSwitch) {
        super(instance, res, null, body, null);
        this.res = res;
        this.body = body;
        this.onSwitch = onSwitch;
        this.setClick(b -> onPress());
    }

    protected boolean state() {
        return state;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(res);
        RenderSystem.disableDepthTest();
        boolean mouseOver = isInside(mouseX, mouseY);
        if (body == null) {
            ButtonBody body = isSwitched() ? on : off;
            int xTex = body.getX();
            int yTex = body.getY();
            if (mouseOver) {
                xTex += body.getX2();
                yTex += body.getY2();
            }
            ScreenWidget.blit(matrixStack, realX(), realY(), getW(), getH(), xTex, yTex, body.getW(), body.getH(), 256, 256);
        } else {
            int xTex = body.getX();
            int yTex = body.getY();
            float f = isSwitched() ? 1.0F : mouseOver ? 0.75F : 0.5F;
            RenderSystem.color4f(f, f, f, 1.0F);
            ScreenWidget.blit(matrixStack, realX(), realY(), getW(), getH(), xTex, yTex, body.getW(), body.getH(), 256, 256);
        }
        RenderSystem.enableDepthTest();
    }
    public boolean isSwitched() {
        return state;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
    }

    public void onPress() {
        this.state = !this.state;
        this.onSwitch.onSwitch(this, isSwitched());
    }

    @OnlyIn(Dist.CLIENT)
    public interface ISwitchable {
        void onSwitch(AbstractSwitchWidget button, boolean state);
    }
}
