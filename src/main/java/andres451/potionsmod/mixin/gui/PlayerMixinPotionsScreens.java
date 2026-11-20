package andres451.potionsmod.mixin.gui;

import andres451.potionsmod.alchemy_table.TileEntityAlchemyTable;
import andres451.potionsmod.alchemy_table.gui.PotionsScreens;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixinPotionsScreens implements PotionsScreens {
	@Override
	public void potions$displayAlchemyTableScreen(TileEntityAlchemyTable tileEntity) {}
}
