package click.seichi.petra.stage.spawn

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
interface SpawnProxy {
    fun spawn(world: World, entityType: EntityType, function: Consumer<Entity>? = null)
}