package hudkeys.hudk;

import hudkeys.hudk.config.HudConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public class HudKeysClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			renderHud(drawContext);
		});
	}

	private void renderHud(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		if(client.player == null || client.options.hudHidden) return;

		int width = client.getWindow().getScaledWidth();
		int height = client.getWindow().getScaledHeight();

		// 1. Establish base position (centered over hotbar)
		int baseStartX = (width / 2) - 90;
		int baseY = height - 22 + 1;

		// 2. Add the user's config offsets
		int startX = baseStartX + HudConfig.xOffset;
		int y = baseY + HudConfig.yOffset;

		for (int i = 0; i < 9; i++) {
			KeyBinding key = client.options.hotbarKeys[i];
			int x = startX + (i * 20);
			renderKey(context, key, x, y);
		}
	}

	private void renderKey(DrawContext context, KeyBinding key, int x, int y) {
		boolean isPressed = key.isPressed();
		String label = key.getBoundKeyLocalizedText().getString();

		// background color light gray/dark gray
		int backgroundColor = isPressed ? 0x10FFFFFF : 0x10808080;

		// text color
		int textColor = 0xFFFFFFFF;
		int borderColor = 0xAA000000;

		int boxSize = 9;

		renderRoundedRect(context, x - 1, y - 1, boxSize + 2, boxSize + 2, borderColor);
		renderRoundedRect(context, x, y, boxSize, boxSize, backgroundColor);

		// scale
		float scale = HudConfig.scale;

		// save curr state
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