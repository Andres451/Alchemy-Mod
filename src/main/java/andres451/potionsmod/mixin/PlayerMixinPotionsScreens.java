package andres451.potionsmod.mixin;

import andres451.potionsmod.TileEntityAlchemyTable;
import andres451.potionsmod.gui.PotionsScreens;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixinPotionsScreens implements PotionsScreens {
	@Override
	public void potions$displayAlchemyTableScreen(TileEntityAlchemyTable tileEntity) {}
}
