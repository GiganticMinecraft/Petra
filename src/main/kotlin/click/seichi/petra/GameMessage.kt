package click.seichi.petra

import click.seichi.message.Message
import click.seichi.message.TitleMessage
import click.seichi.util.Random
import org.bukkit.ChatColor

/**
 * @author tar0ss
 */
object GameMessage {
    val READY: (Int, Int) -> Message = { ready: Int, all: Int ->
        TitleMessage(
                "${ChatColor.WHITE} $ready / $all",
                "${ChatColor.BLUE}/r で じゅんび",
                stay = 20 * 60
        )
    }

    val CANCEL_READY: (Int, Int) -> Message = { ready: Int, all: Int ->
        TitleMessage(
                "${ChatColor.WHITE} $ready / $all",
                "${ChatColor.BLUE}/r で キャンセル",
                stay = 20 * 60
        )
    }

    val START_COUNT: (Int) -> Message = { remainSeconds: Int ->
        TitleMessage(
                "${Random.nextChatColor()}$remainSeconds",
                "${ChatColor.RED}/r で キャンセル",
                fadeIn = 5,
                stay = 10,
                fadeOut = 10
        )
    }
    val START_GAME: Message = TitleMessage(
            "${ChatColor.YELLOW}スタート",
            ""
    )
}