package andres451.potionsmod.mixin;

import andres451.potionsmod.TileEntityAlchemyTable;
import andres451.potionsmod.gui.PotionsScreens;
import andres451.potionsmod.gui.ScreenAlchemyTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(PlayerLocal.class)
public abstract class PlayerLocalMixinPotionsScreens extends Player implements PotionsScreens {
	protected PlayerLocalMixinPotionsScreens(World world) {
		super(world);
	}

	@Shadow
	protected Minecraft mc;

	@Override
	public void potions$displayAlchemyTableScreen(TileEntityAlchemyTable tileEntity) {
		this.mc.displayScreen(new ScreenAlchemyTable(this.inventory, tileEntity));
	}
}
