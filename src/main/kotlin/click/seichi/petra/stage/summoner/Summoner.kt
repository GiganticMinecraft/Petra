package click.seichi.petra.stage.summoner

import click.seichi.petra.stage.summon.SummonProxy
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Consumer
import java.util.*

/**
 * @author tar0ss
 */
open class Summoner(val entityType: EntityType) : ISummoner {

    override fun summon(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID> {
        return (1..players.count()).map { _ ->
            val entity = summonProxy.summon(world, entityType, Consumer {
                val livingEntity = it as LivingEntity
                livingEntity.removeWhenFarAway = false
                onCreate(it)
            })
            if (this is Named) {
                // カーソル合わせた時に見えればよい
                entity.isCustomNameVisible = false
                entity.customName = getName()
            }
            onSummoned(entity)
            entity.uniqueId
        }.toSet()
    }

    open fun onCreate(entity: Entity) {}
    open fun onSummoned(entity: Entity) {}
}