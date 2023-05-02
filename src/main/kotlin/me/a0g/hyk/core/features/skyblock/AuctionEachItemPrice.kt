package me.a0g.hyk.core.features.skyblock

import me.a0g.hyk.HykX
import me.a0g.hyk.utils.HypixelUtil
import me.a0g.hyk.utils.TextUtil
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.lang3.StringUtils
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

class AuctionEachItemPrice {

    @SubscribeEvent
    fun onItemTooltip(e: ItemTooltipEvent) {

        if (e.itemStack == null || e.toolTip == null || e.entityPlayer == null) {
            return
        }

        //Price for each item auction
        if (HypixelUtil.checkForSkyBlock() && HykX.config.sbPriceEach) {
            val itemAmount = e.itemStack.stackSize
            if (itemAmount > 1) {
                val toolTip = e.toolTip
                for (i in 1 until toolTip.size) {
                    val line = toolTip[i]
                    val unformatedLine = TextUtil.stripColor(line)
                    if (unformatedLine != null) {
                        if (unformatedLine.startsWith("Buy it now: ")) {
                            try {
                                val numberFormatter = NumberFormat.getNumberInstance(Locale.US)
                                numberFormatter.maximumFractionDigits = 0
                                val price =
                                    numberFormatter.parse(StringUtils.substringBetween(unformatedLine, ": ", " coins"))
                                        .toLong()
                                val priceEach = price / itemAmount.toDouble()
                                val priceEachString: String = TextUtil.niceNumber(priceEach.toInt())
                                val pricePerItem = " §e($priceEachString) each"
                                toolTip[i] = line + pricePerItem
                                return
                            } catch (ex: ParseException) {
                                return
                            }
                        }
                    }
                }
            }
        }
    }

}