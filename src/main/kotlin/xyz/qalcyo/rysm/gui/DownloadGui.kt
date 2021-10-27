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

package xyz.qalcyo.rysm.gui

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildConfirmationModal
import gg.essential.api.utils.Multithreading
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import net.minecraft.client.gui.GuiScreen
import xyz.qalcyo.rysm.Rysm
import xyz.qalcyo.rysm.RysmInfo
import xyz.qalcyo.rysm.utils.Updater
import java.io.File

class DownloadGui(private val parent: GuiScreen): WindowScreen(restoreCurrentGuiOnClose = true) {
    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        EssentialAPI.getEssentialComponentFactory().buildConfirmationModal {
            this.text = "Are you sure you want to update?"
            this.secondaryText = "(This will update from v${RysmInfo.VER} to ${Updater.latestTag})"
            this.onConfirm = {
                EssentialAPI.getGuiUtil().openScreen(parent)
                Multithreading.runAsync {
                    if (Updater.download(
                            Updater.updateUrl,
                            File("mods/${RysmInfo.NAME} [1.8.9]-${Updater.latestTag.substringAfter("v")}.jar")
                        ) && Updater.download(
                            "https://github.com/Qalcyo/Deleter/releases/download/v1.2/Deleter-1.2.jar",
                            File(Rysm.modDir.parentFile, "Deleter-1.2.jar")
                        )
                    ) {
                        EssentialAPI.getNotifications()
                            .push(RysmInfo.NAME, "The ingame updater has successfully installed the newest version.")
                        Updater.addShutdownHook()
                        Updater.shouldUpdate = false
                    } else {
                        EssentialAPI.getNotifications().push(
                            RysmInfo.NAME,
                            "The ingame updater has NOT installed the newest version as something went wrong."
                        )
                    }
                }
            }
            this.onDeny = {
                EssentialAPI.getGuiUtil().openScreen(parent)
            }
        } childOf this.window
    }
}