package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.*
import org.bukkit.util.Consumer
import java.util.*

/**
 * @author tar0ss
 */
open class Raider(val entityType: EntityType) : StageEntity {

    override fun spawn(world: World, spawnProxy: SpawnProxy, players: Set<UUID>) {
        (1..calcNumSpawns(players.count())).forEach { _ ->
            val entity = spawnProxy.spawn(world, entityType, Consumer {
                val livingEntity = it as LivingEntity
                livingEntity.removeWhenFarAway = false
                onCreate(it)
            })
            if (entity is Monster) {
                val targetPlayer = getNearestPlayer(entity, players)
                if (targetPlayer != null) {
                    entity.target = targetPlayer
                }
            }
            onSpawned(entity)
        }
    }

    fun getNearestPlayer(entity: Entity, players: Set<UUID>): Player? {
        return players.mapNotNull { Bukkit.getServer().getPlayer(it) }
                .map { it to it.location.distanceSquared(entity.location) }
                .minBy { it.second }?.first
    }

    open fun onCreate(entity: Entity) {}
    open fun onSpawned(entity: Entity) {}

    protected open fun calcNumSpawns(playerCount: Int): Int {
        return playerCount
    }
}