"""
Генератор моделей и текстур рогов (запускать из корня проекта: python3 tools/gen_horns.py).
Пишет: HornsModel.java и три текстуры 64x64 (horns_toxic / horns_frost / horns_inferno).
Одна таблица кубиков -> и Java-код, и раскладка UV, поэтому они всегда совпадают.
"""
import random
from PIL import Image

random.seed(11)
ROOT = "src/main/resources/assets/roblox_horns/textures/entity/"
JAVA = "src/main/java/com/example/robloxhorns/client/HornsModel.java"

# ---- размеры областей (kind -> w,h,d) ----
KINDS = {
    "seg0": (4, 3, 4), "seg1": (3, 3, 3), "seg2": (3, 3, 3),
    "seg3": (2, 2, 2), "seg4": (2, 2, 2), "seg5": (1, 2, 1),
    "c5": (5, 1, 5), "c54": (5, 1, 4), "c4": (4, 1, 4),
    "ss": (1, 1, 1), "st": (1, 2, 1),
}

# ---- кубики правого рога: kind, x, y, z (левый угол), модель считает размеры из KINDS ----
# Цепочка сегментов касается друг друга гранями (нет мерцания), рог идёт наружу и загибается вверх.
CUBES = [
    ("seg0", 1.0, -10.5, -2.0),
    ("seg1", 5.0, -11.5, -1.5),
    ("seg2", 7.0, -14.5, -1.5),
    ("seg3", 8.0, -16.5, -1.0),
    ("seg4", 8.0, -18.5, -1.0),
    ("seg5", 8.5, -20.5, -0.5),
    # металлические кольца
    ("c5", 0.5, -9.5, -2.5),
    ("c54", 4.0, -10.5, -2.0),
    ("c5", 6.0, -13.5, -2.5),
    ("c4", 7.0, -16.0, -2.0),
    # шипы вверх
    ("st", 2.0, -12.5, -0.5),
    ("st", 5.0, -13.5, -0.5),
    # шипы наружу
    ("ss", 10.0, -14.5, -0.5),
    ("ss", 10.0, -18.0, -0.5),
    # шипы вперёд / назад
    ("ss", 2.5, -10.5, -3.0), ("ss", 2.5, -10.5, 2.0),
    ("ss", 6.0, -11.5, -2.5), ("ss", 6.0, -11.5, 1.5),
    ("ss", 8.0, -14.5, -2.5), ("ss", 8.0, -14.5, 1.5),
    ("ss", 8.5, -18.0, -2.0), ("ss", 8.5, -18.0, 1.0),
]

# ---- раскладка областей в атласе 64x64 ----
def pack():
    pos, x, y, rowh = {}, 0, 0, 0
    for k, (w, h, d) in KINDS.items():
        rw, rh = 2 * d + 2 * w, d + h
        if x + rw > 64:
            x, y, rowh = 0, y + rowh, 0
        pos[k] = (x, y)
        x += rw
        rowh = max(rowh, rh)
    assert y + rowh <= 64
    return pos

POS = pack()

# ---- Java ----
def f(v):
    return ("%s" % v).rstrip("0").rstrip(".") + "f" if v != int(v) else "%d.0f" % int(v)

lines = []
for k, x, y, z in CUBES:
    w, h, d = KINDS[k]
    u, v = POS[k]
    lines.append("            cube(b, side, %d, %d, %s, %s, %s, %d, %d, %d);   // %s" % (u, v, f(x), f(y), f(z), w, h, d, k))

java = '''package com.example.robloxhorns.client;

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
''' + "\n".join(lines) + '''
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
'''
open(JAVA, "w", encoding="utf-8").write(java)

# ---- текстуры ----
def clamp(c):
    return tuple(max(0, min(255, int(v))) for v in c)

def mix(a, b, t):
    t = max(0.0, min(1.0, t))
    return tuple(a[i] * (1 - t) + b[i] * t for i in range(3))

def mul(c, m):
    return tuple(v * m for v in c)

STYLES = {
    "toxic": dict(metal=(30, 32, 34), low=(70, 200, 60), high=(150, 60, 230), glow=(205, 130, 255),
                  spike=(130, 45, 215), spike_tip=(220, 160, 255)),
    "frost": dict(metal=(34, 38, 46), low=(45, 110, 210), high=(70, 170, 255), glow=(180, 245, 255),
                  spike=(55, 140, 235), spike_tip=(200, 245, 255)),
    "inferno": dict(metal=(40, 36, 36), low=(140, 25, 20), high=(235, 95, 25), glow=(255, 215, 100),
                    spike=(175, 30, 25), spike_tip=(255, 190, 70)),
}
SEG_W = {"seg0": 0.08, "seg1": 0.16, "seg2": 0.30, "seg3": 0.50, "seg4": 0.75, "seg5": 1.0}
FACE_L = {"top": 1.25, "front": 1.0, "left": 0.82, "right": 0.82, "back": 0.72, "bottom": 0.5}

def faces(u, v, w, h, d):
    return {
        "top": (u + d, v, w, d), "bottom": (u + d + w, v, w, d),
        "left": (u, v + d, d, h), "front": (u + d, v + d, w, h),
        "right": (u + d + w, v + d, d, h), "back": (u + 2 * d + w, v + d, w, h),
    }

def paint(img, kind, st):
    w, h, d = KINDS[kind]
    u, v = POS[kind]
    px = img.load()
    for name, (fx, fy, fw, fh) in faces(u, v, w, h, d).items():
        for yy in range(fh):
            for xx in range(fw):
                t = yy / max(1, fh - 1) if name not in ("top", "bottom") else 0.5
                n = random.uniform(-6, 6)
                if kind in SEG_W:
                    wgt = SEG_W[kind] + 0.18 * (1 - t) * (name not in ("top", "bottom"))
                    tint = mix(st["low"], st["high"], SEG_W[kind] * 1.3)
                    if kind in ("seg4", "seg5"):
                        tint = mix(st["high"], st["glow"], 0.5 if kind == "seg4" else 0.9)
                    c = mix(st["metal"], tint, wgt)
                    if name not in ("top", "bottom"):
                        r = yy % 3                      # рёбра как у рога в Roblox
                        c = mul(c, 1.18 if r == 0 else (0.82 if r == 2 else 1.0))
                elif kind.startswith("c"):              # кольца: светлый металл
                    c = mix((74, 76, 84), st["low"], 0.10)
                    if name in ("top", "bottom"):
                        c = mul(c, 0.9)
                    elif yy == 0:
                        c = mul(c, 1.25)
                else:                                   # шипы: от основания к острию
                    c = mix(st["spike"], st["spike_tip"], 0.2 + 0.8 * (1 - t) if name not in ("top", "bottom") else 1.0)
                c = mul(c, FACE_L[name])
                c = tuple(v_ + n for v_ in c)
                if random.random() < 0.04 and kind in SEG_W:   # царапины
                    c = mul(c, 0.6)
                if name == "front" and xx == 0 and yy == 0:    # блик
                    c = tuple(min(255, v_ + 40) for v_ in c)
                px[fx + xx, fy + yy] = clamp(c) + (255,)

for sname, st in STYLES.items():
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    for k in KINDS:
        paint(img, k, st)
    img.save(ROOT + "horns_%s.png" % sname)
print("ok", len(CUBES), "cubes per horn;", POS)
