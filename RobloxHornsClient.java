package com.example.robloxhorns.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobloxHornsClient implements ClientModInitializer {
    public static final String MOD_ID = "roblox_horns";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void onInitializeClient() {
        HornsConfig.load();

        EntityModelLayerRegistry.registerModelLayer(HornsModel.LAYER, HornsModel::getTexturedModelData);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityType == EntityType.PLAYER) {
                        registrationHelper.register(new HornsFeatureRenderer(
                                (FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>)
                                        (FeatureRendererContext) entityRenderer,
                                context));
                    }
                });

        ClientTickEvents.END_CLIENT_TICK.register(HornsFire::tick);
        LOGGER.info("Roblox Horns loaded");
    }
}
