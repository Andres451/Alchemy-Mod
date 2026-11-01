package andres451.potionsmod;

import andres451.potionsmod.gui.PotionsScreens;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockLogicAlchemyTable extends BlockLogicRotatable {
	public final boolean isActive;

	public BlockLogicAlchemyTable(Block<?> block, boolean active) {
		super(block, Materials.STONE);
		this.isActive = active;
		block.withEntity(TileEntityAlchemyTable::new);
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return switch (dropCause) {
			case PICK_BLOCK, EXPLOSION, PROPER_TOOL, SILK_TOUCH, PISTON_CRUSH ->
				new ItemStack[]{new ItemStack(ModBlocks.alchemyTable)};
			default -> null;
		};
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc pos, @NotNull Player player,
								Side side, double xPlaced, double yPlaced) {
		if (!world.isClientSide) {
			TileEntityAlchemyTable table = (TileEntityAlchemyTable) world.getTileEntity(pos);
			((PotionsScreens) player).potions$displayAlchemyTableScreen(table);
		}
		return true;
	}

	public static void updateBlockState(@NotNull World world, @NotNull TilePos tilePos, boolean lit) {
		if (!(world.getTileEntity(tilePos) instanceof TileEntityAlchemyTable)) {
			return;
		}
		int meta = world.getBlockData(tilePos);
		Block<? extends BlockLogic> block = lit ? ModBlocks.alchemyTableActive : ModBlocks.alchemyTable;
		world.setBlockTypeDataRaw(tilePos, block, meta);
		world.notifyBlockChange(tilePos, block);
	}
}
