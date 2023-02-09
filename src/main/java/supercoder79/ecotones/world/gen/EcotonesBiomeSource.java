package supercoder79.ecotones.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.TopologicalSorts;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import supercoder79.ecotones.Ecotones;
import supercoder79.ecotones.api.BiomeIdManager;
import supercoder79.ecotones.api.CaveBiome;
import supercoder79.ecotones.api.Climate;
import supercoder79.ecotones.api.ModCompat;
import supercoder79.ecotones.util.noise.OpenSimplexNoise;
import supercoder79.ecotones.world.biome.cave.LimestoneCaveBiome;
import supercoder79.ecotones.world.layers.system.BiomeLayerSampler;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EcotonesBiomeSource extends BiomeSource implements CaveBiomeSource {
    public static Codec<EcotonesBiomeSource> CODEC =  RecordCodecBuilder.create((instance) -> {
        return instance.group(RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter((source) -> source.biomeRegistry),
                Codec.LONG.fieldOf("seed").stable().forGetter((source) -> source.seed),
                Codec.BOOL.fieldOf("real").stable().forGetter((source) -> source.isReal))
                .apply(instance, instance.stable(EcotonesBiomeSource::new));
    });

    public final Registry<Biome> biomeRegistry;
    private final BiomeLayerSampler biomeSampler;
    private final long seed;

    private final OpenSimplexNoise caveBiomeNoise;
    private final boolean isReal;

    public EcotonesBiomeSource(Registry<Biome> biomeRegistry, long seed, boolean isReal) {
        super(
                BiomeGenData.LOOKUP
                        .keySet()
                        .stream()
                        .map(biomeRegistry::getOrThrow)
                        .map(RegistryEntry.Direct::new)
        );
        this.biomeRegistry = biomeRegistry;
        this.biomeSampler = EcotonesBiomeLayers.build(seed);
        this.seed = seed;
        this.caveBiomeNoise = new OpenSimplexNoise(seed);
        this.isReal = isReal;

        if (isReal) {
            Ecotones.REGISTRY = this.biomeRegistry;
            ModCompat.run();

            BiomeIdManager.clear();

            for (Biome biome : this.biomeRegistry) {
                BiomeIdManager.register(this.biomeRegistry.getKey(biome).get(), this.biomeRegistry.getRawId(biome));
            }

//            System.out.println("============");
//            System.out.println(BiomeIdManager.MAP);
        }
    }

//    @Override
//    public List<BiomeSource.IndexedFeatures> method_39525(List<RegistryEntry<Biome>> biomes, boolean bl) {
//        // Not relevant for ecotones
//        return new ArrayList<>();
//    }

    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler sampler) {
        try {
            // TODO: no crash sampler
            return this.biomeRegistry.entryOf(this.biomeSampler.sampleKey(biomeX, biomeZ));
        } catch (Exception e) {
            e.printStackTrace();
            return new RegistryEntry.Direct<>(this.biomeRegistry.get(BiomeKeys.PLAINS));
        }
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

//    @Override
//    public BiomeSource withSeed(long seed) {
//        return new EcotonesBiomeSource(this.biomeRegistry, seed);
//    }

    @Override
    public CaveBiome getCaveBiomeForNoiseGen(int x, int z) {
        return this.caveBiomeNoise.sample(x / 256.0, z / 256.0) > 0.2 ? LimestoneCaveBiome.INSTANCE : CaveBiome.DEFAULT;
    }

    @Nullable
    public Pair<BlockPos, RegistryEntry<Biome>> locateBiome(
            BlockPos origin,
            int radius,
            int horizontalBlockCheckInterval,
            int verticalBlockCheckInterval,
            Predicate<RegistryEntry<Biome>> predicate,
            MultiNoiseUtil.MultiNoiseSampler noiseSampler,
            WorldView world
    ) {
        Set<RegistryEntry<Biome>> set = this.getBiomes().stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
        System.out.println(set);
        System.out.println("----------");
        System.out.println(getBiomes());
        System.out.println("----------");
        if (set.isEmpty()) {
            return null;
        } else {
            int i = Math.floorDiv(radius, horizontalBlockCheckInterval);
            int[] is = MathHelper.stream(origin.getY(), world.getBottomY() + 1, world.getTopY(), verticalBlockCheckInterval).toArray();

            for(BlockPos.Mutable mutable : BlockPos.iterateInSquare(BlockPos.ORIGIN, i, Direction.EAST, Direction.SOUTH)) {
                int j = origin.getX() + mutable.getX() * horizontalBlockCheckInterval;
                int k = origin.getZ() + mutable.getZ() * horizontalBlockCheckInterval;
                int l = BiomeCoords.fromBlock(j);
                int m = BiomeCoords.fromBlock(k);

                for(int n : is) {
                    int o = BiomeCoords.fromBlock(n);
                    RegistryEntry<Biome> registryEntry = this.getBiome(l, o, m, noiseSampler);
                    if (set.contains(registryEntry)) {
                        return Pair.of(new BlockPos(j, n, k), registryEntry);
                    }
                }
            }

            return null;
        }
    }
}
