package me.a0g.hyk.core.handlers

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraftforge.fml.common.FMLLog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object APIHandler {

    fun getResponse(urlString: String?): JsonObject {
        try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                var input: String?
                val response = StringBuilder()
                while (`in`.readLine().also { input = it } != null) {
                    response.append(input)
                }
                `in`.close()
                val gson = Gson()
                return gson.fromJson(response.toString(), JsonObject::class.java)
            }
        } catch (e: IOException) {
            FMLLog.getLogger().error(e)
            System.err.println(e)
        }
        return JsonObject()
    }

    fun getUUID(username: String): String {
        val gson = Gson()
        val uuidResponse = APIHandler.getResponse("https://api.mojang.com/users/profiles/minecraft/$username")
        return uuidResponse["id"].asString
    }

}