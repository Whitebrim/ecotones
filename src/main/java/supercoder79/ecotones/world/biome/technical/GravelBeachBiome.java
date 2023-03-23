package supercoder79.ecotones.world.biome.technical;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placementmodifier.HeightmapPlacementModifier;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.world.biome.BiomeAssociations;
import supercoder79.ecotones.world.biome.BiomeHelper;
import supercoder79.ecotones.world.decorator.Spread32Decorator;
import supercoder79.ecotones.world.features.EcotonesConfiguredFeature;
import supercoder79.ecotones.world.features.EcotonesFeatures;
import supercoder79.ecotones.world.surface.system.SurfaceBuilder;
import supercoder79.ecotones.world.biome.EcotonesBiomeBuilder;
import supercoder79.ecotones.world.features.config.FeatureConfigHolder;

public class GravelBeachBiome extends EcotonesBiomeBuilder {
    public static Biome INSTANCE;

    public static void init() {
        INSTANCE = Registry.register(BuiltinRegistries.BIOME, new Identifier("ecotones", "gravel_beach"), new GravelBeachBiome().build());

        BiomeRegistries.registerBeach(INSTANCE);
    }

    protected GravelBeachBiome() {
        this.surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRAVEL_CONFIG);

        this.depth(-0.025F);
        this.scale(-0.07F);
        this.temperature(0.9F);
        this.downfall(0.9F);

        this.precipitation(Biome.Precipitation.RAIN);
        this.associate(BiomeAssociations.BEACH_LIKE);

//        this.category(Biome.Category.BEACH);

        BiomeHelper.addDefaultFeatures(this);

//        this.addStructureFeature(ConfiguredStructureFeatures.MINESHAFT);
//        this.addStructureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
//        this.addStructureFeature(ConfiguredStructureFeatures.SHIPWRECK_BEACHED);

        DefaultBiomeFeatures.addLandCarvers(this.getGenerationSettings());
        //DefaultBiomeFeatures.addDefaultUndergroundStructures(this.getGenerationSettings());
        DefaultBiomeFeatures.addDungeons(this.getGenerationSettings());
        DefaultBiomeFeatures.addMineables(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultOres(this.getGenerationSettings());
        DefaultBiomeFeatures.addAmethystGeodes(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultDisks(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultFlowers(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultGrass(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultMushrooms(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultVegetation(this.getGenerationSettings());
        DefaultBiomeFeatures.addSprings(this.getGenerationSettings());
        DefaultBiomeFeatures.addFrozenTopLayer(this.getGenerationSettings());

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SURFACE_ROCKS)
                        .decorate(new Spread32Decorator())
                        .spreadHorizontally()
                        .repeat(24));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, EcotonesConfiguredFeature.wrap(Feature.SEAGRASS, (new ProbabilityConfig(0.6f)))
                .decorate(HeightmapPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG))
                .spreadHorizontally()
                .repeat(64));

        this.addSpawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        this.addSpawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 5, 1, 1));
    }
}
