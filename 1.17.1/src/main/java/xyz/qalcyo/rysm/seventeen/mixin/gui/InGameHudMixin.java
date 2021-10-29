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

package xyz.qalcyo.rysm.seventeen.mixin.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import xyz.qalcyo.rysm.core.RysmCore;
import xyz.qalcyo.rysm.core.listener.events.TitleEvent;

/**
 * This mixin sends and handles the TitleEvent which is used
 * in the core submodule to modify rendered titles and
 * subtitles ingame.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    private TitleEvent titleEvent;

    /**
     * Invokes the TitleEvent and modifies the title scale accordingly.
     */
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void modifyTitleTranslate(Args args) {
        titleEvent = new TitleEvent(false, 1.0F, 1.0F, true);
        RysmCore.INSTANCE.getEventBus().post(titleEvent);
        if (((float) args.get(0)) == 4.0F) {
            args.set(0, 4.0F * titleEvent.getTitleScale());
            args.set(1, 4.0F * titleEvent.getTitleScale());
        } else if (((float) args.get(0)) == 2.0F) {
            args.set(0, 2.0F * titleEvent.getSubtitleScale());
            args.set(1, 2.0F * titleEvent.getSubtitleScale());
        }
    }

    /**
     * Cancels and sets the shadow of the title and subtitle based on the invoked event.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private int redirect(TextRenderer instance, MatrixStack matrices, Text text, float x, float y, int color) {
        if (!titleEvent.getCancelled()) {
            if (titleEvent.getShadow()) {
                return instance.drawWithShadow(matrices, text, x, y, color);
            } else {
                return instance.draw(matrices, text, x, y, color);
            }
        } else {
            return -1;
        }
    }
}