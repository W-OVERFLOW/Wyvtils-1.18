/*
 * Rysm, a utility mod for 1.8.9.
 * Copyright (C) 2021 Rysm
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

package xyz.qalcyo.rysm.eight.mixin;

import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.qalcyo.rysm.core.RysmCore;
import xyz.qalcyo.rysm.core.listener.events.MessageRenderEvent;

@Pseudo
@Mixin(targets = "skytils.skytilsmod.features.impl.handlers.ChatTabs")
public class SkytilsChatTabsMixin {
    @Inject(method = "shouldAllow", at = @At("HEAD"))
    private void dg(IChatComponent chatComponent, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        MessageRenderEvent event = new MessageRenderEvent(chatComponent.getUnformattedText(), false);
        RysmCore.INSTANCE.getEventBus().post(event);
        if (event.getCancelled()) booleanCallbackInfoReturnable.setReturnValue(event.getCancelled());
    }
}
