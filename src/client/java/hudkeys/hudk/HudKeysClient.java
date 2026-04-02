package hudkeys.hudk;

import hudkeys.hudk.config.HudConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public class HudKeysClient implements ClientModInitializer {

	private int currentTick = 0;
	private int lastAttackTick = -10;
	private int lastSwapTick = -10;
	private int previousSlot = -1;
	private boolean wasAttackPressed = false;
	private final int[] greenFlashTicks = new int[9];

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			renderHud(drawContext);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			currentTick++;

			boolean isAttackPressed = client.options.attackKey.isPressed();
			if (isAttackPressed && !wasAttackPressed) {
				lastAttackTick = currentTick;
				if (currentTick - lastSwapTick <= 1) {
					greenFlashTicks[client.player.getInventory().getSelectedSlot()] = 15;
				}
			}
			wasAttackPressed = isAttackPressed;

			int currentSlot = client.player.getInventory().getSelectedSlot();
			if (currentSlot != previousSlot) {
				if (previousSlot != -1) {
					lastSwapTick = currentTick;
					if (currentTick - lastAttackTick <= 1) {
						greenFlashTicks[currentSlot] = 15;
					}
				}
				previousSlot = currentSlot;
			}

			for (int i = 0; i < 9; i++) {
				if (greenFlashTicks[i] > 0) greenFlashTicks[i]--;
			}
		});
	}

	private void renderHud(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		if(client.player == null || client.options.hudHidden) return;

		int width = client.getWindow().getScaledWidth();
		int height = client.getWindow().getScaledHeight();

		HudConfig config = HudConfig.getInstance();

		int baseStartX = (width / 2) - 90;
		int baseY = height - 22 + 1;

		int startX = baseStartX + config.xOffset;
		int y = baseY + config.yOffset;

		for (int i = 0; i < 9; i++) {
			KeyBinding key = client.options.hotbarKeys[i];
			int x = startX + (i * 20);
			renderKey(context, key, x, y, i);
		}
	}

	private void renderKey(DrawContext context, KeyBinding key, int x, int y, int slotIndex) {
		boolean isPressed = key.isPressed();
		String label = key.getBoundKeyLocalizedText().getString();

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

		float scale = HudConfig.getInstance().scale;

		context.getMatrices().pushMatrix();
		context.getMatrices().translate(x + (boxSize / 2f), y + (boxSize / 2f) + 1);
		context.getMatrices().scale(scale, scale);

		renderOutlinedText(context, label, 0, -4);

		context.getMatrices().popMatrix();
	}

	private void renderRoundedRect(DrawContext context, int x, int y, int width, int height, int color) {
		context.fill(x + 1, y, x + width - 1, y + height, color);
		context.fill(x + 1, y, x + width - 1, y + height, color);
		context.fill(x, y + 1, x + 1, y + height - 1, color);
		context.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
	}

	private void renderOutlinedText(DrawContext context, String text, int x, int y) {
		MinecraftClient client = MinecraftClient.getInstance();
		int outlineColor = 0xFF000000;
		int textColor = 0xFFFFFFFF;

		context.drawCenteredTextWithShadow(client.textRenderer, text, x - 1, y, outlineColor);
		context.drawCenteredTextWithShadow(client.textRenderer, text, x + 1, y, outlineColor);
		context.drawCenteredTextWithShadow(client.textRenderer, text, x, y - 1, outlineColor);
		context.drawCenteredTextWithShadow(client.textRenderer, text, x, y + 1, outlineColor);

		context.drawCenteredTextWithShadow(client.textRenderer, text, x, y, textColor);
	}
}