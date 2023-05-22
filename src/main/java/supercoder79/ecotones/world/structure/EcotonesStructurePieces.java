package supercoder79.ecotones.world.structure;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import supercoder79.ecotones.Ecotones;
import supercoder79.ecotones.util.RegistryReport;
import supercoder79.ecotones.world.structure.gen.CampfireGenerator;
import supercoder79.ecotones.world.structure.gen.CottageGenerator;

public class EcotonesStructurePieces {
    public static final StructurePieceType.Simple CAMPFIRE = CampfireGenerator.Piece::new;
    public static final StructurePieceType.Simple COTTAGE_CENTER = CottageGenerator.CenterRoom::new;
    public static final StructurePieceType.Simple COTTAGE_PORCH = CottageGenerator.Porch::new;
    public static final StructurePieceType.Simple COTTAGE_FARM = CottageGenerator.Farm::new;
    public static final StructurePieceType.ManagerAware OUTPOST = OutpostStructure.OutpostPiece::new;

    public static void init() {
        register("campfire", CAMPFIRE);
        register("cottage_center", COTTAGE_CENTER);
        register("cottage_porch", COTTAGE_PORCH);
        register("cottage_farm", COTTAGE_FARM);
        register("outpost", OUTPOST);
    }

    private static void register(String name, StructurePieceType piece) {
        Registry.register(Registries.STRUCTURE_PIECE, Ecotones.id(name), piece);
        RegistryReport.increment("Structure Pieces");
    }
}
