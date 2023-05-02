package me.a0g.hyk.utils

import java.io.File
import java.io.IOException

object Util {

    /**
     * Get java runtime
     *
     * @throws IOException
     * @return path to java runtime
     */
    @Throws(IOException::class)
    fun getJavaRuntime(): String? {
        val os = System.getProperty("os.name")
        val java = System.getProperty("java.home") + File.separator + "bin" + File.separator +
                if (os != null && os.lowercase().startsWith("windows")) "java.exe" else "java"
        if (!File(java).isFile) {
            return null
//            throw IOException("Unable to find suitable java runtime at $java")
        }
        return java
    }


}