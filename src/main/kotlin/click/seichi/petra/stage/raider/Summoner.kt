package click.seichi.petra.stage.raider

import click.seichi.petra.stage.summon.SummonProxy
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.util.Consumer
import java.util.*

/**
 * @author tar0ss
 */
open class Summoner(val entityType: EntityType) : ISummoner {

    override fun summon(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID> {
        return (1..calcNumSpawns(players.count())).map { _ ->
            val entity = summonProxy.summon(world, entityType, Consumer {
                val livingEntity = it as LivingEntity
                livingEntity.removeWhenFarAway = false
                onCreate(it)
            })
            if (entity is Mob && this is AutoTarget) {
                findTarget(entity, players)?.let { entity.target = it }
            }
            if (this is Named) {
                entity.isCustomNameVisible = true
                entity.customName = getName()
            }
            onSummoned(entity)
            entity.uniqueId
        }.toSet()
    }


    open fun onCreate(entity: Entity) {}
    open fun onSummoned(entity: Entity) {}

    protected open fun calcNumSpawns(playerCount: Int): Int {
        return playerCount
    }
}