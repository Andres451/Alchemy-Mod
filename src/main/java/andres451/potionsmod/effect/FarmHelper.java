package andres451.potionsmod.effect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import andres451.potionsmod.PotionsMod;
import andres451.potionsmod.registry.ModEffects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicCropsWheat;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

import static andres451.potionsmod.Constants.*;
import static andres451.potionsmod.Config.*;

public class FarmHelper {

	private static final int[][] OFFSETS = {
		{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
	};

	private static class Chain {
		Block<?> block;
		List<TilePos> layer = new ArrayList<>();
		Set<Long> seen = new HashSet<>();
		int count = 0;
		int max;
	}

	private static final Map<Player, Chain> CHAINS = new WeakHashMap<>();

	private static long key(int x, int y, int z) {
		return ((long) x & 0x3FFFFFF) << 38 | ((long) z & 0x3FFFFFF) << 12 | (y & 0xFFF);
	}

	private static boolean isCrop(Block<?> block) {
		return block != null
			&& (block.getLogic() instanceof BlockLogicCropsWheat
			|| block.getMaterial() == Materials.VEGETABLE
			|| modCrops().contains(block));
	}

	private static List<TilePos> neighbors(World world, Chain c, TilePosc from) {
		List<TilePos> out = new ArrayList<>();
		for (int[] o : OFFSETS) {
			int nx = from.x() + o[0], ny = from.y(), nz = from.z() + o[1];
			if (!c.seen.add(key(nx, ny, nz))) continue;
			TilePos n = new TilePos(nx, ny, nz);
			if (world.getBlockType(n) == c.block) out.add(n);
		}
		return out;
	}

	public static void onBreak(World world, Player player, Block<?> block, TilePosc origin) {
		if (world == null || world.isClientSide || block == null) return;
		if (!ModEffects.has(player, ModEffects.FARM) || !isCrop(block)) return;
		Chain c = new Chain();
		c.max = ModEffects.scaleCount(FARM_MAX_BLOCKS, ModEffects.strength(player, ModEffects.FARM));
		c.block = block;
		c.seen.add(key(origin.x(), origin.y(), origin.z()));
		c.layer.addAll(neighbors(world, c, origin));
		if (!c.layer.isEmpty()) CHAINS.put(player, c);
	}

	public static void tick(Player p, int tickCount) {
		World world = p.world;
		if (world.isClientSide) return;
		if (tickCount % FARM_CHAIN_STEP_TICKS == 0) stepChain(world, p);
		if (tickCount % FARM_BONEMEAL_INTERVAL == 0) bonemealUnder(world, p);
	}

	private static void stepChain(World world, Player p) {
		Chain c = CHAINS.get(p);
		if (c == null) return;
		List<TilePos> next = new ArrayList<>();
		boolean sound = true;
		for (TilePos pos : c.layer) {
			if (c.count >= c.max) break;
			if (world.getBlockType(pos) != c.block) continue;
			int data = world.getBlockData(pos);
			if (sound) { world.playBlockEvent(pos, 2001, c.block.id()); sound = false; }
			if (!world.setBlockTypeNotify(pos, Blocks.AIR)) continue;
			c.block.onHarvest(world, p, pos, data, null);
			c.count++;
			next.addAll(neighbors(world, c, pos));
		}
		if (next.isEmpty() || c.count >= c.max) CHAINS.remove(p);
		else c.layer = next;
	}

	private static void bonemealUnder(World world, Player p) {
		int radius = ModEffects.scale(FARM_BONEMEAL_RADIUS, ModEffects.strength(p, ModEffects.FARM));
		int cx = MathHelper.floor(p.x), cz = MathHelper.floor(p.z);
		int y0 = MathHelper.floor(p.y);
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				for (int y = y0; y >= y0 - 2; y--) {
					TilePos pos = new TilePos(cx + dx, y, cz + dz);
					Block<?> b = world.getBlockType(pos);
					if (b != null && b.getLogic() instanceof IBonemealable bm) {
						ItemStack meal = new ItemStack(Items.DYE, 1, DyeColor.WHITE.itemMeta);
						bm.onBonemealUsed(meal, p, world, pos, Side.TOP, 0.5, 0.5);
					}
				}
			}
		}
	}


	private static Set<Block<?>> modCrops;

	private static Set<Block<?>> modCrops() {
		if (modCrops == null) {
			modCrops = new HashSet<>();
			if (FabricLoader.getInstance().isModLoaded("stardew")) {
				try {
					for (Field f : Class.forName(STARDEW_BLOCKS).getFields()) {
						String n = f.getName();
						if (n.startsWith("CROPS_") && !n.endsWith("_TOP") && f.get(null) instanceof Block<?> b) {
							modCrops.add(b);
							PotionsMod.LOGGER.info("Potions.Farm: stardew crop {}", n);
						}
					}
				} catch (Exception e) {
					PotionsMod.LOGGER.warn("Potions.Farm: error stardew crops", e);
				}
			}
		}
		return modCrops;
	}
}
