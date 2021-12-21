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

package net.wyvest.wyvtils

import gg.essential.api.EssentialAPI
import gg.essential.lib.kbrewster.eventbus.Subscribe
import gg.essential.lib.kbrewster.eventbus.eventbus
import gg.essential.lib.kbrewster.eventbus.invokers.LMFInvoker
import gg.essential.universal.UDesktop
import gg.essential.universal.UResolution
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.wyvest.wyvtils.config.WyvtilsConfig
import net.wyvest.wyvtils.gui.ActionBarGui
import net.wyvest.wyvtils.gui.BossHealthGui
import net.wyvest.wyvtils.gui.DownloadGui
import net.wyvest.wyvtils.gui.SidebarGui
import net.wyvest.wyvtils.listener.Listener
import net.wyvest.wyvtils.listener.events.*
import net.wyvest.wyvtils.mixin.gui.ChatHudAccessor
import net.wyvest.wyvtils.utils.Updater
import org.lwjgl.glfw.GLFW
import java.io.File
import java.net.URI


object Wyvtils : ClientModInitializer {

    val eventBus = eventbus {
        invoker { LMFInvoker() }
        exceptionHandler { exception -> println("Error occurred in method: ${exception.message}") }
    }

    lateinit var modDir: File
    private set

    lateinit var jarFile: File
    private set

    var needsToCancel = false
    private val keyBinding: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            "key.wyvtils.keybind", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_X, // The keycode of the key
            "category.wyvtils.ok" // The translation key of the keybinding's category.
        )
    )
    var packY: Int? = null
    var packBottom: Int? = null

    /**
     * Handles the initialization of the mod.
     */
    override fun onInitializeClient() {
        eventBus.register(this)
        modDir =
            File(File(File(FabricLoader.getInstance().configDir.toFile(), "W-OVERFLOW"), "Wyvtils"), "1.18")
        if (!modDir.exists()) {
            modDir.mkdirs()
        }
        jarFile = File(javaClass.protectionDomain.codeSource.location.toURI())
        WyvtilsConfig.preload()
        Updater.update()
        eventBus.register(Listener)
        ClientTickEvents.END_CLIENT_TICK.register {
            while (keyBinding.wasPressed() && it.currentScreen == null) {
                EssentialAPI.getGuiUtil().openScreen(WyvtilsConfig.gui())
                return@register
            }
        }
        WorldRenderEvents.END.register {
            if (WyvtilsConfig.firstTime) {
                EssentialAPI.getNotifications().push(
                    "Wyvtils",
                    "Hello! As this is your first time using this mod, click the key Z on your keyboard to configure the many features in Wyvtils!"
                )
                WyvtilsConfig.firstTime = false
                WyvtilsConfig.markDirty()
                WyvtilsConfig.writeData()
            }

        }
    }

    @Subscribe
    fun onChatRefresh(e: ChatRefreshEvent) {
        val chat = MinecraftClient.getInstance().inGameHud.chatHud as ChatHudAccessor
        try {
            MinecraftClient.getInstance().inGameHud.chatHud.reset()
        } catch (e: Exception) {
            e.printStackTrace()
            EssentialAPI.getNotifications().push(
                WyvtilsInfo.NAME,
                "There was a critical error while trying to refresh the chat. Please go to inv.wtf/qalcyo or click on this notification to fix this issue."
            ) {
                UDesktop.browse(URI.create("https://inv.wtf/qalcyo"))
            }
            chat.visibleMessages.clear()
            MinecraftClient.getInstance().inGameHud.chatHud.resetScroll()
            for (i in chat.messages.asReversed()) {
                chat.invokeAddMessage(i.text, i.id, i.creationTick, true)
            }
        }
    }

    @Subscribe
    fun onNotification(e: UpdateEvent) {
        EssentialAPI.getNotifications()
            .push(
                "Mod Update",
                "${WyvtilsInfo.NAME} ${e.version} is available!\nClick here to download it!",
                5f
            ) {
                EssentialAPI.getGuiUtil().openScreen(DownloadGui())
            }
    }

    @Subscribe
    fun onRenderGui(e: RenderGuiEvent) {
        when (e.gui) {
            Gui.BOSSBAR -> EssentialAPI.getGuiUtil().openScreen(BossHealthGui())
            Gui.ACTIONBAR -> EssentialAPI.getGuiUtil().openScreen(ActionBarGui())
            Gui.SIDEBAR -> EssentialAPI.getGuiUtil().openScreen(SidebarGui())
            Gui.UPDATER -> EssentialAPI.getGuiUtil().openScreen(DownloadGui())
        }
    }

    @Subscribe
    fun onBossBarReset(e: BossBarResetEvent) {
        EssentialAPI.getGuiUtil().openScreen(null)
        WyvtilsConfig.bossBarX = (UResolution.scaledWidth / 2)
        WyvtilsConfig.bossBarY = 12
        WyvtilsConfig.markDirty()
        WyvtilsConfig.writeData()
        EssentialAPI.getGuiUtil().openScreen(WyvtilsConfig.gui())
    }

}