package supercoder79.ecotones;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import supercoder79.ecotones.api.client.ModelDataRegistry;
import supercoder79.ecotones.blocks.EcotonesBlocks;
import supercoder79.ecotones.client.ClientPacketHandler;
import supercoder79.ecotones.client.Colors;
import supercoder79.ecotones.client.gui.EcotonesScreens;
import supercoder79.ecotones.client.model.DuckEntityModel;
import supercoder79.ecotones.client.model.EcotonesModelLayers;
import supercoder79.ecotones.client.particle.*;
import supercoder79.ecotones.client.render.block.EcotonesBlockEntityRenderers;
import supercoder79.ecotones.client.render.entity.DuckEntityRenderer;
import supercoder79.ecotones.client.render.magnifying.EcotonesMagnifyingGlassRenderers;
import supercoder79.ecotones.entity.EcotonesEntities;
import supercoder79.ecotones.items.EcotonesItemBlocks;
import supercoder79.ecotones.items.EcotonesItems;

@Environment(EnvType.CLIENT)
public final class EcotonesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EcotonesMagnifyingGlassRenderers.init();
        ClientPacketHandler.init();

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) -> view != null && pos != null ? BiomeColors.getFoliageColor(view, pos) : FoliageColors.getDefaultColor(),
                EcotonesBlocks.HAZEL_LEAVES);

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) -> view != null && pos != null ? BiomeColors.getGrassColor(view, pos) : FoliageColors.getDefaultColor(),
                EcotonesBlocks.WIDE_FERN,
                EcotonesBlocks.SHORT_GRASS,
                EcotonesBlocks.WILDFLOWERS,
                EcotonesBlocks.BLUEBELL,
                EcotonesBlocks.LAVENDER,
                EcotonesBlocks.SMALL_LILAC);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> FoliageColors.getDefaultColor(),
                EcotonesItemBlocks.getAll(
                EcotonesBlocks.SHORT_GRASS,
                        EcotonesBlocks.WIDE_FERN,
                        EcotonesBlocks.HAZEL_LEAVES));

        // Maple leaves
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> Colors.maple(),
                EcotonesItemBlocks.get(EcotonesBlocks.MAPLE_LEAVES));

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) -> Colors.maple(pos),
                EcotonesBlocks.MAPLE_LEAVES);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> Colors.larch(),
                EcotonesItemBlocks.get(EcotonesBlocks.LARCH_LEAVES));

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) -> Colors.larch(pos),
                EcotonesBlocks.LARCH_LEAVES);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                EcotonesBlocks.SHORT_GRASS,
                EcotonesBlocks.WILDFLOWERS,
                EcotonesBlocks.REEDS,
                EcotonesBlocks.COCONUT,
                EcotonesBlocks.HAZEL_LEAVES,
                EcotonesBlocks.HAZEL_SAPLING,
                EcotonesBlocks.SMALL_SHRUB,
                EcotonesBlocks.SANDY_GRASS,
                EcotonesBlocks.CLOVER,
                EcotonesBlocks.BLUEBELL,
                EcotonesBlocks.WIDE_FERN,
                EcotonesBlocks.SMALL_LILAC,
                EcotonesBlocks.CYAN_ROSE,
                EcotonesBlocks.LICHEN,
                EcotonesBlocks.MOSS,
                EcotonesBlocks.PINECONE,
                EcotonesBlocks.MAPLE_LEAVES,
                EcotonesBlocks.BLUEBERRY_BUSH,
                EcotonesBlocks.SWITCHGRASS,
                EcotonesBlocks.ROSEMARY,
                EcotonesBlocks.LAVENDER,
                EcotonesBlocks.SPRUCE_LEAF_PILE,
                EcotonesBlocks.MARIGOLD,
                EcotonesBlocks.MAPLE_SAPLING,
                EcotonesBlocks.LARCH_LEAVES,
                EcotonesBlocks.SMALL_CACTUS,
                EcotonesBlocks.POOFY_DANDELION,
                EcotonesBlocks.CATTAIL,
                EcotonesBlocks.DUCKWEED,
                EcotonesBlocks.POTTED_LARCH_SAPLING,
                EcotonesBlocks.POTTED_HAZEL_SAPLING,
                EcotonesBlocks.POTTED_MAPLE_SAPLING,
                EcotonesBlocks.THORN_BUSH,
                EcotonesBlocks.LARCH_SAPLING,
                EcotonesBlocks.FLAME_LILY,
                EcotonesBlocks.WATERGRASS
        );

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                EcotonesBlocks.BLUEBERRY_JAM_JAR,
                EcotonesBlocks.MAPLE_SYRUP_JAR);

        ParticleFactoryRegistry.getInstance().register(EcotonesParticles.SAND, SandParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EcotonesParticles.MAPLE_LEAF, MapleLeafParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EcotonesParticles.SYRUP_POP, SyrupPopParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EcotonesParticles.SAP_DRIP, SapDripParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EcotonesParticles.ASBESTOS, AsbestosParticle.Factory::new);

        EcotonesModelLayers.init();
        EcotonesScreens.init();
        EcotonesBlockEntityRenderers.init();

        ModelDataRegistry.register(EcotonesModelLayers.DUCK, DuckEntityModel.getTexturedModelData());
        EntityRendererRegistry.register(EcotonesEntities.DUCK, DuckEntityRenderer::new);
    }
}
