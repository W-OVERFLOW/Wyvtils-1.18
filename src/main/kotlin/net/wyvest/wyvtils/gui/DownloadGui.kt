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

package net.wyvest.wyvtils.gui

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildConfirmationModal
import gg.essential.api.utils.Multithreading
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import net.minecraft.MinecraftVersion
import net.wyvest.wyvtils.Wyvtils
import net.wyvest.wyvtils.WyvtilsInfo
import net.wyvest.wyvtils.utils.Updater
import java.io.File

/**
 * The GUI which is displayed when the user is asked for confirmation
 * of an update.
 */
class DownloadGui : WindowScreen(restoreCurrentGuiOnClose = true, version = ElementaVersion.V1) {

    /**
     * Initializes the screen and builds an Essential Confirmation Modal.
     */
    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        EssentialAPI.getEssentialComponentFactory().buildConfirmationModal {
            this.text = "Are you sure you want to update?"
            this.secondaryText = "(This will update from v${WyvtilsInfo.VER} to ${Updater.latestTag})"
            this.onConfirm = {
                restorePreviousScreen()
                Multithreading.runAsync {
                    if (Updater.download(
                            Updater.updateUrl,
                            File(
                                "mods/${WyvtilsInfo.NAME}-${MinecraftVersion.CURRENT.name}-${
                                    Updater.latestTag.substringAfter(
                                        "v"
                                    )
                                }.jar"
                            )
                        ) && Updater.download(
                            "https://github.com/Wyvest/Deleter/releases/download/v1.2/Deleter-1.2.jar",
                            File(File(Wyvtils.modDir.parentFile.parentFile, "Libraries"), "Deleter-1.2.jar")
                        )
                    ) {
                        EssentialAPI.getNotifications()
                            .push(WyvtilsInfo.NAME, "The ingame updater has successfully installed the newest version.")
                        Updater.addShutdownHook()
                        Updater.shouldUpdate = false
                    } else {
                        EssentialAPI.getNotifications().push(
                            WyvtilsInfo.NAME,
                            "The ingame updater has NOT installed the newest version as something went wrong."
                        )
                    }
                }
            }
            this.onDeny = {
                restorePreviousScreen()
            }
        } childOf this.window
    }
}