package muramasa.antimatter.ore;

import muramasa.antimatter.Data;
import muramasa.antimatter.block.BlockMaterialType;
import muramasa.antimatter.material.Material;
import muramasa.antimatter.registration.ISharedAntimatterObject;
import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;

public class BlockOreStone extends BlockMaterialType implements ISharedAntimatterObject {

    public BlockOreStone(String domain, Material material) {
        super(domain, material, Data.ORE_STONE, Block.Properties.create(net.minecraft.block.material.Material.ROCK).harvestLevel(material.getMiningLevel() > -1 ? material.getMiningLevel() : 0).setRequiresTool().harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.0F, 7.5F));
        instancedTextures("stone");
    }

    @Override
    public boolean registerColorHandlers() {
        return false;
    }
}
