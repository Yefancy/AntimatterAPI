package muramasa.antimatter.mixin;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Container.class)
public interface IContainerListeners {
    @Accessor
    List<IContainerListener> getListeners();
}
