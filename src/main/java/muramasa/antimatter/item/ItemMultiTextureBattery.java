package muramasa.antimatter.item;

import muramasa.antimatter.Ref;
import muramasa.antimatter.client.RenderHelper;
import muramasa.antimatter.datagen.providers.AntimatterItemModelProvider;
import muramasa.antimatter.machine.Tier;
import muramasa.antimatter.texture.Texture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ItemMultiTextureBattery extends ItemBattery{
    public ItemMultiTextureBattery(String domain, String id, Tier tier, long cap, boolean reusable) {
        super(domain, id, tier, cap, reusable);
        if (FMLEnvironment.dist.isClient()){
            RenderHelper.registerBatteryPropertyOverrides(this);
        }
    }

    @Override
    public void onItemModelBuild(IItemProvider item, AntimatterItemModelProvider prov) {
        String id = this.getId();
        ItemModelBuilder builders[] = new ItemModelBuilder[6];
        for (int i = 0; i < 6; i++){
            ItemModelBuilder builder = prov.getBuilder(id + i);
            builder.parent(new ModelFile.UncheckedModelFile(new ResourceLocation("minecraft","item/handheld")));
            builder.texture("layer0", new Texture(getDomain(), "item/basic/" + getId() + "/" + i));
            builders[i] = builder;
        }

        prov.tex(item, "minecraft:item/handheld", new Texture(getDomain(), "item/basic/" + getId() + "/1")).override().predicate(new ResourceLocation(Ref.ID, "battery"), 0.0F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"0"))).end().override().predicate(new ResourceLocation(Ref.ID, "battery"), 0.2F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"1"))).end().override().predicate(new ResourceLocation(Ref.ID, "battery"), 0.4F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"2"))).end().override().predicate(new ResourceLocation(Ref.ID, "battery"), 0.6F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"3"))).end().override().predicate(new ResourceLocation(Ref.ID, "battery"), 0.8F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"4"))).end().override().predicate(new ResourceLocation(Ref.ID, "battery"), 1.0F).model(new ModelFile.UncheckedModelFile(new ResourceLocation(Ref.ID, "item/" + id +"5")));
    }
}
