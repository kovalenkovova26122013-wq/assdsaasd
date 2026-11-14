package com.example.robloxhorns.client;

/** Вид огня над рогами. AUTO = подбирается под выбранные рога. */
public enum FireStyle {
    AUTO, NONE, ORANGE, BLUE, GREEN, PURPLE, WHITE, RAINBOW;

    public String translationKey() {
        return "roblox_horns.fire." + name().toLowerCase();
    }
}
