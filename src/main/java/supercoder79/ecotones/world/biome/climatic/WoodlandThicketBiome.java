package supercoder79.ecotones.world.biome.climatic;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.HeightmapPlacementModifier;
import net.minecraft.world.gen.placementmodifier.NoiseThresholdCountPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import supercoder79.ecotones.world.biome.BiomeAssociations;
import supercoder79.ecotones.world.decorator.Spread32Decorator;
import supercoder79.ecotones.world.features.EcotonesConfiguredFeature;
import supercoder79.ecotones.world.surface.system.SurfaceBuilder;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.api.Climate;
import supercoder79.ecotones.api.SimpleTreeDecorationData;
import supercoder79.ecotones.world.biome.BiomeHelper;
import supercoder79.ecotones.world.biome.EcotonesBiomeBuilder;
import supercoder79.ecotones.world.decorator.EcotonesDecorators;
import supercoder79.ecotones.world.decorator.ShrubDecoratorConfig;
import supercoder79.ecotones.world.features.EcotonesFeatures;
import supercoder79.ecotones.world.features.config.FeatureConfigHolder;
import supercoder79.ecotones.world.features.config.SimpleTreeFeatureConfig;
import supercoder79.ecotones.world.structure.EcotonesStructures;

import java.util.OptionalInt;

public class WoodlandThicketBiome extends EcotonesBiomeBuilder {
    public static Biome INSTANCE;
    public static Biome CLEARING;
    public static Biome HILLY;
    public static Biome HILLY_CLEARING;

    public static void init() {
        INSTANCE = Registry.register(BuiltinRegistries.BIOME, new Identifier("ecotones", "woodland_thicket"), new WoodlandThicketBiome(0.5F, 0.125F, 2.1, 1, 8).build());
        CLEARING = Registry.register(BuiltinRegistries.BIOME, new Identifier("ecotones", "woodland_clearing"), new WoodlandThicketBiome(0.5F, 0.0F, 1.2, 0.88, 2).build());
        HILLY = Registry.register(BuiltinRegistries.BIOME, new Identifier("ecotones", "woodland_hilly"), new WoodlandThicketBiome(1.2F, 0.8F, 3.2, 0.7, 6).build());
        HILLY_CLEARING = Registry.register(BuiltinRegistries.BIOME, new Identifier("ecotones", "woodland_clearing_hilly"), new WoodlandThicketBiome(1.2F, 0.2F, 1.6, 0.8, 2).build());
        BiomeRegistries.registerBiomeVariantChance(INSTANCE, 4);
        BiomeRegistries.registerBiomeVariants(INSTANCE, CLEARING, HILLY, HILLY_CLEARING);
        Climate.WARM_VERY_HUMID.add(INSTANCE, 0.3);
        Climate.HOT_VERY_HUMID.add(INSTANCE, 0.2);
        Climate.HOT_HUMID.add(INSTANCE, 0.15);
    }

    public WoodlandThicketBiome(float depth, float scale, double hilliness, double volatility, int treeAmt) {
        this.surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG);

        this.depth(depth);
        this.scale(scale);
        this.temperature(1F);
        this.downfall(0.4F);

        this.precipitation(Biome.Precipitation.RAIN);

        this.hilliness(hilliness);
        this.volatility(volatility);
        this.associate(BiomeAssociations.FOREST_LIKE);
//        this.category(Biome.Category.FOREST);

//         this.addStructureFeature(ConfiguredStructureFeatures.STRONGHOLD.value());
        this.addStructureFeature(EcotonesStructures.CAMPFIRE_OAK);

        DefaultBiomeFeatures.addLandCarvers(this.getGenerationSettings());
        DefaultBiomeFeatures.addPlainsTallGrass(this.getGenerationSettings());
        DefaultBiomeFeatures.addPlainsFeatures(this.getGenerationSettings());
        //DefaultBiomeFeatures.addDefaultUndergroundStructures(this.getGenerationSettings());
        DefaultBiomeFeatures.addDungeons(this.getGenerationSettings());
        DefaultBiomeFeatures.addMineables(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultOres(this.getGenerationSettings());
        DefaultBiomeFeatures.addAmethystGeodes(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultDisks(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultMushrooms(this.getGenerationSettings());
        DefaultBiomeFeatures.addSprings(this.getGenerationSettings());
        DefaultBiomeFeatures.addFrozenTopLayer(this.getGenerationSettings());
        BiomeHelper.addDefaultFeatures(this);

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SURFACE_ROCKS)
                        .repeat(1)
                        .spreadHorizontally()
                        .decorate(new Spread32Decorator()));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.RARELY_SHORT_GRASS_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .decorate(NoiseThresholdCountPlacementModifier.of(-0.8D, 10, 20)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SHRUB_PLACEMENT_DECORATOR.configure(new ShrubDecoratorConfig(6))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesConfiguredFeature.wrap(Feature.TREE, (new TreeFeatureConfig.Builder(
                                BlockStateProvider.of(Blocks.OAK_LOG.getDefaultState()),
                                new LargeOakTrunkPlacer(3, 11, 0),
                                BlockStateProvider.of(Blocks.OAK_LEAVES.getDefaultState()),
//                        BlockStateProvider.of(Blocks.OAK_SAPLING.getDefaultState()),
                                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
                                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))
                                .ignoreVines().build()))
                        .decorate(EcotonesDecorators.SIMPLE_TREE_DECORATOR.configure(new SimpleTreeDecorationData(treeAmt + 0.5))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SWITCHGRASS_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .applyChance(2));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.POPLAR_TREE.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SIMPLE_TREE_DECORATOR.configure(new SimpleTreeDecorationData(0.3))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.DEAD_TREE.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.AIR.getDefaultState()))
                        .decorate(EcotonesDecorators.REVERSE_QUALITY_TREE_DECORATOR.configure(new SimpleTreeDecorationData(0.125))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.WIDE_SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SHRUB_PLACEMENT_DECORATOR.configure(new ShrubDecoratorConfig(2))));

        this.addFeature(GenerationStep.Feature.RAW_GENERATION,
                EcotonesFeatures.SMALL_ROCK.configure(FeatureConfig.DEFAULT)
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .applyChance(12));

        BiomeHelper.addDefaultSpawns(this.getSpawnSettings());
    }
}
