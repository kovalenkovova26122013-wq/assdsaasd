package com.example.robloxhorns.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** Подключает экран настроек к кнопке "Настроить" в Mod Menu. */
public class HornsModMenuApi implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return HornsConfigScreen::new;
    }
}
