package andres451.potionsmod.mixin;

import andres451.potionsmod.ModBlocks;
import andres451.potionsmod.gui.guidebook.GuidebookSectionPotions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.guidebook.GuidebookSections;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = GuidebookSections.class, remap = false)
public abstract class GuidebookMixinPotions {
	@Inject(method = "init",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/guidebook/GuidebookSections;register(Lnet/minecraft/client/gui/guidebook/GuidebookSection;)Lnet/minecraft/client/gui/guidebook/GuidebookSection;",
			ordinal = 6, shift = At.Shift.AFTER))
	private static void potions$addSection(CallbackInfo ci) {
		GuidebookSections.register(new GuidebookSectionPotions(
			"potionsmod.guidebook.section.alchemy",
			new ItemStack(ModBlocks.alchemyTable),
			0x4A3A5A, 0xB080FF));
	}
}
