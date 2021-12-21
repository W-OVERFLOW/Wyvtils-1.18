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

package net.wyvest.wyvtils.config

import gg.essential.api.EssentialAPI
import gg.essential.universal.ChatColor
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import net.wyvest.wyvtils.WyvtilsInfo
import net.wyvest.wyvtils.listener.events.BossBarResetEvent
import net.wyvest.wyvtils.listener.events.ChatRefreshEvent
import net.wyvest.wyvtils.listener.events.Gui
import net.wyvest.wyvtils.listener.events.RenderGuiEvent
import net.wyvest.wyvtils.utils.Updater
import net.wyvest.wyvtils.Wyvtils
import java.awt.Color
import java.io.File

/**
 * The main configuration of the mod, powered by the Vigilance library.
 */
object WyvtilsConfig: Vigilant(
    File(Wyvtils.modDir, "${WyvtilsInfo.ID}.toml"), "${ChatColor.DARK_PURPLE}${WyvtilsInfo.NAME}", sortingBehavior = ConfigSorting
) {
    @Property(
        type = PropertyType.SWITCH,
        name = "Reverse Inventory Scrolling",
        description = "Reverse the direction of which the inventory scrolls to when scrolling on your mouse.",
        category = "General"
    )
    var reverseScrolling = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Locraw Messages",
        description = "Hide locraw messages in chat (e.g {\"server\": \"something\"}).",
        category = "General"
    )
    var hideLocraw = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Text Shadow",
        description = "Disable the shadow on text rendering. Can boost performance.",
        category = "General"
    )
    var disableTextShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Render Own Nametag",
        description = "Render your own nametag in third person.",
        category = "General",
        subcategory = "Nametags"
    )
    var renderOwnNametag = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Nametag Background",
        description = "Remove the background of nametags.",
        category = "General",
        subcategory = "Nametags"
    )
    var removeNametagBackground = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Nametag Text Shadow",
        description = "Render a text shadow with the nametag text.",
        category = "General",
        subcategory = "Nametags"
    )
    var nametagTextShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Bossbar Customization",
        description = "Toggle Bossbar customization",
        category = "Bossbar"
    )
    var bossBarCustomization = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Bossbar",
        description = "Toggle the bossbar.",
        category = "Bossbar"
    )
    var bossBar = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Text",
        description = "Toggle the text for the bossbar.",
        category = "Bossbar"
    )
    var bossBarText = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Shadow",
        description = "Toggle the text shadow for the bossbar.",
        category = "Bossbar"
    )
    var bossBarShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Bar",
        description = "Toggle the bar for the bossbar.",
        category = "Bossbar"
    )
    var bossBarBar = true

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Bossbar Scale",
        description = "Set the scale for the bossbar.",
        category = "Bossbar"
    )
    var bossbarScale = 1.0F

    @Property(
        type = PropertyType.BUTTON,
        name = "Bossbar Editor",
        description = "Change the position of the bossbar.",
        category = "Bossbar"
    )
    fun openBossHealthGui() {
        if (bossBarCustomization) Wyvtils.eventBus.post(RenderGuiEvent(Gui.BOSSBAR))
        else EssentialAPI.getNotifications()
            .push(WyvtilsInfo.NAME, "You can't do that, you haven't enabled Bossbar Customization!")
    }

    @Property(
        type = PropertyType.BUTTON,
        name = "Reset Position",
        description = "Reset the position of the bossbar to its original position.",
        category = "Bossbar"
    )
    fun resetBossbar() {
        Wyvtils.eventBus.post(BossBarResetEvent())
    }

    @Property(
        type = PropertyType.NUMBER,
        name = "Bossbar X",
        description = "X",
        category = "Bossbar",
        hidden = true
    )
    var bossBarX: Int = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "Bossbar Y",
        description = "Y",
        category = "Bossbar",
        hidden = true
    )
    var bossBarY: Int = 12


    @Property(
        type = PropertyType.SWITCH,
        name = "bossbar first time",
        description = "a",
        category = "General",
        hidden = true
    )
    var firstLaunchBossbar = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Action Bar Customization",
        description = "Toggle customization of the action bar.",
        category = "Action Bar"
    )
    var actionBarCustomization = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Action Bar",
        description = "Toggle the action bar.",
        category = "Action Bar"
    )
    var actionBar = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Action Bar Shadow",
        description = "Toggle the action bar shadow.",
        category = "Action Bar"
    )
    var actionBarShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Action Bar Position",
        description = "Toggle the action bar position customization.",
        category = "Action Bar"
    )
    var actionBarPosition = false

    @Property(
        type = PropertyType.BUTTON,
        name = "Action Bar Editor",
        description = "Change the position of the action bar.",
        category = "Action Bar"
    )
    fun openActionBarGui() {
        if (actionBarPosition && actionBarCustomization) Wyvtils.eventBus.post(RenderGuiEvent(Gui.ACTIONBAR))
        else EssentialAPI.getNotifications()
            .push(WyvtilsInfo.NAME, "You can't do that, you don't have Action Bar position enabled!")
    }

    @Property(
        type = PropertyType.NUMBER,
        name = "Action Bar X",
        description = "X",
        category = "Action Bar",
        hidden = true
    )
    var actionBarX: Int = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "Action Bar Y",
        description = "Y",
        category = "Action Bar",
        hidden = true
    )
    var actionBarY: Int = 12

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Title",
        description = "Toggle the title on or off.",
        category = "Titles"
    )
    var title = true

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Title Scale Percentage",
        description = "Change the scale of the title.",
        category = "Titles",
    )
    var titleScale = 1.0F

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Subtitle Scale Percentage",
        description = "Change the scale of the subtitle.",
        category = "Titles",
    )
    var subtitleScale = 1.0F

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Title Shadow",
        description = "Toggle the title and subtitle's shadow.",
        category = "Titles"
    )
    var titleShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Sidebar",
        category = "Sidebar",
        description = "Toggle the sidebar from rendering."
    )
    var sidebar = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Sidebar Text Shadow",
        category = "Sidebar",
        description = "Toggle the sidebar text's shadow from rendering."
    )
    var sidebarTextShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Score Points",
        category = "Sidebar",
        description = "Toggle the sidebar score points (aka red numbers) from rendering."
    )
    var sidebarScorePoints = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Background",
        category = "Sidebar",
        description = "Toggle the background from rendering."
    )
    var sidebarBackground = true

    @Property(
        type = PropertyType.COLOR,
        name = "Sidebar Background Color",
        category = "Sidebar",
        description = "Change the text color for the sidebar."
    )
    var sidebarBackgroundColor: Color = Color(0, 0, 0, 50)

    @Property(
        type = PropertyType.SWITCH,
        name = "Sidebar Position",
        category = "Sidebar",
        description = "Toggle the sidebar position editor."
    )
    var sidebarPosition = false

    @Property(
        type = PropertyType.BUTTON,
        name = "Sidebar Editor",
        description = "Change the position of the sidebar.",
        category = "Sidebar"
    )
    fun openSidebarGui() {
        if (sidebarPosition) Wyvtils.eventBus.post(RenderGuiEvent(Gui.SIDEBAR))
        else EssentialAPI.getNotifications()
            .push(WyvtilsInfo.NAME, "You can't do that, you don't have Sidebar position enabled!")
    }

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Sidebar Scale",
        description = "Set the scale for the sidebar.",
        category = "Sidebar"
    )
    var sidebarScale = 1.0F

    @Property(
        type = PropertyType.NUMBER,
        name = "Sidebar X",
        description = "X",
        category = "Sidebar",
        hidden = true
    )
    var sidebarX: Int = 0

    @Property(
        type = PropertyType.NUMBER,
        name = "Sidebar Y",
        description = "Y",
        category = "Sidebar",
        hidden = true
    )
    var sidebarY: Int = 12

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox",
        category = "Hitbox",
        description = "Toggle the hitbox of entities."
    )
    var hitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Player Hitboxes",
        description = "Turn on and off player hitboxes from displaying.",
        category = "Hitbox"
    )
    var playerHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Passive Hitboxes",
        description = "Turn on and off passive entity hitboxes from displaying.",
        category = "Hitbox"
    )
    var passiveHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Monster Hitboxes",
        description = "Turn on and off monster hitboxes from displaying.",
        category = "Hitbox"
    )
    var monsterHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Armorstand Hitboxes",
        description = "Turn on and off player armorstand from displaying.",
        category = "Hitbox"
    )
    var armorstandHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Fireball Hitboxes",
        description = "Turn on and off fireball hitboxes from displaying.",
        category = "Hitbox"
    )
    var fireballHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Minecart Hitboxes",
        description = "Turn on and off minecart hitboxes from displaying.",
        category = "Hitbox"
    )
    var minecartHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Wither Skull Hitboxes",
        description = "Turn on and off wither skull hitboxes from displaying.",
        category = "Hitbox"
    )
    var witherSkullHitboxes = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Item Hitboxes",
        description = "Turn on and off item hitboxes from displaying.",
        category = "Hitbox"
    )
    var itemHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Item Frame Hitboxes",
        description = "Turn on and off item frame hitboxes from displaying.",
        category = "Hitbox"
    )
    var itemFrameHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Firework Hitboxes",
        description = "Turn on and off firework hitboxes from displaying.",
        category = "Hitbox"
    )
    var fireworkHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle XP Orb Hitboxes",
        description = "Turn on and off XP Orb hitboxes from displaying.",
        category = "Hitbox"
    )
    var xpOrbHitbox = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Projectile Hitboxes",
        description = "Turn on and off projectile hitboxes from displaying.",
        category = "Hitbox"
    )
    var projectileHitbox = true

    @Property(
        type = PropertyType.NUMBER,
        name = "Hitbox Width",
        category = "Hitbox",
        description = "Change the width of hitboxes.",
        min = 1,
        max = 10
    )
    var hitboxWidth = 1

    @Property(
        type = PropertyType.SWITCH,
        name = "Force Hitboxes",
        category = "Hitbox",
        description = "Force the rendering of hitbox of entities."
    )
    var forceHitbox = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable for Self",
        category = "Hitbox",
        description = "Don't render the hitbox if the player's hitbox is you."
    )
    var disableForSelf = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox Box",
        category = "Hitbox",
        description = "Toggle the hitbox box of entities."
    )
    var hitboxBox = true

    @Property(
        type = PropertyType.COLOR,
        name = "Hitbox Color",
        category = "Hitbox",
        description = "Change the color of the hitbox.",
    )
    var hitboxColor: Color = Color.WHITE

    @Property(
        type = PropertyType.COLOR,
        name = "Hitbox Color (within crosshair)",
        category = "Hitbox",
        description = "Change the color of the hitbox of players when they are within the crosshair of the player.",
    )
    var hitboxCrosshairColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox Chroma",
        category = "Hitbox",
        description = "Toggle hitbox chroma to the color."
    )
    var hitboxChroma = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Line Hitbox",
        category = "Hitbox",
        description = "Toggle the eye line hitbox of entities."
    )
    var hitboxEyeLine = true

    @Property(
        type = PropertyType.COLOR,
        name = "Hitbox Line Color",
        category = "Hitbox",
        description = "Change the color of the hitbox eye line.",
    )
    var hitboxEyelineColor: Color = Color(0, 0, 255, 255)

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox Line Chroma",
        category = "Hitbox",
        description = "Toggle hitbox eye line chroma to the color."
    )
    var hitboxLineChroma = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox Line of Sight",
        category = "Hitbox",
        description = "Toggle the hitbox of entities."
    )
    var hitboxLineOfSight = true

    @Property(
        type = PropertyType.COLOR,
        name = "Line of Sight Color",
        category = "Hitbox",
        description = "Change the color of the hitbox's line of sight.",
    )
    var hitboxLineOfSightColor: Color = Color(255, 0, 0, 255)

    @Property(
        type = PropertyType.SWITCH,
        name = "Toggle Hitbox Line of Sight Chroma",
        category = "Hitbox",
        description = "Toggle hitbox line of sight chroma to the color."
    )
    var hitboxLineOfSightChroma = false

    @Property(
        type = PropertyType.COLOR,
        name = "Debug HUD Color",
        description = "Set the color of the Debug HUD background color.",
        category = "Debug Screen"
    )
    var debugColor: Color = Color(-1873784752)

    @Property(
        type = PropertyType.SWITCH,
        name = "Debug HUD Shadow",
        description = "Turn on / off the shadow of Debug HUD text.",
        category = "Debug Screen"
    )
    var debugShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Pack Search Box",
        category = "Pack GUI Modifier",
        description = "Add a search box to search packs in the pack GUI.",
    )
    var packSearchBox = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Pack GUI Background",
        category = "Pack GUI Modifier",
        description = "Remove the dirt background in the pack GUI.",
    )
    var transparentPackGUI = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Incompatible Packs",
        category = "Pack GUI Modifier",
        description = "Remove incompatible packs from the pack GUI.",
    )
    var hideIncompatiblePacks = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Reverse Packs",
        category = "Pack GUI Modifier",
        description = "Reverse the order of packs in the pack GUI.",
    )
    var reversePacks = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Update Notification",
        description = "Show a notification when you start Minecraft informing you of new updates.",
        category = "Updater"
    )
    var showUpdateNotification = true

    @Property(
        type = PropertyType.SWITCH,
        name = "First time",
        description = "ok.",
        category = "General",
        hidden = true
    )
    var firstTime = true

    @Property(
        type = PropertyType.BUTTON,
        name = "Update Now",
        description = "Update ${WyvtilsInfo.NAME} by clicking the button.",
        category = "Updater"
    )
    fun update() {
        if (Updater.shouldUpdate) Wyvtils.eventBus.post(RenderGuiEvent(Gui.UPDATER)) else EssentialAPI.getNotifications()
            .push(WyvtilsInfo.NAME, "No update had been detected at startup, and thus the update GUI has not been shown.")
    }

    init {
        initialize()
        registerListener("hideLocraw") { boolean: Boolean ->
            hideLocraw = boolean
            Wyvtils.eventBus.post(ChatRefreshEvent())
        }
    }

    private object ConfigSorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
            if (o1.name == "General") return@Comparator -1
            if (o2.name == "General") return@Comparator 1
            else compareValuesBy(o1, o2) {
                it.name
            }
        }
    }
}