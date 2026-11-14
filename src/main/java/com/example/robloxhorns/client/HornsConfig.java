package com.example.robloxhorns.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Настройки мода. Хранятся в .minecraft/config/roblox_horns.json */
public class HornsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static HornsConfig instance;

    public HornStyle horns = HornStyle.TOXIC;
    public FireStyle fire = FireStyle.AUTO;
    public int fireIntensity = 4;          // 1..10 частиц за тик
    public float hornScale = 1.0f;         // 0.5..1.5
    public boolean fireInFirstPerson = true;
    public boolean showOnOthers = false;   // рисовать твои рога на всех игроках

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("roblox_horns.json");
    }

    public static HornsConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        HornsConfig loaded = null;
        try {
            Path p = path();
            if (Files.exists(p)) {
                loaded = GSON.fromJson(Files.readString(p), HornsConfig.class);
            }
        } catch (Exception e) {
            RobloxHornsClient.LOGGER.warn("Could not read config, using defaults", e);
        }
        instance = loaded != null ? loaded : new HornsConfig();
        instance.sanitize();
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(get()));
        } catch (IOException e) {
            RobloxHornsClient.LOGGER.warn("Could not save config", e);
        }
    }

    private void sanitize() {
        if (horns == null) horns = HornStyle.TOXIC;
        if (fire == null) fire = FireStyle.AUTO;
        fireIntensity = Math.max(1, Math.min(10, fireIntensity));
        hornScale = Math.max(0.5f, Math.min(1.5f, hornScale));
    }

    /** Реальный вид огня с учётом AUTO. */
    public FireStyle effectiveFire() {
        return fire == FireStyle.AUTO ? horns.defaultFire : fire;
    }
}
