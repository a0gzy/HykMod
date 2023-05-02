package me.a0g.hyk


import gg.essential.api.EssentialAPI
import gg.essential.api.utils.Multithreading
import me.a0g.hyk.commands.GetSw
import me.a0g.hyk.commands.HykCmd
import me.a0g.hyk.commands.SlotNbt
import me.a0g.hyk.core.AutoUpdaterX
import me.a0g.hyk.core.Config
import me.a0g.hyk.core.DiscordRPC
import me.a0g.hyk.core.features.client.ChatCopy
import me.a0g.hyk.core.features.client.Dev
import me.a0g.hyk.core.features.client.Ping
import me.a0g.hyk.core.features.client.WaypointPath
import me.a0g.hyk.core.features.hypixel.AutoTip
import me.a0g.hyk.core.features.hypixel.BuildBattleHelper
import me.a0g.hyk.core.listeners.ChatListener
import me.a0g.hyk.core.listeners.KeyListener
import me.a0g.hyk.core.listeners.RenderListener
import me.a0g.hyk.core.listeners.TickListener
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

@Mod(
    modid = HykX.MODID,
    version = HykX.VERSION,
    name = HykX.NAME,
    clientSideOnly = true,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object HykX {

    const val MODID = "hyk"
    const val VERSION = "4.0.8"
    const val NAME = "Hyk"

    val logger: Logger = LogManager.getLogger()
    val keyBindings = arrayOfNulls<KeyBinding>(1)


//    val server = HttpServer.create(InetSocketAddress(3030),0)

    val discordRpc = DiscordRPC()
    val tickListener = TickListener()

    lateinit var config: Config
    var dir: File? = null
    lateinit var jarFile: File
    var autoUpdater = AutoUpdaterX()

    var isDev = false

    var bbhWords = mutableListOf<String>()
    var bbh = BuildBattleHelper()

    val threadPool = Executors.newFixedThreadPool(10) as ThreadPoolExecutor
    var mods: Map<String, ModContainer>? = null

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        dir = File(event.modConfigurationDirectory, "hyk")
        dir!!.mkdirs()
        jarFile = event.sourceFile
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config = Config(File(dir, "hyk.toml"))
        config.init()

        val bbh = File(dir, "bbh")
        initBbh(bbh)

        if (!EssentialAPI.getMinecraftUtil().isDevelopment() && config.autoUpdate) {
            MinecraftForge.EVENT_BUS.register(autoUpdater)
            autoUpdater.downloadDelete()
        }

        registerKeybindings()

        arrayOf(
            this,
            KeyListener(),
            RenderListener(),
            ChatListener(),
            AutoTip(),
            Dev(),
            tickListener,
            ChatCopy(),
            WaypointPath,
            Ping
        ).forEach(MinecraftForge.EVENT_BUS::register)


    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        mods = Loader.instance().indexedModList


        val cch = ClientCommandHandler.instance

        cch.registerCommand(GetSw())
        cch.registerCommand(HykCmd())
        //cch.registerCommand(HyK())
        cch.registerCommand(SlotNbt())

    }

    @SubscribeEvent
    fun onLogin(event: ClientConnectedToServerEvent?) {
//        if (config.apiKey.isEmpty()) {
            Multithreading.runAsync {
                try {
                    Thread.sleep(10000)
//                    MessageUtil.sendMessage(EnumChatFormatting.GREEN.toString() + "You ApiKey isn't set upped - use /hyk api")
                    Ping.sendPing()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
//        }
    }


    private fun registerKeybindings() {
        keyBindings[0] = KeyBinding("Hyk Config", Keyboard.KEY_P, NAME)
        for (keyBinding in keyBindings) {
            ClientRegistry.registerKeyBinding(keyBinding)
        }
    }

    private fun initBbh(bbh: File) {
        if (bbh.exists()) {
            Multithreading.runAsync {
                try {
                    val lines =
                        Files.readAllLines(bbh.toPath(), StandardCharsets.UTF_8)
                    bbhWords.addAll(lines)
                    if (bbhWords.isEmpty()) {
                        downloadBbh(bbh)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            downloadBbh(bbh)
        }
    }

    private fun downloadBbh(bbh: File) {
        Multithreading.runAsync {
            try {
                val urlTask =
                    URL("https://raw.githubusercontent.com/a0gzy/HykData/dev/bbh")
                FileUtils.copyURLToFile(urlTask, bbh, 1000, 1000)
                Thread.sleep(4000)
                initBbh(bbh)
            } catch (e: IOException) {
                e.printStackTrace()
                FMLLog.info("HYK Download fail")
            }
        }
    }


}