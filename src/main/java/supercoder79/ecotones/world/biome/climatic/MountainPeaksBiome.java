package supercoder79.ecotones.world.biome.climatic;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.feature.*;
import supercoder79.ecotones.world.biome.BiomeAssociations;
import supercoder79.ecotones.world.biome.EarlyBiomeRegistry;
import supercoder79.ecotones.world.surface.system.SurfaceBuilder;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.api.Climate;
import supercoder79.ecotones.api.ClimateType;
import supercoder79.ecotones.api.SimpleTreeDecorationData;
import supercoder79.ecotones.world.biome.BiomeHelper;
import supercoder79.ecotones.world.biome.EcotonesBiomeBuilder;
import supercoder79.ecotones.world.decorator.ChanceDecoratorConfig;
import supercoder79.ecotones.world.decorator.EcotonesDecorators;
import supercoder79.ecotones.world.decorator.ShrubDecoratorConfig;
import supercoder79.ecotones.world.decorator.Spread32Decorator;
import supercoder79.ecotones.world.features.EcotonesFeatures;
import supercoder79.ecotones.world.features.config.DuckweedFeatureConfig;
import supercoder79.ecotones.world.features.config.FeatureConfigHolder;
import supercoder79.ecotones.world.features.config.RockFeatureConfig;
import supercoder79.ecotones.world.features.config.SimpleTreeFeatureConfig;
import supercoder79.ecotones.world.structure.EcotonesStructures;
import supercoder79.ecotones.world.surface.EcotonesSurfaces;

public class MountainPeaksBiome extends EcotonesBiomeBuilder {
    public static Biome INSTANCE;

    public static void init() {
        INSTANCE = EarlyBiomeRegistry.register(new Identifier("ecotones", "mountain_peaks"), new MountainPeaksBiome(6.5F, 0.175F, 6, 0.93).build());
        BiomeRegistries.addMountainBiome(INSTANCE);
        BiomeRegistries.addMountainType(ClimateType.MOUNTAIN_PEAKS, INSTANCE);

        Climate.HOT_DESERT.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_VERY_DRY.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_DRY.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_MODERATE.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_MILD.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_HUMID.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_VERY_HUMID.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.HOT_RAINFOREST.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);

        Climate.WARM_DESERT.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_VERY_DRY.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_DRY.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_MODERATE.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_MILD.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_HUMID.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_VERY_HUMID.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
        Climate.WARM_RAINFOREST.add(ClimateType.MOUNTAIN_PEAKS, INSTANCE, 1.0);
    }

    public MountainPeaksBiome(float depth, float scale, double hilliness, double volatility) {
        this.surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.STONE_CONFIG);

        this.depth(depth);
        this.scale(scale);
        this.temperature(0.05F);
        this.downfall(0.3F);
        this.precipitation(Biome.Precipitation.SNOW);

        this.hilliness(hilliness);
        this.volatility(volatility);
        this.associate(BiomeAssociations.MOUNTAIN_LIKE);
//        this.category(Biome.Category.EXTREME_HILLS);

        DefaultBiomeFeatures.addLandCarvers(this.getGenerationSettings());
        DefaultBiomeFeatures.addPlainsTallGrass(this.getGenerationSettings());
        //DefaultBiomeFeatures.addDefaultUndergroundStructures(this.getGenerationSettings());
        DefaultBiomeFeatures.addDungeons(this.getGenerationSettings());
        DefaultBiomeFeatures.addMineables(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultOres(this.getGenerationSettings());
        DefaultBiomeFeatures.addAmethystGeodes(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultDisks(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultMushrooms(this.getGenerationSettings());
        DefaultBiomeFeatures.addSprings(this.getGenerationSettings());
        DefaultBiomeFeatures.addFrozenTopLayer(this.getGenerationSettings());

//         this.addStructureFeature(ConfiguredStructureFeatures.STRONGHOLD.value());

        this.addFeature(GenerationStep.Feature.RAW_GENERATION,
                EcotonesFeatures.ROCK.configure(new RockFeatureConfig(Blocks.STONE.getDefaultState(), 1))
                        .decorate(EcotonesDecorators.LARGE_ROCK.configure(new ChanceDecoratorConfig(4))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SURFACE_ROCKS)
                        .decorate(EcotonesDecorators.ROCKINESS.configure()));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SHORT_GRASS_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .decorate(NoiseThresholdCountPlacementModifier.of(-0.8D, 5, 7)));

        this.addFeature(GenerationStep.Feature.RAW_GENERATION,
                EcotonesFeatures.SMALL_ROCK.configure(FeatureConfig.DEFAULT)
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .applyChance(6));

        this.addFeature(GenerationStep.Feature.RAW_GENERATION,
                EcotonesFeatures.ROCK_SPIRE.configure(FeatureConfigHolder.STONE_SPIRE)
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .applyChance(8));

        this.addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
        this.addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.PIG, 10, 4, 4));
        this.addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
        this.addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.COW, 8, 4, 4));
        this.addSpawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 5, 1, 1));

        BiomeHelper.addDefaultFeatures(this);
    }
}
