package click.seichi.petra.event

import org.bukkit.event.HandlerList
import java.util.*

/**
 * @author tar0ss
 */
class StartGameEvent(
        val playerSet: Set<UUID>
) : CustomEvent() {
    companion object {
        @JvmStatic
        private val handler = HandlerList()

        @JvmStatic
        fun getHandlerList() = handler
    }

    override fun getHandlers() = handler
}