package com.example.robloxhorns.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

/** Экран настроек, открывается из Mod Menu. */
public class HornsConfigScreen extends Screen {
    private final Screen parent;

    public HornsConfigScreen(Screen parent) {
        super(Text.translatable("roblox_horns.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        HornsConfig cfg = HornsConfig.get();
        int w = 240;
        int x = this.width / 2 - w / 2;
        int y = Math.max(40, this.height / 6);
        int step = 24;

        this.addDrawableChild(new TextWidget(x, y - 24, w, 14, this.title, this.textRenderer).alignCenter());

        this.addDrawableChild(CyclingButtonWidget
                .builder((HornStyle v) -> Text.translatable(v.translationKey()), cfg.horns)
                .values(HornStyle.values())
                .build(x, y, w, 20, Text.translatable("roblox_horns.config.horns"),
                        (btn, v) -> cfg.horns = v));
        y += step;

        this.addDrawableChild(CyclingButtonWidget
                .builder((FireStyle v) -> Text.translatable(v.translationKey()), cfg.fire)
                .values(FireStyle.values())
                .build(x, y, w, 20, Text.translatable("roblox_horns.config.fire"),
                        (btn, v) -> cfg.fire = v));
        y += step;

        this.addDrawableChild(new ValueSlider(x, y, w, 20, "roblox_horns.config.intensity",
                1, 10, 1, cfg.fireIntensity, v -> cfg.fireIntensity = Math.round(v)));
        y += step;

        this.addDrawableChild(new ValueSlider(x, y, w, 20, "roblox_horns.config.scale",
                0.5f, 1.5f, 0.1f, cfg.hornScale, v -> cfg.hornScale = v));
        y += step;

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(cfg.fireInFirstPerson)
                .build(x, y, w, 20, Text.translatable("roblox_horns.config.first_person"),
                        (btn, v) -> cfg.fireInFirstPerson = v));
        y += step;

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(cfg.showOnOthers)
                .build(x, y, w, 20, Text.translatable("roblox_horns.config.others"),
                        (btn, v) -> cfg.showOnOthers = v));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, b -> this.close())
                .dimensions(x, this.height - 28, w, 20).build());
    }

    @Override
    public void close() {
        HornsConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    /** Простой слайдер со значением и шагом. */
    private static class ValueSlider extends SliderWidget {
        private final String key;
        private final float min;
        private final float max;
        private final float step;
        private final java.util.function.Consumer<Float> onChange;

        ValueSlider(int x, int y, int width, int height, String key, float min, float max, float step,
                    float current, java.util.function.Consumer<Float> onChange) {
            super(x, y, width, height, Text.empty(), (current - min) / (max - min));
            this.key = key;
            this.min = min;
            this.max = max;
            this.step = step;
            this.onChange = onChange;
            this.updateMessage();
        }

        private float currentValue() {
            float raw = this.min + (float) this.value * (this.max - this.min);
            return Math.round(raw / this.step) * this.step;
        }

        @Override
        protected void updateMessage() {
            float v = currentValue();
            String shown = this.step >= 1 ? String.valueOf(Math.round(v)) : String.format("%.1f", v);
            this.setMessage(Text.translatable(this.key).append(": " + shown));
        }

        @Override
        protected void applyValue() {
            this.onChange.accept(currentValue());
        }
    }
}
