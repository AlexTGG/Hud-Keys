package hudkeys.hudk.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import hudkeys.hudk.config.HudConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent ->{
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Hud Keys Config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // X Offset
            general.addEntry(entryBuilder.startIntField(Text.literal("X Offset"), HudConfig.xOffset)
                    .setDefaultValue(0)
                    .setTooltip(Text.literal("0 is default. Negative moves left, Positive moves right."))
                    .setSaveConsumer(newValue -> HudConfig.xOffset = newValue)
                    .build());

            // Y Offset
            general.addEntry(entryBuilder.startIntField(Text.literal("Y Offset"), HudConfig.yOffset)
                    .setDefaultValue(0)
                    .setTooltip(Text.literal("0 is default. Negative moves up, Positive moves down."))
                    .setSaveConsumer(newValue -> HudConfig.yOffset = newValue)
                    .build());

            builder.setSavingRunnable(() -> {
            });

            return builder.build();
        };
    }
}