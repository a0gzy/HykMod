package me.a0g.hyk.utils

import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.roundToInt

object TextUtil {

    private val USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]+")
    private val STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]")


    /**
     * Reverses a given text while leaving the english parts intact and in order.
     * (Maybe its more complicated than it has to be, but it gets the job done.)
     *
     * @param originalText Input text
     * @return Reversed input text
     */
    fun reverseText(originalText: String): String? {
        val newString = StringBuilder()
        val parts = originalText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in parts.size downTo 1) {
            val textPart = parts[i - 1]
            var foundCharacter = false
            for (letter in textPart.toCharArray()) {
                if (letter.code > 191) { // Found special character
                    foundCharacter = true
                    newString.append(StringBuilder(textPart).reverse().toString())
                    break
                }
            }
            newString.append(" ")
            if (!foundCharacter) {
                newString.insert(0, textPart)
            }
            newString.insert(0, " ")
        }
        return removeDuplicateSpaces(newString.toString().trim { it <= ' ' })
    }

    /**
     * Removes any duplicate spaces from a given text.
     *
     * @param text Input text
     * @return Input text without repeating spaces
     */
    fun removeDuplicateSpaces(text: String): String {
        return text.replace("\\s+".toRegex(), " ")
    }

    /**
     * Strips color codes from a given text
     *
     * @param input Text to strip colors from
     * @return Text without color codes
     */
    fun stripColor(input: String?): String? {
        return input?.let { STRIP_COLOR_PATTERN.matcher(it).replaceAll("") }
    }

    /**
     * Checks if text matches a Minecraft username
     *
     * @param input Text to check
     * @return Whether this input can be Minecraft username or not
     */
    fun isUsername(input: String?): Boolean {
        return USERNAME_PATTERN.matcher(input).matches()
    }

    /**
     * Converts number to abbreviation
     * - 123525 is 123.3k
     * @author a0g
     *
     * @param[number] input number
     * @return abbreviated number
     */
    fun niceNumber(number: Int): String {
        return if (number >= 1000000000) {
            val num = (number / 100000000).toFloat().roundToInt() / 10.0
            num.toString() + "B"
        } else if (number >= 1000000) {
            val num = (number / 100000).toFloat().roundToInt() / 10.0
            num.toString() + "M"
        } else if (number >= 1000) {
            //  12333     123     12.3k
            val num = (number / 100).toFloat().roundToInt() / 10.0
            num.toString() + "k"
        } else {
            number.toString() + ""
        }
    }

    fun getTimeBetween(timeOne: Double, timeTwo: Double): String {
        val secondsBetween = floor(timeTwo - timeOne)
        val timeFormatted: String
        val days: Int
        val hours: Int
        val minutes: Int
        val seconds: Int
        if (secondsBetween > 86400) {
            // More than 1d, display #d#h
            days = (secondsBetween / 86400).toInt()
            hours = (secondsBetween % 86400 / 3600).toInt()
            minutes = (secondsBetween % 3600 / 60).toInt()
            timeFormatted = days.toString() + "d" + hours + "h" + minutes + "m"
        } else if (secondsBetween > 3600) {
            // More than 1h, display #h#m
            hours = (secondsBetween / 3600).toInt()
            minutes = (secondsBetween % 3600 / 60).toInt()
            timeFormatted = hours.toString() + "h" + minutes + "m"
        } else {
            // Display #m#s
            minutes = (secondsBetween / 60).toInt()
            seconds = (secondsBetween % 60).toInt()
            timeFormatted = minutes.toString() + "m" + seconds + "s"
        }
        return timeFormatted
    }

}