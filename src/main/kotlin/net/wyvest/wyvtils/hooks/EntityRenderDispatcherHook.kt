/*
 * Wyvtils, a utility mod for 1.8.9.
 * Copyright (C) 2021 Wyvtils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.wyvtils.hooks

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.*
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.entity.vehicle.MinecartEntity
import net.minecraft.util.math.Box
import net.wyvest.wyvtils.Wyvtils.eventBus
import net.wyvest.wyvtils.config.WyvtilsConfig
import net.wyvest.wyvtils.listener.events.HitboxRenderEvent
import net.wyvest.wyvtils.utils.ColorUtils.getAlpha
import net.wyvest.wyvtils.utils.ColorUtils.getBlue
import net.wyvest.wyvtils.utils.ColorUtils.getGreen
import net.wyvest.wyvtils.utils.ColorUtils.getRed
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.invoke.arg.Args
import java.awt.Color

private var hitboxRenderEvent: HitboxRenderEvent? = null
var prevWidth = 0F

fun invokeHitboxEvent(
    entityIn: Entity,
    ci: CallbackInfo
) {
    hitboxRenderEvent = HitboxRenderEvent(
        when (entityIn) {
            is PlayerEntity -> if (entityIn is ClientPlayerEntity && (entityIn as PlayerEntity).gameProfile.id === if (MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player!!.gameProfile.id else "null") net.wyvest.wyvtils.listener.events.Entity.SELF else net.wyvest.wyvtils.listener.events.Entity.PLAYER
            is MobEntity -> if (entityIn is Monster) net.wyvest.wyvtils.listener.events.Entity.MONSTER else net.wyvest.wyvtils.listener.events.Entity.LIVING
            is ArmorStandEntity -> net.wyvest.wyvtils.listener.events.Entity.ARMORSTAND
            is FireballEntity -> net.wyvest.wyvtils.listener.events.Entity.FIREBALL
            is MinecartEntity -> net.wyvest.wyvtils.listener.events.Entity.MINECART
            is ItemEntity -> net.wyvest.wyvtils.listener.events.Entity.ITEM
            is ItemFrameEntity -> net.wyvest.wyvtils.listener.events.Entity.ITEMFRAME
            is FireworkRocketEntity -> net.wyvest.wyvtils.listener.events.Entity.FIREWORK
            is ExperienceOrbEntity -> net.wyvest.wyvtils.listener.events.Entity.XP
            is TridentEntity, is ShulkerBulletEntity,
            is FishingBobberEntity, is LlamaSpitEntity,
            is ArrowEntity, is ThrownEntity,
            is SpectralArrowEntity -> net.wyvest.wyvtils.listener.events.Entity.PROJECTILE
            else -> net.wyvest.wyvtils.listener.events.Entity.UNDEFINED
        },
        (if (MinecraftClient.getInstance().targetedEntity != null && MinecraftClient.getInstance().targetedEntity === entityIn) 2 else Int.MAX_VALUE).toDouble(),
        Color.WHITE.rgb,
        Color.WHITE.rgb,
        Color.WHITE.rgb,
        cancelled = false,
        cancelBox = false,
        cancelLineOfSight = false,
        cancelEyeLine = false
    )
    eventBus.post(hitboxRenderEvent!!)
    if (hitboxRenderEvent!!.cancelled) {
        ci.cancel()
    } else {
        if (WyvtilsConfig.hitboxWidth != 1) {
            prevWidth = RenderSystem.getShaderLineWidth()
            RenderSystem.lineWidth(WyvtilsConfig.hitboxWidth.toFloat())
        }
    }
}

fun modifyBox(
    matrices: MatrixStack,
    vertexConsumer: VertexConsumer,
    box: Box
) {
    if (!hitboxRenderEvent!!.cancelBox) {
        WorldRenderer.drawBox(
            matrices,
            vertexConsumer,
            box,
            getRed(hitboxRenderEvent!!.boxColor)
                .toFloat() / 255,
            getGreen(hitboxRenderEvent!!.boxColor).toFloat() / 255,
            getBlue(hitboxRenderEvent!!.boxColor)
                .toFloat() / 255,
            getAlpha(hitboxRenderEvent!!.boxColor).toFloat() / 255
        )
    }
}

fun modifyLineOfSight(
    matrices: MatrixStack,
    vertexConsumer: VertexConsumer,
    x1: Double,
    y1: Double,
    z1: Double,
    x2: Double,
    y2: Double,
    z2: Double
) {
    if (!hitboxRenderEvent!!.cancelLineOfSight) {
        WorldRenderer.drawBox(
            matrices,
            vertexConsumer,
            x1,
            y1,
            z1,
            x2,
            y2,
            z2,
            getRed(hitboxRenderEvent!!.lineOfSightColor)
                .toFloat() / 255,
            getGreen(hitboxRenderEvent!!.lineOfSightColor).toFloat() / 255,
            getBlue(hitboxRenderEvent!!.lineOfSightColor)
                .toFloat() / 255,
            getAlpha(hitboxRenderEvent!!.lineOfSightColor).toFloat() / 255
        )
    }
}

fun cancelEyeline(
    ci: CallbackInfo
) {
    if (hitboxRenderEvent!!.cancelEyeLine) ci.cancel()
}

fun modifyEyeLine(args: Args) {
    args.set(0, getRed(hitboxRenderEvent!!.eyeLineColor))
    args.set(1, getGreen(hitboxRenderEvent!!.eyeLineColor))
    args.set(2, getBlue(hitboxRenderEvent!!.eyeLineColor))
    args.set(3, getAlpha(hitboxRenderEvent!!.eyeLineColor))
}