package supercoder79.ecotones.world.biome.climatic;

import net.minecraft.block.Blocks;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.feature.*;
import supercoder79.ecotones.util.compat.AurorasDecoCompat;
import supercoder79.ecotones.world.biome.BiomeAssociations;
import supercoder79.ecotones.world.biome.EarlyBiomeRegistry;
import supercoder79.ecotones.world.data.EcotonesData;
import supercoder79.ecotones.world.decorator.Spread32Decorator;
import supercoder79.ecotones.world.surface.system.SurfaceBuilder;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.api.Climate;
import supercoder79.ecotones.api.SimpleTreeDecorationData;
import supercoder79.ecotones.api.TreeType;
import supercoder79.ecotones.world.biome.BiomeHelper;
import supercoder79.ecotones.world.biome.EcotonesBiomeBuilder;
import supercoder79.ecotones.world.decorator.EcotonesDecorators;
import supercoder79.ecotones.world.decorator.ShrubDecoratorConfig;
import supercoder79.ecotones.world.features.EcotonesFeatures;
import supercoder79.ecotones.world.features.config.FeatureConfigHolder;
import supercoder79.ecotones.world.features.config.OakTreeFeatureConfig;
import supercoder79.ecotones.world.features.config.SimpleTreeFeatureConfig;
import supercoder79.ecotones.world.structure.EcotonesStructures;

public class FlowerPrairieBiome extends EcotonesBiomeBuilder {
    public static Biome INSTANCE;
    public static Biome HILLY;
    public static Biome MOUNTAINOUS;

    public static void init() {
        INSTANCE = EarlyBiomeRegistry.register(new Identifier("ecotones", "flower_prairie"), new FlowerPrairieBiome(0.5F, 0.025F).build());
        HILLY = EarlyBiomeRegistry.register(new Identifier("ecotones", "flower_prairie_hilly"), new FlowerPrairieBiome(1.25F, 0.225F).build());
        MOUNTAINOUS = EarlyBiomeRegistry.register(new Identifier("ecotones", "flower_prairie_mountainous"), new FlowerPrairieBiome(2F, 0.625F).build());
        BiomeRegistries.registerMountains(INSTANCE, HILLY, MOUNTAINOUS);
        Climate.WARM_MODERATE.add(INSTANCE, 0.2);
    }

    public FlowerPrairieBiome(float depth, float scale) {
        this.surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG);

        this.depth(depth);
        this.scale(scale);
        this.temperature(1F);
        this.downfall(0.4F);

        this.grassColor(0xabcf59);
        this.foliageColor(0xabcf59);

        // TODO: customize
        this.hilliness(2.0);
        this.volatility(1.1);

        this.precipitation(Biome.Precipitation.RAIN);
        this.associate(BiomeAssociations.PLAINS_LIKE);
//        this.category(Biome.Category.PLAINS);

//        this.addStructureFeature(StructureFeature.VILLAGE.configure(new StructurePoolFeatureConfig(() -> PlainsVillageData.STRUCTURE_POOLS, 7)));
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

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SURFACE_ROCKS)
                        .decorate(EcotonesDecorators.ROCKINESS.configure()));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.PRAIRIE_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .decorate(NoiseThresholdCountPlacementModifier.of(-0.8D, 16, 18)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.CLOVER)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .repeat(1)
        );

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SHRUB_PLACEMENT_DECORATOR.configure(new ShrubDecoratorConfig(2))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.WIDE_SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SHRUB_PLACEMENT_DECORATOR.configure(new ShrubDecoratorConfig(0.15))));

        DefaultBiomeFeatures.addForestFlowers(this.getGenerationSettings());

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SWITCHGRASS_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .applyChance(2));

        // FIXME
//        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
//                EcotonesFeatures.RANDOM_PATCH.configure(
//                        (new RandomPatchFeatureConfig.Builder(ForestFlowerBlockStateProvider.INSTANCE, SimpleBlockPlacer.INSTANCE))
//                                .tries(64).build())
//                        .decorate(new Spread32Decorator())
//                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
//                        .spreadHorizontally()
//                        .repeat(100));
//
//        // TODO
//        this.getGenerationSettings().feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION_COMMON);

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.DENSE_LAVENDER_LILAC)
                        .decorate(EcotonesDecorators.DATA_FUNCTION.configure(EcotonesData.FLOWER_MOSAIC)));

        AurorasDecoCompat.applyDaffodils(this);

        this.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS,
                EcotonesFeatures.BEEHIVES.configure(FeatureConfig.DEFAULT)
                        .applyChance(32)
                        .spreadHorizontally()
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SMALL_LILAC)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .repeat(2));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.STRAIGHT_OAK.configure(new OakTreeFeatureConfig(12, 7))
                        .decorate(EcotonesDecorators.TREE_DECORATOR.configure(TreeType.RARE_LARGE_CLUSTERED_OAK.decorationData)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.DEAD_TREE.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.AIR.getDefaultState()))
                        .decorate(EcotonesDecorators.REVERSE_QUALITY_TREE_DECORATOR.configure(new SimpleTreeDecorationData(0.1))));

        BiomeHelper.addDefaultSpawns(this.getSpawnSettings());
        BiomeHelper.addDefaultFeatures(this);
    }
}
