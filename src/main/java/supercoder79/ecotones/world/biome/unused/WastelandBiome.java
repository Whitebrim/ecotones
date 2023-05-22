package supercoder79.ecotones.world.biome.unused;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import supercoder79.ecotones.api.BiomeRegistries;
import supercoder79.ecotones.api.SimpleTreeDecorationData;
import supercoder79.ecotones.blocks.EcotonesBlocks;
import supercoder79.ecotones.world.biome.BiomeHelper;
import supercoder79.ecotones.world.biome.EarlyBiomeRegistry;
import supercoder79.ecotones.world.biome.EcotonesBiomeBuilder;
import supercoder79.ecotones.world.decorator.CountExtraDecoratorConfig;
import supercoder79.ecotones.world.decorator.EcotonesDecorators;
import supercoder79.ecotones.world.decorator.ShrubDecoratorConfig;
import supercoder79.ecotones.world.decorator.Spread32Decorator;
import supercoder79.ecotones.world.features.EcotonesFeatures;
import supercoder79.ecotones.world.features.config.FeatureConfigHolder;
import supercoder79.ecotones.world.features.config.SimpleTreeFeatureConfig;
import supercoder79.ecotones.world.surface.EcotonesSurfaces;
import supercoder79.ecotones.world.surface.system.TernarySurfaceConfig;

public class WastelandBiome extends EcotonesBiomeBuilder {

    public static Biome INSTANCE;
    public static Biome THICKET;
    public static Biome FLATS;
    public static Biome HILLS;
    public static Biome SHRUB;
    public static Biome DEPTHS;

    public static void init() {
        INSTANCE = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland"), new WastelandBiome(0.2f, 0.65f, 0, 8, 1.25).build());
        THICKET = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland_thicket"), new WastelandBiome(0.8f, 0.65f, 0.1f, 4, 1.25).build());
        FLATS = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland_flats"), new WastelandBiome(0.3f, 0.65f, 0, 0.1, 1.5).build());
        HILLS = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland_hills"), new WastelandBiome(0.25f, 1.25f, 0.45f, 12, 0.7).build());
        SHRUB = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland_shrub"), new WastelandBiome(0, 0.65f, 0, 6, 1.25).build());
        DEPTHS = EarlyBiomeRegistry.register(new Identifier("ecotones", "wasteland_depths"), new WastelandBiome(0.1f, -0.25f, 0, 2, 1.25).build());

        BiomeRegistries.registerAllSpecial(id ->
                BiomeHelper.contains(id, "desert") || BiomeHelper.contains(id, "scrub"),
                INSTANCE, THICKET, FLATS, HILLS, SHRUB, DEPTHS);

        BiomeRegistries.registerBigSpecialBiome(INSTANCE, 150);
        BiomeRegistries.registerBiomeVariantChance(INSTANCE, 2);
        BiomeRegistries.registerBiomeVariants(INSTANCE, THICKET, FLATS, HILLS, SHRUB, DEPTHS);
    }


    protected WastelandBiome(float treeChance, float depth, float scale, double hilliness, double volatility) {
        this.surfaceBuilder(EcotonesSurfaces.WASTELAND_BUILDER, new TernarySurfaceConfig(EcotonesBlocks.DRIED_DIRT.getDefaultState(), EcotonesBlocks.DRIED_DIRT.getDefaultState(), Blocks.GRAVEL.getDefaultState()));

        this.depth(depth);
        this.scale(scale);
        this.temperature(1.7F);
        this.downfall(0.2F);

        this.precipitation(Biome.Precipitation.RAIN);

        this.skyColor(0xf5dd89);
        this.grassColor(0x705719);
        this.foliageColor(0x705719);

        this.hilliness(hilliness);
        this.volatility(volatility);

//         this.addStructureFeature(ConfiguredStructureFeatures.STRONGHOLD.value());

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.AIR.getDefaultState()))
                        .decorate(EcotonesDecorators.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.75f, 1)))
                        .spreadHorizontally()
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.SHRUB.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState()))
                        .decorate(EcotonesDecorators.SHRUB_PLACEMENT_DECORATOR.configure(new ShrubDecoratorConfig(2))));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.RANDOM_PATCH.configure(FeatureConfigHolder.SCRUBLAND_CONFIG)
                        .decorate(new Spread32Decorator())
                        .decorate(HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING))
                        .spreadHorizontally()
                        .decorate(NoiseThresholdCountPlacementModifier.of(-0.8D, 1, 2)));

        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                EcotonesFeatures.DEAD_TREE.configure(new SimpleTreeFeatureConfig(Blocks.OAK_LOG.getDefaultState(), Blocks.AIR.getDefaultState()))
                        .decorate(EcotonesDecorators.REVERSE_QUALITY_TREE_DECORATOR.configure(new SimpleTreeDecorationData(treeChance))));

        DefaultBiomeFeatures.addDefaultDisks(this.getGenerationSettings());
        DefaultBiomeFeatures.addLandCarvers(this.getGenerationSettings());
        //DefaultBiomeFeatures.addDefaultUndergroundStructures(this.getGenerationSettings());
        DefaultBiomeFeatures.addDungeons(this.getGenerationSettings());
        DefaultBiomeFeatures.addMineables(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultOres(this.getGenerationSettings());
        DefaultBiomeFeatures.addAmethystGeodes(this.getGenerationSettings());
        DefaultBiomeFeatures.addDefaultMushrooms(this.getGenerationSettings());
        DefaultBiomeFeatures.addSprings(this.getGenerationSettings());
        DefaultBiomeFeatures.addFrozenTopLayer(this.getGenerationSettings());

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
