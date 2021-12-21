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

package net.wyvest.wyvtils.mixin.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.wyvest.wyvtils.config.WyvtilsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin handles the Render Own Nametag feature
 * which is a version-independent feature.
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "TAIL", shift = At.Shift.BEFORE), cancellable = true)
    private void renderPlayerNametag(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                MinecraftClient.isHudEnabled() && (WyvtilsConfig.INSTANCE.getRenderOwnNametag() || livingEntity != MinecraftClient.getInstance().getCameraEntity()) && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player) && !livingEntity.hasPassengers()
        );
    }

}