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
 * Модель рогов (ГЕНЕРИРУЕТСЯ скриптом tools/gen_horns.py - правь скрипт, а не этот файл).
 * Координаты относительно головы игрока (ось Y вниз, 16 пикселей = 1 блок).
 * Рог: цепочка сегментов, металлические кольца и шипы. Текстура 64x64.
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
            cube(b, side, 0, 0, 1.0f, -10.5f, -2.0f, 4, 3, 4);   // seg0
            cube(b, side, 16, 0, 5.0f, -11.5f, -1.5f, 3, 3, 3);   // seg1
            cube(b, side, 28, 0, 7.0f, -14.5f, -1.5f, 3, 3, 3);   // seg2
            cube(b, side, 40, 0, 8.0f, -16.5f, -1.0f, 2, 2, 2);   // seg3
            cube(b, side, 48, 0, 8.0f, -18.5f, -1.0f, 2, 2, 2);   // seg4
            cube(b, side, 56, 0, 8.5f, -20.5f, -0.5f, 1, 2, 1);   // seg5
            cube(b, side, 0, 7, 0.5f, -9.5f, -2.5f, 5, 1, 5);   // c5
            cube(b, side, 20, 7, 4.0f, -10.5f, -2.0f, 5, 1, 4);   // c54
            cube(b, side, 0, 7, 6.0f, -13.5f, -2.5f, 5, 1, 5);   // c5
            cube(b, side, 38, 7, 7.0f, -16.0f, -2.0f, 4, 1, 4);   // c4
            cube(b, side, 58, 7, 2.0f, -12.5f, -0.5f, 1, 2, 1);   // st
            cube(b, side, 58, 7, 5.0f, -13.5f, -0.5f, 1, 2, 1);   // st
            cube(b, side, 54, 7, 10.0f, -14.5f, -0.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 10.0f, -18.0f, -0.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 2.5f, -10.5f, -3.0f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 2.5f, -10.5f, 2.0f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 6.0f, -11.5f, -2.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 6.0f, -11.5f, 1.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 8.0f, -14.5f, -2.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 8.0f, -14.5f, 1.5f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 8.5f, -18.0f, -2.0f, 1, 1, 1);   // ss
            cube(b, side, 54, 7, 8.5f, -18.0f, 1.0f, 1, 1, 1);   // ss
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
