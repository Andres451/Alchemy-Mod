package andres451.potionsmod.gui;

import andres451.potionsmod.TileEntityAlchemyTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenAlchemyTable extends ScreenContainerAbstract {
	private final TileEntityAlchemyTable table;

	public ScreenAlchemyTable(ContainerInventory inventory, TileEntityAlchemyTable table) {
		super(new MenuAlchemyTable(inventory, table));
		this.table = table;
	}

	// FLAME: POSITION XY, POSITION PROGRESS UV, SIZE WH
	private static final int FLAME_X = 136, FLAME_Y = 38, FLAME_U = 176, FLAME_V = 0, FLAME_W = 14, FLAME_H = 14;
	// PROGRESS: POSITION XY, POSITION PROGRESS UV, SIZE WH
	private static final int ARROW_X = 62, ARROW_Y = 34, ARROW_U = 176, ARROW_V = 15, ARROW_W = 16, ARROW_H = 19;
	private static final boolean ARROW_HORIZONTAL = false;

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTick) {
		this.mc.textureManager.loadTexture("/assets/potionsmod/textures/gui/container/alchemy_table.png").bind();
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (table.isBurning()) {
			int h = table.getBurnRemainingScaled(FLAME_H);
			this.drawTexturedModalRect(x + FLAME_X, y + FLAME_Y + FLAME_H - h, FLAME_U, FLAME_V + FLAME_H - h, FLAME_W, h);
		}
		if (ARROW_HORIZONTAL) {
			int p = table.getProgressScaled(ARROW_W);
			if (p > 0) this.drawTexturedModalRect(x + ARROW_X, y + ARROW_Y, ARROW_U, ARROW_V, p, ARROW_H);
		} else {
			int p = table.getProgressScaled(ARROW_H);
			if (p > 0) this.drawTexturedModalRect(x + ARROW_X, y + ARROW_Y, ARROW_U, ARROW_V, ARROW_W, p);
		}
	}

	@Override
	public void drawGuiContainerForegroundLayer() {
		I18n i18n = I18n.getInstance();
		this.drawStringNoShadow(this.fontRenderer, i18n.translateKey("potionsmod.gui.alchemy_table.title"), 8, 6, 0xFF404040);
		this.drawStringNoShadow(this.fontRenderer, i18n.translateKey("gui.furnace.label.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
}
