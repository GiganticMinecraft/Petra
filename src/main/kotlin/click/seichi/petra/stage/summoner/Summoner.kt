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
open class Summoner(
        private val entityType: EntityType,
        private val case: SummonCase = SummonCase.DANGER_ZONE
) : ISummoner {
    enum class SummonCase {
        DANGER_ZONE,
        CENTER
    }

    override fun summon(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID> {
        return (1..players.count()).map { _ ->
            val consumer: Consumer<Entity> = Consumer {
                val livingEntity = it as LivingEntity
                livingEntity.removeWhenFarAway = false
                onCreate(it)
            }
            val entity = when (case) {
                SummonCase.DANGER_ZONE -> summonProxy.summon(world, entityType, consumer)
                SummonCase.CENTER -> summonProxy.summonToCenter(world, entityType, consumer)
            }
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