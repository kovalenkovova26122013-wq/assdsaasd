package com.example.robloxhorns.client;

import net.minecraft.util.Identifier;

/** Три вида рогов (как на картинках) + "нет". */
public enum HornStyle {
    NONE(null, FireStyle.NONE),
    TOXIC("horns_toxic", FireStyle.GREEN),
    FROST("horns_frost", FireStyle.BLUE),
    INFERNO("horns_inferno", FireStyle.ORANGE);

    public final Identifier texture;
    public final FireStyle defaultFire;

    HornStyle(String textureName, FireStyle defaultFire) {
        this.texture = textureName == null ? null
                : Identifier.of(RobloxHornsClient.MOD_ID, "textures/entity/" + textureName + ".png");
        this.defaultFire = defaultFire;
    }

    public String translationKey() {
        return "roblox_horns.horn." + name().toLowerCase();
    }
}
