package com.example.robloxhorns.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

/** Слой рендера: рисует рога на голове игрока. */
public class HornsFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final HornsModel model;

    public HornsFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
                                EntityRendererFactory.Context factoryContext) {
        super(context);
        this.model = new HornsModel(factoryContext.getEntityModels().getModelPart(HornsModel.LAYER));
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        HornsConfig cfg = HornsConfig.get();
        if (cfg.horns == HornStyle.NONE || state.invisible) {
            return;
        }
        if (!cfg.showOnOthers && !isLocalPlayer(state)) {
            return;
        }

        matrices.push();
        // Привязываем к голове (учитывает наклон/поворот головы, приседание и т.д.)
        this.getContextModel().getHead().applyTransform(matrices);
        // Масштаб относительно верха головы, чтобы рога не отрывались от неё
        float s = cfg.hornScale;
        matrices.translate(0.0f, -0.5f, 0.0f);
        matrices.scale(s, s, s);
        matrices.translate(0.0f, 0.5f, 0.0f);

        queue.submitModel(this.model, state, matrices,
                this.model.getLayer(cfg.horns.texture),
                light, OverlayTexture.DEFAULT_UV, 0, null);
        matrices.pop();
    }

    /** Рендер-стейт не знает, чей он, поэтому сравниваем позицию с позицией локального игрока. */
    private static boolean isLocalPlayer(PlayerEntityRenderState state) {
        ClientPlayerEntity me = MinecraftClient.getInstance().player;
        if (me == null) {
            return false;
        }
        double dx = state.x - me.getX();
        double dy = state.y - me.getY();
        double dz = state.z - me.getZ();
        return dx * dx + dy * dy + dz * dz < 1.0;
    }
}
