package muramasa.itech.proxy;

import muramasa.itech.api.items.MetaItem;
import muramasa.itech.api.items.MetaTool;
import muramasa.itech.client.render.ModelLoader;
import muramasa.itech.client.render.models.ModelCable;
import muramasa.itech.client.render.models.ModelMachine;
import muramasa.itech.client.render.models.ModelOre;
import muramasa.itech.common.blocks.BlockMachines;
import muramasa.itech.common.blocks.BlockOres;
import muramasa.itech.common.items.ItemBlockOres;
import muramasa.itech.loaders.ContentLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        ModelLoaderRegistry.registerLoader(new ModelLoader());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MetaItem.ColorHandler(), ContentLoader.metaItem);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MetaTool.ColorHandler(), ContentLoader.metaTool);

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new BlockOres.ColorHandler(), ContentLoader.blockOres);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemBlockOres.ColorHandler(), Item.getItemFromBlock(ContentLoader.blockOres));

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new BlockMachines.ColorHandler(), ContentLoader.blockMachines);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
//        ContentLoader.blockMachines.initItemModel();
//        ContentLoader.blockMultiMachines.initItemModel();
//        ContentLoader.blockHatches.initItemModel();
    }

    @Override
    public void serverStarting(FMLServerStartingEvent e) {
        //NOOP
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ContentLoader.metaItem.initModel();
        ContentLoader.metaTool.initModel();
        ContentLoader.blockMachines.initModel();
        ContentLoader.blockOres.initModel();
        ContentLoader.blockCables.initModel();
        ContentLoader.blockCasings.initModel();
        ContentLoader.blockCoils.initModel();

        //TODO avoid multiple instances of ModelMachine, static lists?
        ModelMachine modelMachine = new ModelMachine();
        ModelLoader.register(ContentLoader.blockMachines, modelMachine);
        ModelLoader.register(ContentLoader.blockMultiMachines, modelMachine);
        ModelLoader.register(ContentLoader.blockHatches, modelMachine);
        ModelLoader.register(ContentLoader.blockCables, new ModelCable());
        ModelLoader.register(ContentLoader.blockOres, new ModelOre());
    }
}
