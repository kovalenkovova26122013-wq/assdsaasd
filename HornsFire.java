package com.example.robloxhorns.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

/** Огонь между рогами - обычные частицы, спавнятся каждый тик. */
public final class HornsFire {
    private HornsFire() {
    }

    public static void tick(MinecraftClient client) {
        ClientWorld world = client.world;
        if (world == null || client.player == null || client.isPaused()) {
            return;
        }
        HornsConfig cfg = HornsConfig.get();
        if (cfg.horns == HornStyle.NONE || cfg.effectiveFire() == FireStyle.NONE) {
            return;
        }

        boolean firstPerson = client.options.getPerspective().isFirstPerson();
        spawn(world, client.player, cfg, !firstPerson || cfg.fireInFirstPerson);

        if (cfg.showOnOthers) {
            for (AbstractClientPlayerEntity p : world.getPlayers()) {
                if (p != client.player) {
                    spawn(world, p, cfg, true);
                }
            }
        }
    }

    private static void spawn(ClientWorld world, AbstractClientPlayerEntity p, HornsConfig cfg, boolean allowed) {
        if (!allowed || p.isSpectator() || p.isInvisible()) {
            return;
        }
        Random r = world.getRandom();
        FireStyle fire = cfg.effectiveFire();
        double s = cfg.hornScale;
        double baseY = p.getEyeY() + 0.30 * s;

        for (int i = 0; i < cfg.fireIntensity; i++) {
            double x = p.getX() + (r.nextDouble() - 0.5) * 0.24 * s;
            double z = p.getZ() + (r.nextDouble() - 0.5) * 0.24 * s;
            double y = baseY + r.nextDouble() * 0.25 * s;
            double vy = 0.02 + r.nextDouble() * 0.05;
            world.addParticleClient(effect(fire, world.getTime(), r), x, y, z,
                    (r.nextDouble() - 0.5) * 0.01, vy, (r.nextDouble() - 0.5) * 0.01);
        }
    }

    private static ParticleEffect effect(FireStyle fire, long time, Random r) {
        return switch (fire) {
            case BLUE -> ParticleTypes.SOUL_FIRE_FLAME;
            case GREEN -> new DustParticleEffect(0x55FF22, 1.2f);
            case PURPLE -> new DustParticleEffect(0xB040FF, 1.2f);
            case WHITE -> ParticleTypes.END_ROD;
            case RAINBOW -> new DustParticleEffect(
                    MathHelper.hsvToRgb(((time % 40) / 40.0f + r.nextFloat() * 0.1f) % 1.0f, 0.9f, 1.0f), 1.2f);
            default -> ParticleTypes.FLAME;
        };
    }
}
