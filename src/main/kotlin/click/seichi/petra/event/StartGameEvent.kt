package click.seichi.petra.event

import java.util.*

/**
 * @author tar0ss
 */
class StartGameEvent(
        val playerSet: Set<UUID>
) : CustomEvent()