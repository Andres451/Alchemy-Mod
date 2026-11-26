package andres451.potionsmod.registry;

import static andres451.potionsmod.PotionsMod.MOD_ID;

import andres451.potionsmod.alchemy_table.BlockLogicAlchemyTable;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.BlockSounds;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public class ModBlocks {
	public static final int ALCHEMY_TABLE_ID = 12000;
	public static final int ALCHEMY_TABLE_ACTIVE_ID = 12001;

	public static Block<BlockLogicAlchemyTable> alchemyTable;
	public static Block<BlockLogicAlchemyTable> alchemyTableActive;

	public static void initBlocks() {
		alchemyTable = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(2.5F)
			.setResistance(10.0F)
			.setTags(BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.TROMMEL_IDLE))
			.build("alchemy_table", ALCHEMY_TABLE_ID, b -> new BlockLogicAlchemyTable(b, false));

		alchemyTableActive = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(2.5F)
			.setResistance(10.0F)
			.setLuminance(13)
			.setStatParent(() -> alchemyTable)
			.setTags(BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("alchemy_table_active", ALCHEMY_TABLE_ACTIVE_ID, b -> new BlockLogicAlchemyTable(b, true));
	}
}
