package supercoder79.ecotones.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import supercoder79.ecotones.api.DevOnly;
import supercoder79.ecotones.world.biome.EarlyBiomeRegistry;

@DevOnly
public final class BiomeChecker {
    public static void check(Biome biome) {
        Identifier id = EarlyBiomeRegistry.getId(biome);
        boolean hasSpawns = false;
        for (SpawnGroup value : SpawnGroup.values()) {
            if (!biome.getSpawnSettings().getSpawnEntries(value).getEntries().isEmpty()) {
                hasSpawns = true;
            }
        }

        if (!hasSpawns) {
            System.out.println("Biome [" + id + "] has no spawns");
        }

//        if (!biome.getGenerationSettings().hasStructureFeature(StructureFeature.STRONGHOLD)) {
//            System.out.println("Biome [" + id + "] has no stronghold");
//        }
    }
}
