package andres451.potionsmod;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.gamerule.TreecapitatorHelper;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

public class VinesHelper {
	private static final int MAX = 64;
	private static final int SOUND_EVERY = 8;
	private static final int[][] FACES = {
		{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
	};

	private static Map<Block<?>, String> families;

	private static void add(String family, Block<?>... blocks) {
		for (Block<?> b : blocks) if (b != null) families.put(b, family);
	}

	private static String family(Block<?> block) {
		if (families == null) {
			families = new HashMap<>();
			add("coal", Blocks.ORE_COAL_STONE, Blocks.ORE_COAL_BASALT, Blocks.ORE_COAL_LIMESTONE,
				Blocks.ORE_COAL_GRANITE, Blocks.ORE_COAL_PERMAFROST);
			add("iron", Blocks.ORE_IRON_STONE, Blocks.ORE_IRON_BASALT, Blocks.ORE_IRON_LIMESTONE,
				Blocks.ORE_IRON_GRANITE, Blocks.ORE_IRON_PERMAFROST);
			add("gold", Blocks.ORE_GOLD_STONE, Blocks.ORE_GOLD_BASALT, Blocks.ORE_GOLD_LIMESTONE,
				Blocks.ORE_GOLD_GRANITE, Blocks.ORE_GOLD_PERMAFROST);
			add("lapis", Blocks.ORE_LAPIS_STONE, Blocks.ORE_LAPIS_BASALT, Blocks.ORE_LAPIS_LIMESTONE,
				Blocks.ORE_LAPIS_GRANITE, Blocks.ORE_LAPIS_PERMAFROST);
			add("redstone", Blocks.ORE_REDSTONE_STONE, Blocks.ORE_REDSTONE_BASALT, Blocks.ORE_REDSTONE_LIMESTONE,
				Blocks.ORE_REDSTONE_GRANITE, Blocks.ORE_REDSTONE_PERMAFROST,
				Blocks.ORE_REDSTONE_GLOWING_STONE, Blocks.ORE_REDSTONE_GLOWING_BASALT,
				Blocks.ORE_REDSTONE_GLOWING_LIMESTONE, Blocks.ORE_REDSTONE_GLOWING_GRANITE,
				Blocks.ORE_REDSTONE_GLOWING_PERMAFROST);
			add("diamond", Blocks.ORE_DIAMOND_STONE, Blocks.ORE_DIAMOND_BASALT, Blocks.ORE_DIAMOND_LIMESTONE,
				Blocks.ORE_DIAMOND_GRANITE, Blocks.ORE_DIAMOND_PERMAFROST);
			add("nethercoal", Blocks.ORE_NETHERCOAL_NETHERRACK, Blocks.ORE_NETHERCOAL_BASALT,
				Blocks.ORE_NETHERCOAL_GLOOMSTONE);
		}
		return families.get(block);
	}

	private static long key(int x, int y, int z) {
		return ((long) x & 0x3FFFFFF) << 38 | ((long) z & 0x3FFFFFF) << 12 | (y & 0xFFF);
	}

	public static boolean run(World world, Player player, Block<?> block, TilePosc origin) {
		if (world.isClientSide || player.isSneaking() || !ModEffects.has(player, ModEffects.VINES)) return false;

		if (block.getLogic() instanceof BlockLogicLog) {
			return new TreecapitatorHelper(world, origin.x(), origin.y(), origin.z(), player).chopTree();
		}

		String fam = family(block);
		if (fam == null) return false;

		// FIND ORES
		List<TilePos> found = new ArrayList<>();
		Set<Long> seen = new HashSet<>();
		ArrayDeque<TilePos> queue = new ArrayDeque<>();
		TilePos start = new TilePos(origin.x(), origin.y(), origin.z());
		seen.add(key(start.x, start.y, start.z));
		queue.add(start);
		while (!queue.isEmpty() && found.size() < MAX) {
			TilePos p = queue.poll();
			for (int[] f : FACES) {
				int nx = p.x + f[0], ny = p.y + f[1], nz = p.z + f[2];
				if (!seen.add(key(nx, ny, nz))) continue;
				TilePos n = new TilePos(nx, ny, nz);
				if (!fam.equals(family(world.getBlockType(n)))) continue;
				found.add(n);
				queue.add(n);
				if (found.size() >= MAX) break;
			}
		}

		// BREAK ORES
		int i = 0;
		for (TilePos p : found) {
			Block<?> b = world.getBlockType(p);
			if (!player.canHarvestBlock(b)) break;
			int data = world.getBlockData(p);

			if (i++ % SOUND_EVERY == 0) world.playBlockEvent(p, 2001, b.id());

			if (!world.setBlockTypeNotify(p, Blocks.AIR)) continue;
			b.onHarvest(world, player, p, data, null);
			ItemStack tool = player.getCurrentEquippedItem();
			if (tool != null && tool.isItemStackDamageable()) {
				tool.damageItem(1, player);
				if (tool.stackSize <= 0) {
					player.destroyCurrentEquippedItem();
					break;
				}
			}
		}
		return false;
	}
}
