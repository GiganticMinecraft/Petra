package click.seichi.petra.stage.raider

import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * @author tar0ss
 */
interface AutoTarget {
    fun findTarget(self: LivingEntity, players: Set<UUID>): LivingEntity?
}