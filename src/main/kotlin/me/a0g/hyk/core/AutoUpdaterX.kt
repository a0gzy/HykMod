package me.a0g.hyk.core

import gg.essential.api.EssentialAPI
import me.a0g.hyk.HykX
import me.a0g.hyk.core.events.MinecraftCloseEvent
import me.a0g.hyk.core.handlers.APIHandler
import me.a0g.hyk.utils.Util
import net.minecraft.client.gui.GuiMainMenu
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.io.FileUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URL

class AutoUpdaterX {

    private var isUpdated = false
    private var isUpdatedForPush = false

    private var updateLog: String? = null

    var hykDeleter: File? = null
    var oldHykJar: File? = null

    fun downloadDelete() {
        val sd = Thread(Runnable {
            val rep =
                APIHandler.getResponse("https://api.github.com/repos/a0gzy/HykMod/releases/latest")
            val taskDir = File(HykX.dir, "update")
            if (!taskDir.exists()) {
                taskDir.mkdir()
            }

            //name
            val newJarVersion = rep["name"].asString.replace("[.]".toRegex(), "")
            val oldJarVersion = HykX.VERSION.replace("[.]".toRegex(), "")
            if (newJarVersion.toInt() > oldJarVersion.toInt()) {
                HykX.logger.info("Hyk is now updating")
                val updateUrl =
                    rep["assets"].asJsonArray[0].asJsonObject["browser_download_url"].asString
                val jarName =
                    rep["assets"].asJsonArray[0].asJsonObject["name"].asString
                updateLog = rep["body"].asString
                if (updateUrl.isEmpty()) {
                    HykX.logger.info("updateUrl broken")
                    return@Runnable
                }
                val deleter = File(taskDir, "HykFileDeleter.jar")
                if (!deleter.exists()) {
                    try {
                        val urlTask = URL("https://raw.githubusercontent.com/a0gzy/HykData/dev/HykFileDeleter.jar")
                        FileUtils.copyURLToFile(urlTask, deleter, 1000, 1000)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                try {
                    val url = URL(updateUrl)
                    FileUtils.copyURLToFile(
                        url,
                        File(HykX.jarFile.parentFile, jarName), // /mods/ jarName
                        2000,
                        2000
                    ) //C:\Users\a0g\Desktop\Hyk\build\classes\java\main
                    isUpdated = true
                    isUpdatedForPush = true

                    hykDeleter = deleter
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else HykX.logger.info("HYK is up to date")
        })
        sd.name = "Hyk Updater thread"
        sd.start()
    }

    @SubscribeEvent
    fun onMinecraftClose(event: MinecraftCloseEvent){
        if(isUpdated){
            try {
                Thread {
                    val deleter = hykDeleter
                    val jar = HykX.jarFile
                    if(deleter != null && deleter.exists() && jar.exists()){
                        try {
                            val runtime = Util.getJavaRuntime() ?: "java"
                            val cmd = "\"$runtime\" -jar \"${deleter.absolutePath}\" \"${jar.absolutePath}\""
                            HykX.logger.info(cmd)
                            Runtime.getRuntime().exec(cmd)
                        } catch (e: IOException) {
                            HykX.logger.info("Failed exec")
                            e.printStackTrace()
                            try {
                                Desktop.getDesktop().open(jar.parentFile)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }.start()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    @SubscribeEvent
    fun onGuiOpen(e: DrawScreenEvent.Post) {
        if (e.gui is GuiMainMenu) {
            if (isUpdatedForPush) {
                EssentialAPI.getNotifications().push(
                    "Hyk Updated. Please reload.",
                    updateLog ?: "",
                    15f
                )
                isUpdatedForPush = false
            }
        }
    }

}