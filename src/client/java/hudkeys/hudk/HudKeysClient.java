package hudkeys.hudk;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.BlockLeakParticle;

import javax.swing.text.JTextComponent;

public class HudKeysClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			renderHud(drawContext);
		});
	}

	private void renderHud(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		if(client.player == null || client.options.hudHidden) return;

		int width = client.getWindow().getScaledWidth();
		int height = client.getWindow().getScaledHeight();

		//set start pos
		int startX = (width / 2) - 90;

		int y = height - 22+1;

		//loop slots get keybinds
		for (int i= 0; i<9; i++){
			//get keybinds
			KeyBinding key = client.options.hotbarKeys[i];

			int x = startX +(i*20);

			renderKey(context, key, x, y);
		}
	}
	private void renderKey(DrawContext context, KeyBinding key, int x, int y) {
		boolean isPressed = key.isPressed();
		String label = key.getBoundKeyLocalizedText().getString();

		//background color light gray/dark gray
		int backgroundColor = isPressed ? 0x30404040 : 0x30808080;

		//text color
		int textColor = 0xFFFFFFFF;
		int borderColor = 0xAA000000;

		int boxSize = 9;

		renderRoundedRect(context, x - 1, y - 1, boxSize + 2, boxSize + 2, borderColor);
		renderRoundedRect(context, x, y, boxSize, boxSize, backgroundColor);

		//scale
		float scale = 0.6f;
		float pressOffset = isPressed ? 0.5f : 0f;

		//save curr state
		context.getMatrices().pushMatrix();

		context.getMatrices().translate(x + (boxSize / 2f), y + (boxSize / 2f) + 1);
		context.getMatrices().scale(scale, scale);


		renderOutlinedText(context, label, 0, -4);

		context.getMatrices().popMatrix();
	}

	private void renderRoundedRect(DrawContext context, int x, int y, int width, int height, int color){
		context.fill(x+1, y, x+width-1,y+height,color);

		context.fill(x+1, y, x+width-1, y+height,color);
		context.fill(x,y+1,x+1,y+height - 1,color);
		context.fill(x+width-1, y + 1, x + width, y+height - 1,color);
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
	}}