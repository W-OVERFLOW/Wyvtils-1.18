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

package net.wyvest.wyvtils.listener

import gg.essential.lib.kbrewster.eventbus.Subscribe
import net.wyvest.wyvtils.config.WyvtilsConfig
import net.wyvest.wyvtils.listener.events.*
import net.wyvest.wyvtils.utils.ColorUtils
import java.util.regex.Pattern

/**
 * Handles mostly all the internal handling of Wyvtils.
 */
object Listener {
    private val regex = Pattern.compile("(?i)§[0-9A-FK-OR]")

    @Subscribe
    fun onTitle(e: TitleEvent) {
        e.cancelled = !WyvtilsConfig.title
        e.shadow = WyvtilsConfig.titleShadow
        e.titleScale = WyvtilsConfig.titleScale
        e.subtitleScale = WyvtilsConfig.subtitleScale
    }

    @Subscribe
    fun onMessage(e: MessageRenderEvent) {
        if (WyvtilsConfig.hideLocraw) {
            val stripped = strip(e.message)
            if (stripped.startsWith("{") && stripped.contains("server") && stripped.endsWith("}")) {
                e.cancelled = true
            }
        }
    }

    @Subscribe
    fun onMouseScroll(e: MouseScrollEvent) {
        if (WyvtilsConfig.reverseScrolling) {
            if (e.scroll != 0.0) {
                e.scroll = e.scroll * -1
            }
        }
    }

    @Subscribe
    fun onHitbox(e: HitboxRenderEvent) {

        if (!WyvtilsConfig.hitbox) {
            e.cancelled = true
            return
        }
        e.cancelled = when (e.entity) {
            Entity.ARMORSTAND -> !WyvtilsConfig.armorstandHitbox
            Entity.FIREBALL -> !WyvtilsConfig.fireballHitbox
            Entity.FIREWORK -> !WyvtilsConfig.fireworkHitbox
            Entity.ITEM -> !WyvtilsConfig.itemHitbox
            Entity.ITEMFRAME -> !WyvtilsConfig.itemFrameHitbox
            Entity.LIVING -> !WyvtilsConfig.passiveHitbox
            Entity.MONSTER -> !WyvtilsConfig.monsterHitbox
            Entity.MINECART -> !WyvtilsConfig.minecartHitbox
            Entity.PLAYER -> !WyvtilsConfig.playerHitbox
            Entity.SELF -> WyvtilsConfig.disableForSelf || !WyvtilsConfig.playerHitbox
            Entity.PROJECTILE -> !WyvtilsConfig.projectileHitbox
            Entity.WITHERSKULL -> !WyvtilsConfig.witherSkullHitboxes
            Entity.XP -> !WyvtilsConfig.xpOrbHitbox
            Entity.UNDEFINED -> false
        }
        if (e.cancelled) return
        if (WyvtilsConfig.hitboxBox) {
            e.boxColor = if (WyvtilsConfig.hitboxChroma) ColorUtils.timeBasedChroma() else {
                if (e.distance <= 3.0 && e.distance != -1.0) WyvtilsConfig.hitboxCrosshairColor.rgb else WyvtilsConfig.hitboxColor.rgb
            }
        } else {
            e.cancelBox = true
        }
        if (WyvtilsConfig.hitboxEyeLine) {
            e.eyeLineColor =
                if (WyvtilsConfig.hitboxLineChroma) ColorUtils.timeBasedChroma() else WyvtilsConfig.hitboxEyelineColor.rgb
        } else {
            e.cancelEyeLine = true
        }
        if (WyvtilsConfig.hitboxLineOfSight) {
            e.lineOfSightColor =
                if (WyvtilsConfig.hitboxLineOfSightChroma) ColorUtils.timeBasedChroma() else WyvtilsConfig.hitboxLineOfSightColor.rgb
        } else {
            e.cancelLineOfSight = true
        }
    }

    private fun strip(string: String): String {
        return regex.matcher(string).replaceAll("")
    }
}