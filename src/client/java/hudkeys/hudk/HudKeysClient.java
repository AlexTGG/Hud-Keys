package hudkeys.hudk;

import hudkeys.hudk.config.HudConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
//? if <=1.21.11 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?} else {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class HudKeysClient implements ClientModInitializer {

	private int currentTick = 0;
	private int lastAttackTick = -10;
	private int lastSwapTick = -10;
	private int previousSlot = -1;
	private boolean wasAttackPressed = false;
	private final int[] greenFlashTicks = new int[9];

	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementAfter(
				VanillaHudElements.HOTBAR,
				Identifier.fromNamespaceAndPath("hudk", "key_overlay"),
				(graphics, deltaTracker) -> renderHud(graphics)
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null)
				return;
			currentTick++;

			boolean isAttackPressed = client.options.keyAttack.isDown();
			if (isAttackPressed && !wasAttackPressed) {
				lastAttackTick = currentTick;
				if (currentTick - lastSwapTick <= 1) {
					int selectedSlot = client.player.getInventory().getSelectedSlot();
					if (selectedSlot >= 0 && selectedSlot < 9) {
						greenFlashTicks[selectedSlot] = 15;
					}
				}
			}
			wasAttackPressed = isAttackPressed;

			int currentSlot = client.player.getInventory().getSelectedSlot();
			if (currentSlot != previousSlot) {
				if (previousSlot != -1) {
					lastSwapTick = currentTick;
					if (currentTick - lastAttackTick <= 1) {
						if (currentSlot >= 0 && currentSlot < 9) {
							greenFlashTicks[currentSlot] = 15;
						}
					}
				}
				previousSlot = currentSlot;
			}

			for (int i = 0; i < 9; i++) {
				if (greenFlashTicks[i] > 0)
					greenFlashTicks[i]--;
			}
		});
	}

	//? if <=1.21.11 {
	/*private void renderHud(GuiGraphics context) {
	*///?} else {
	private void renderHud(GuiGraphicsExtractor context) {
	//?}
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui)
			return;

		int width = client.getWindow().getGuiScaledWidth();
		int height = client.getWindow().getGuiScaledHeight();

		HudConfig config = HudConfig.getInstance();

		int baseStartX = (width / 2) - 90;
		int baseY = height - 22 + 1;

		int startX = baseStartX + config.xOffset;
		int y = baseY + config.yOffset;

		for (int i = 0; i < 9; i++) {
			KeyMapping key = client.options.keyHotbarSlots[i];
			int x = startX + (i * 20);
			renderKey(context, key, x, y, i);
		}
	}

	//? if <=1.21.11 {
	/*private void renderKey(GuiGraphics context, KeyMapping key, int x, int y, int slotIndex) {
	*///?} else {
	private void renderKey(GuiGraphicsExtractor context, KeyMapping key, int x, int y, int slotIndex) {
	//?}
		boolean isPressed = key.isDown();
		HudConfig config = HudConfig.getInstance();
		if (!config.showUnpressedKeys && !isPressed && greenFlashTicks[slotIndex] <= 0) {
			return;
		}

		String label = key.getTranslatedKeyMessage().getString();

		int backgroundColor;
		if (greenFlashTicks[slotIndex] > 0) {
			backgroundColor = 0x8055FF55;
		} else {
			backgroundColor = isPressed ? 0x10FFFFFF : 0x10808080;
		}

		int borderColor = 0xAA000000;
		int boxSize = 9;

		renderRoundedRect(context, x - 1, y - 1, boxSize + 2, boxSize + 2, borderColor);
		renderRoundedRect(context, x, y, boxSize, boxSize, backgroundColor);

		float scale = config.scale;

		context.pose().pushMatrix();
		context.pose().translate(x + (boxSize / 2f), y + (boxSize / 2f) + 1);
		context.pose().scale(scale, scale);

		renderOutlinedText(context, label, 0, -4);

		context.pose().popMatrix();
	}

	//? if <=1.21.11 {
	/*private void renderRoundedRect(GuiGraphics context, int x, int y, int width, int height, int color) {
	*///?} else {
	private void renderRoundedRect(GuiGraphicsExtractor context, int x, int y, int width, int height, int color) {
	//?}
		context.fill(x + 1, y, x + width - 1, y + height, color);
		context.fill(x, y + 1, x + 1, y + height - 1, color);
		context.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
	}

	//? if <=1.21.11 {
	/*private void renderOutlinedText(GuiGraphics context, String text, int x, int y) {
	*///?} else {
	private void renderOutlinedText(GuiGraphicsExtractor context, String text, int x, int y) {
	//?}
		Minecraft client = Minecraft.getInstance();
		int outlineColor = 0xFF000000;
		int textColor = 0xFFFFFFFF;

		//? if <=1.21.11 {
		/*context.drawCenteredString(client.font, text, x - 1, y, outlineColor);
		context.drawCenteredString(client.font, text, x + 1, y, outlineColor);
		context.drawCenteredString(client.font, text, x, y - 1, outlineColor);
		context.drawCenteredString(client.font, text, x, y + 1, outlineColor);
		
		context.drawCenteredString(client.font, text, x, y, textColor);
		*///?} else {
		context.centeredText(client.font, text, x - 1, y, outlineColor);
		context.centeredText(client.font, text, x + 1, y, outlineColor);
		context.centeredText(client.font, text, x, y - 1, outlineColor);
		context.centeredText(client.font, text, x, y + 1, outlineColor);

		context.centeredText(client.font, text, x, y, textColor);
		//?}
	}
}