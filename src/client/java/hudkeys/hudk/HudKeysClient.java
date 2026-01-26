package hudkeys.hudk;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.BlockLeakParticle;


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
		String originalLabel = key.getBoundKeyLocalizedText().getString();
		String label = getShortLabel(originalLabel);
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

	private String getShortLabel(String label) {
		if (label.contains("Shift")) return label.contains("Right") ? "RS" : "LS";
		if (label.contains("Control")) return label.contains("Right") ? "RC" : "LC";
		if (label.contains("Alt")) return label.contains("Right") ? "RA" : "LA";
		if (label.equalsIgnoreCase("Space")) return "Sp";
		if (label.equalsIgnoreCase("Tab")) return "Tb";
		if (label.equalsIgnoreCase("Enter")) return "En";
		if (label.equalsIgnoreCase("Backspace")) return "Bk";
		if (label.equalsIgnoreCase("Caps Lock")) return "CL";

		if (label.contains("Mouse")) {
			String number = label.replaceAll("[^0-9]", ""); // Κρατάμε μόνο τους αριθμούς
			if (!number.isEmpty()) return "M" + number;
		}

		if (label.contains(" ")) {
			String[] parts = label.split(" ");
			StringBuilder result = new StringBuilder();
			for (String part : parts) {
				if (!part.isEmpty()) result.append(part.charAt(0));
			}
			if (result.length() > 2) result = new StringBuilder(result.substring(0, 2));
			return result.toString().toUpperCase();
		}

		if (label.length() > 2) {
			return label.substring(0, 2);
		}

		return label;
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