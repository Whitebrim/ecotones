package supercoder79.ecotones.biome.special;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NoiseHeightmapDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import supercoder79.ecotones.biome.HumidityLayer2Biomes;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.features.EcotonesFeatures;
import supercoder79.ecotones.features.config.ShrubConfig;
import supercoder79.ecotones.features.foliage.SmallPineFoliagePlacer;
import supercoder79.ecotones.treedecorator.PineConeTreeDecorator;

public class PinePeaksBiome extends Biome {
    public static BranchedTreeFeatureConfig SMALL_PINE_CONFIG = new BranchedTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(Blocks.SPRUCE_LOG.getDefaultState()),
            new SimpleBlockStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState()),
            new SmallPineFoliagePlacer(1, 0))
            .baseHeight(9)
            .heightRandA(6)
            .trunkTopOffset(1)
            .foliageHeight(4)
            .foliageHeightRandom(2)
            .noVines()
            .build();

    public static PinePeaksBiome INSTANCE;

    public static void init() {
        INSTANCE = Registry.register(Registry.BIOME, new Identifier("ecotones", "pine_peaks"), new PinePeaksBiome());
        BiomeRegistries.registerSpecialBiome(Registry.BIOME.getRawId(INSTANCE), id ->
                id == Registry.BIOME.getRawId(HumidityLayer2Biomes.BOREAL_FOREST_BIOME) || id ==Registry.BIOME.getRawId(HumidityLayer2Biomes.SPRUCE_FOREST_BIOME));
    }


    protected PinePeaksBiome() {
        super((new Settings())
                .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
                .precipitation(Precipitation.RAIN)
                .category(Category.PLAINS)
                .depth(0.5F)
                .scale(1.1F)
                .temperature(0.8F)
                .downfall(0.5F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .build()).parent(null)
                .noises(ImmutableList.of(new MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 1.0F))));

        this.addStructureFeature(Feature.MINESHAFT.configure(new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL)));
        this.addStructureFeature(Feature.STRONGHOLD.configure(FeatureConfig.DEFAULT));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.SHRUB.configure(new ShrubConfig(Blocks.SPRUCE_LOG.getDefaultState(), Blocks.SPRUCE_LEAVES.getDefaultState()))
                        .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(3, 0.5f, 1))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                Feature.NORMAL_TREE.configure(SMALL_PINE_CONFIG).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(16, 0.66f, 1))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(1, 0.66f, 1))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.TAIGA_GRASS_CONFIG).createDecoratedFeature(Decorator.NOISE_HEIGHTMAP_DOUBLE.configure(new NoiseHeightmapDecoratorConfig(-0.8D, 5, 10))));

        DefaultBiomeFeatures.addDefaultDisks(this);
        DefaultBiomeFeatures.addLandCarvers(this);
        DefaultBiomeFeatures.addDefaultStructures(this);
        DefaultBiomeFeatures.addDungeons(this);
        DefaultBiomeFeatures.addMineables(this);
        DefaultBiomeFeatures.addDefaultOres(this);
        DefaultBiomeFeatures.addDefaultMushrooms(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addFrozenTopLayer(this);
    }
}