package click.seichi.petra

import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.util.Random
import org.bukkit.ChatColor

/**
 * @author tar0ss
 */
object GameMessage {

    val JOIN = TitleMessage(
            "",
            "${ChatColor.AQUA}/ready で参加",
            stay = 20 * 60
    )

    val READY: (Int, Int) -> Message = { ready: Int, all: Int ->
        TitleMessage(
                "${ChatColor.WHITE}$ready / $all",
                "${ChatColor.AQUA}/ready で参加",
                stay = 20 * 60
        )
    }

    val CANCEL_READY: (Int, Int) -> Message = { ready: Int, all: Int ->
        TitleMessage(
                "${ChatColor.WHITE}$ready / $all",
                "${ChatColor.AQUA}/ready でキャンセル",
                stay = 20 * 60
        )
    }

    val COUNT: (Int) -> Message = { remainSeconds: Int ->
        TitleMessage(
                "${Random.nextChatColor()}$remainSeconds",
                "",
                fadeIn = 5,
                stay = 10,
                fadeOut = 10
        )
    }

    val START: Message = TitleMessage(
            "${ChatColor.YELLOW}スタート", ""
    )
}