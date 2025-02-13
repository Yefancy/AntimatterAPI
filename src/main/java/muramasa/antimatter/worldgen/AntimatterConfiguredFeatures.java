package muramasa.antimatter.worldgen;

import muramasa.antimatter.Ref;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class AntimatterConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> VEIN_LAYER = register("stone_layer", AntimatterWorldGenerator.VEIN_LAYER.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> ORE = register("stone_layer", AntimatterWorldGenerator.ORE.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> ORE_SMALL = register("stone_layer", AntimatterWorldGenerator.ORE_SMALL.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> STONE_LAYER = register("stone_layer", AntimatterWorldGenerator.STONE_LAYER.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> SURFACE_ROCK = register("stone_layer", AntimatterWorldGenerator.SURFACE_ROCK.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG));

    public static ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<?, ?> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Ref.ID, id), feature);
    }

    public static void init() {
    }
}
