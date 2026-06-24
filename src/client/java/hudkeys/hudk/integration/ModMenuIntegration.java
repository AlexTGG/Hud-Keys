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

            builder.setSavingRunnable(() -> {
            });

            return builder.build();
        };
    }
}