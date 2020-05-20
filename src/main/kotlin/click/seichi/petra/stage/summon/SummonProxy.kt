package click.seichi.petra.stage.summon

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
interface SummonProxy {
    fun summon(world: World, entityType: EntityType, function: Consumer<Entity>? = null): Entity
}