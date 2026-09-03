package hudkeys.hudk.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import hudkeys.hudk.config.HudConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Hud Keys Config"));

            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // X Offset
            general.addEntry(entryBuilder.startIntField(Component.literal("X Offset"), HudConfig.getInstance().xOffset)
                    .setDefaultValue(0)
                    .setTooltip(Component.literal("0 is default. Negative moves left, Positive moves right."))
                    .setSaveConsumer(newValue -> HudConfig.getInstance().xOffset = newValue)
                    .build());

            // Y Offset
            general.addEntry(entryBuilder.startIntField(Component.literal("Y Offset"), HudConfig.getInstance().yOffset)
                    .setDefaultValue(0)
                    .setTooltip(Component.literal("0 is default. Negative moves up, Positive moves down."))
                    .setSaveConsumer(newValue -> HudConfig.getInstance().yOffset = newValue)
                    .build());

            // Scale
            general.addEntry(entryBuilder.startFloatField(Component.literal("Scale"), HudConfig.getInstance().scale)
                    .setDefaultValue(0.6f)
                    .setMin(0.1f)
                    .setMax(3.0f)
                    .setTooltip(Component.literal("Key box display scale. Default is 0.6."))
                    .setSaveConsumer(newValue -> HudConfig.getInstance().scale = newValue)
                    .build());

            // Show Unpressed Keys
            general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Unpressed Keys"), HudConfig.getInstance().showUnpressedKeys)
                    .setDefaultValue(true)
                    .setTooltip(Component.literal("Whether to show key boxes even when the key is not pressed."))
                    .setSaveConsumer(newValue -> HudConfig.getInstance().showUnpressedKeys = newValue)
                    .build());

            builder.setSavingRunnable(HudConfig::save);

            return builder.build();
        };
    }
}