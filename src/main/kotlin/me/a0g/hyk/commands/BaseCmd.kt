package me.a0g.hyk.commands

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

abstract class BaseCmd(private val name: String, private val aliases: List<String> = emptyList()) : CommandBase() {

    final override fun getCommandName(): String = name
    final override fun getCommandAliases(): List<String> = aliases
    final override fun getRequiredPermissionLevel() = 0

    open fun getCommandUsage(player: EntityPlayerSP): String = "/$commandName"

    abstract fun processCommand(player: EntityPlayerSP, args: Array<String>)

    final override fun processCommand(sender: ICommandSender, args: Array<String>) =
        processCommand(sender as EntityPlayerSP, args)

    final override fun getCommandUsage(sender: ICommandSender) =
        getCommandUsage(sender as EntityPlayerSP)

    fun getPossibleCompilation(compilations:  List<String>,args: Array<String>): MutableList<String> {
        val lastArg = args.last()
        val possible = mutableListOf<String>()
        for(comp in compilations){
            if (comp.startsWith(lastArg)) {
                possible.add(comp)
            }
        }
        return possible
    }
}