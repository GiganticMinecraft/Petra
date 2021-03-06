package click.seichi.petra.stage.summon

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.util.Consumer
import java.util.*

/**
 * @author tar0ss
 */
interface SummonProxy {
    fun summonAtDangerZone(world: World, entityType: EntityType, function: Consumer<Entity>? = null): Entity
    fun summonAtSafeZone(world: World, entityType: EntityType, function: Consumer<Entity>? = null): Entity
    fun summonToCenter(world: World, entityType: EntityType, function: Consumer<Entity>? = null): Entity
    fun summonNearPlayer(world: World, entityType: EntityType, players: Set<UUID>, function: Consumer<Entity>? = null): Entity
}