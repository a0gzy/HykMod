package me.a0g.hyk.core

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import me.a0g.hyk.HykX
import me.a0g.hyk.utils.HypixelUtil
import org.json.JSONObject
import java.time.OffsetDateTime
import java.util.*

class DiscordRPC : IPCListener {

    private var connected = false
    private var firstConnection = true

    var details = "Forge 1.8.9"
    var state = ""
    var smallImageKey = ""
    var smallImageText = ""
    var largeImageKey = "https://i.imgur.com/ND3700u.gif"
    var largeImageText = "Hyk ${HykX.VERSION} by a0g#1387"
    //var largeImageKey = "rpc"

    private val APPLICATION_ID = 862685928195096617L
    private var updateTimer: Timer? = null


    private var client: IPCClient? = null
    private var startTimestamp: OffsetDateTime? = null


    fun startRpc() {
        try {
            if (isRpcActive()) {
                return
            }
            client = IPCClient(APPLICATION_ID)
            client!!.setListener(this)
            if (firstConnection) {
                startTimestamp = OffsetDateTime.now()
                firstConnection = false
            }
            client!!.connect()
            val presence = RichPresence.Builder()
                .setDetails(details)
                .setState(state)
                .setStartTimestamp(startTimestamp)
                .setLargeImage(largeImageKey, largeImageText)
                .build()
            client!!.sendRichPresence(presence)
        } catch (ex: Throwable) {
            // logger.error("DiscordRP has thrown an unexpected error while trying to start...");
            ex.printStackTrace()
        }
    }

    fun isRpcActive(): Boolean {
        return client != null && connected
    }

    fun stopRpc() {
        if (isRpcActive()) {
            client!!.close()
            connected = false
        }
    }

    fun updateRichPresence() {
        if (isRpcActive()) {
            val presence = RichPresence.Builder()
                .setDetails(details)
                .setState(state)
                .setLargeImage(largeImageKey, largeImageText)
                .setStartTimestamp(startTimestamp)
            if (smallImageKey != "") {
                presence.setSmallImage(smallImageKey, smallImageText)
            }
            client!!.sendRichPresence(presence.build())
        }
    }

    override fun onReady(client: IPCClient?) {
        connected = true
        updateTimer = Timer()
        updateTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                HypixelUtil.setRPCGame()
                updateRichPresence()
            }
        }, 0, (5 * 1000).toLong())
    }

    override fun onClose(client: IPCClient?, json: JSONObject?) {
        this.client = null
        connected = false
        cancelTimer()
    }

    override fun onDisconnect(client: IPCClient?, t: Throwable?) {
        this.client = null
        connected = false
        cancelTimer()
    }

    private fun cancelTimer() {
        if (updateTimer != null) {
            updateTimer!!.cancel()
            updateTimer = null
        }
    }


}