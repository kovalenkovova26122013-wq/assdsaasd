package com.example.robloxhorns.client;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Модель рогов. Координаты - относительно головы игрока (ось Y вниз, 16 пикселей = 1 блок).
 * Рог = цепочка кубиков, которая уходит в стороны и загибается вверх, плюс шипы.
 * Области UV в текстуре 64x64 совпадают со скриптом tools/gen_textures.py.
 */
public class HornsModel extends EntityModel<PlayerEntityRenderState> {
    public static final EntityModelLayer LAYER =
            new EntityModelLayer(Identifier.of(RobloxHornsClient.MOD_ID, "horns"), "main");

    public HornsModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartBuilder b = ModelPartBuilder.create();

        for (int side : new int[]{1, -1}) {
            // сегменты рога: u, v, x, y, z, w, h, d
            cube(b, side, 0, 0, 1.5f, -10f, -1.5f, 3, 3, 3);      // A - основание
            cube(b, side, 0, 8, 4.0f, -10.5f, -1.5f, 3, 3, 3);    // B
            cube(b, side, 0, 16, 6.5f, -12.5f, -1.5f, 3, 3, 3);   // C - изгиб наружу
            cube(b, side, 0, 24, 7.5f, -15.5f, -1.0f, 2, 3, 2);   // D - вверх
            cube(b, side, 0, 32, 8.0f, -17.5f, -0.5f, 1, 2, 1);   // E - кончик

            // шипы 1x1x1 (одна и та же область текстуры)
            cube(b, side, 32, 0, 2.5f, -11.0f, -0.5f, 1, 1, 1);
            cube(b, side, 32, 0, 5.0f, -11.5f, -0.5f, 1, 1, 1);
            cube(b, side, 32, 0, 7.0f, -13.5f, -0.5f, 1, 1, 1);
            cube(b, side, 32, 0, 9.5f, -11.5f, -0.5f, 1, 1, 1);
            cube(b, side, 32, 0, 9.5f, -14.5f, -0.5f, 1, 1, 1);
            cube(b, side, 32, 0, 5.0f, -9.0f, -2.5f, 1, 1, 1);
            cube(b, side, 32, 0, 5.0f, -9.0f, 1.5f, 1, 1, 1);
            cube(b, side, 32, 0, 7.5f, -11.0f, -2.5f, 1, 1, 1);
            cube(b, side, 32, 0, 7.5f, -11.0f, 1.5f, 1, 1, 1);
            cube(b, side, 32, 0, 8.0f, -14.0f, -2.0f, 1, 1, 1);
            cube(b, side, 32, 0, 8.0f, -14.0f, 1.0f, 1, 1, 1);
        }

        root.addChild("horns", b, ModelTransform.NONE);
        return TexturedModelData.of(data, 64, 64);
    }

    /** side = 1 (правый рог) или -1 (левый, зеркально по X). */
    private static void cube(ModelPartBuilder b, int side, int u, int v,
                             float x, float y, float z, int w, int h, int d) {
        float px = side > 0 ? x : -(x + w);
        b.uv(u, v).cuboid(px, y, z, w, h, d);
    }
}
