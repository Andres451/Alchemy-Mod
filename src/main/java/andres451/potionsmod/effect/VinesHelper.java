package andres451.potionsmod.effect;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import andres451.potionsmod.PotionsMod;
import andres451.potionsmod.registry.ModEffects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.gamerule.TreecapitatorHelper;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

import static andres451.potionsmod.Constants.*;
import static andres451.potionsmod.Config.*;

public class VinesHelper {
	private static final int SOUND_EVERY = 8;
	private static final int[][] FACES = {
		{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
	};

	private static final String[] AETHER_LOGS = {"LOG_SKYROOT", "LOG_OAK_GOLDEN", "LOG_AETHER_SCORCHED"};
	private static final String[] UNIVERSAL_AXES = {"TOOL_AXE_PHOENIX", "TOOL_AXE_VALKYRIE"};
	private static final String[] UNIVERSAL_PICKAXES = {"TOOL_PICKAXE_PHOENIX", "TOOL_PICKAXE_VALKYRIE"};

	private static final String[] STARDEW_LOGS = {"LOG_APPLE", "LOG_APPLE_GOLDEN"};

	private static boolean ready = false;
	private static final Map<Block<?>, String> oreFamily = new HashMap<>();
	private static final Set<Block<?>> aetherBlocks = new HashSet<>();
	private static final Set<Item> aetherTools = new HashSet<>();
	private static final Set<Item> universalAxes = new HashSet<>();
	private static final Set<Item> universalPickaxes = new HashSet<>();

	private static void ore(String family, Block<?>... blocks) {
		for (Block<?> b : blocks) if (b != null) oreFamily.put(b, family);
	}

	private static Object field(String className, String name) {
		try {
			Field f = Class.forName(className).getField(name);
			return f.get(null);
		} catch (Exception e) {
			PotionsMod.LOGGER.warn("Vines: {}.{} not found", className, name);
			return null;
		}
	}

	private static void aetherOre(String family, String... fields) {
		for (String name : fields) {
			if (field(AETHER_BLOCKS, name) instanceof Block<?> b) {
				oreFamily.put(b, family);
				aetherBlocks.add(b);
			}
		}
	}

	private static void aetherUniversal(Set<Item> target, String... fields) {
		for (String name : fields) {
			if (field(AETHER_ITEMS, name) instanceof Item item) target.add(item);
		}
	}

	private static void init() {
		ready = true;

		ore("coal", Blocks.ORE_COAL_STONE, Blocks.ORE_COAL_BASALT, Blocks.ORE_COAL_LIMESTONE,
			Blocks.ORE_COAL_GRANITE, Blocks.ORE_COAL_PERMAFROST);
		ore("iron", Blocks.ORE_IRON_STONE, Blocks.ORE_IRON_BASALT, Blocks.ORE_IRON_LIMESTONE,
			Blocks.ORE_IRON_GRANITE, Blocks.ORE_IRON_PERMAFROST);
		ore("gold", Blocks.ORE_GOLD_STONE, Blocks.ORE_GOLD_BASALT, Blocks.ORE_GOLD_LIMESTONE,
			Blocks.ORE_GOLD_GRANITE, Blocks.ORE_GOLD_PERMAFROST);
		ore("lapis", Blocks.ORE_LAPIS_STONE, Blocks.ORE_LAPIS_BASALT, Blocks.ORE_LAPIS_LIMESTONE,
			Blocks.ORE_LAPIS_GRANITE, Blocks.ORE_LAPIS_PERMAFROST);
		ore("redstone", Blocks.ORE_REDSTONE_STONE, Blocks.ORE_REDSTONE_BASALT, Blocks.ORE_REDSTONE_LIMESTONE,
			Blocks.ORE_REDSTONE_GRANITE, Blocks.ORE_REDSTONE_PERMAFROST,
			Blocks.ORE_REDSTONE_GLOWING_STONE, Blocks.ORE_REDSTONE_GLOWING_BASALT,
			Blocks.ORE_REDSTONE_GLOWING_LIMESTONE, Blocks.ORE_REDSTONE_GLOWING_GRANITE,
			Blocks.ORE_REDSTONE_GLOWING_PERMAFROST);
		ore("diamond", Blocks.ORE_DIAMOND_STONE, Blocks.ORE_DIAMOND_BASALT, Blocks.ORE_DIAMOND_LIMESTONE,
			Blocks.ORE_DIAMOND_GRANITE, Blocks.ORE_DIAMOND_PERMAFROST);
		ore("nethercoal", Blocks.ORE_NETHERCOAL_NETHERRACK, Blocks.ORE_NETHERCOAL_BASALT,
			Blocks.ORE_NETHERCOAL_GLOOMSTONE);

		if (!FabricLoader.getInstance().isModLoaded("aether")) return;

		aetherOre("aether_ambrosium", "ORE_AMBROSIUM_HOLYSTONE", "ORE_AMBROSIUM_COBBLE_HOLYSTONE");
		aetherOre("aether_zanite", "ORE_ZANITE_HOLYSTONE", "ORE_ZANITE_COBBLE_HOLYSTONE");
		aetherOre("aether_gravitite", "ORE_GRAVITITE_HOLYSTONE", "ORE_GRAVITITE_COBBLE_HOLYSTONE");

		for (String name : AETHER_LOGS) {
			if (field(AETHER_BLOCKS, name) instanceof Block<?> b) aetherBlocks.add(b);
		}

		try {
			for (Field f : Class.forName(AETHER_ITEMS).getFields()) {
				if (f.getName().startsWith("TOOL_") && f.get(null) instanceof Item item) {
					aetherTools.add(item);
					PotionsMod.LOGGER.info("Potions.Vines: Aether tool {}", f.getName());
				}
			}
		} catch (Exception e) {
			PotionsMod.LOGGER.warn("Potions.Vines: could not read Aether tools", e);
		}
		aetherUniversal(universalAxes, UNIVERSAL_AXES);
		aetherUniversal(universalPickaxes, UNIVERSAL_PICKAXES);

		if (FabricLoader.getInstance().isModLoaded("stardew")) {
			for (String name : STARDEW_LOGS) {
				if (field(STARDEW_BLOCKS, name) instanceof Block<?>) {
					PotionsMod.LOGGER.info("Potions.Vines: Stardew log {}", name);
				}
			}
		}
	}

	// AETHER AND VANILLA TOOLS ONLY WORK IN THEIR DIMENSIONS MINUS UNIVERSAL TOOLS
	private static boolean toolAllowed(ItemStack stack, boolean aetherBlock, boolean log) {
		Item tool = stack == null ? null : stack.getItem();
		if (tool != null && (log ? universalAxes : universalPickaxes).contains(tool)) return true;
		boolean aetherTool = tool != null && aetherTools.contains(tool);
		return aetherTool == aetherBlock;
	}

	public static boolean run(World world, Player player, Block<?> block, TilePosc origin) {
		if (world.isClientSide || player.isSneaking() || !ModEffects.has(player, ModEffects.VINES)) return false;
		if (!ready) init();

		boolean aetherBlock = aetherBlocks.contains(block);
		ItemStack held = player.getCurrentEquippedItem();

		if (block.getLogic() instanceof BlockLogicLog) {
			if (!toolAllowed(held, aetherBlock, true)) return false;
			if (aetherBlock && !player.canHarvestBlock(block)) return false;
			return new TreecapitatorHelper(world, origin.x(), origin.y(), origin.z(), player).chopTree();
		}

		String family = oreFamily.get(block);
		if (family == null) return false;
		if (!toolAllowed(held, aetherBlock, false)) return false;
		if (!player.canHarvestBlock(block)) return false;

		int max = ModEffects.scaleCount(VINES_MAX_BLOCKS, ModEffects.strength(player, ModEffects.VINES));
		breakOres(world, player, family, findVein(world, family, origin, max));
		return false;
	}

	private static long key(int x, int y, int z) {
		return ((long) x & 0x3FFFFFF) << 38 | ((long) z & 0x3FFFFFF) << 12 | (y & 0xFFF);
	}

	// BFS OVER 6 FACES
	private static List<TilePos> findVein(World world, String family, TilePosc origin, int max) {
		List<TilePos> found = new ArrayList<>();
		Set<Long> seen = new HashSet<>();
		ArrayDeque<TilePos> queue = new ArrayDeque<>();
		seen.add(key(origin.x(), origin.y(), origin.z()));
		queue.add(new TilePos(origin.x(), origin.y(), origin.z()));
		while (!queue.isEmpty() && found.size() < max) {
			TilePos p = queue.poll();
			for (int[] f : FACES) {
				int nx = p.x + f[0], ny = p.y + f[1], nz = p.z + f[2];
				if (!seen.add(key(nx, ny, nz))) continue;
				TilePos n = new TilePos(nx, ny, nz);
				if (!family.equals(oreFamily.get(world.getBlockType(n)))) continue;
				found.add(n);
				queue.add(n);
				if (found.size() >= max) break;
			}
		}
		return found;
	}

	private static void breakOres(World world, Player player, String family, List<TilePos> vein) {
		int i = 0;
		for (TilePos p : vein) {
			Block<?> b = world.getBlockType(p);
			if (!family.equals(oreFamily.get(b)) || !player.canHarvestBlock(b)) break;
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
	}
}
