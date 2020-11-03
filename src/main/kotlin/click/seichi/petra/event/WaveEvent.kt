package click.seichi.petra.event

import org.bukkit.event.HandlerList

/**
 * @author tar0ss
 */
class WaveEvent(val i: Int) : CustomEvent() {
    companion object {
        @JvmStatic
        private val handler = HandlerList()

        @JvmStatic
        fun getHandlerList() = handler
    }

    override fun getHandlers() = handler
}