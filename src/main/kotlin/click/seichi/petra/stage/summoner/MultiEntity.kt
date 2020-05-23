package click.seichi.petra.stage.summoner

import click.seichi.petra.stage.summon.SummonProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
class MultiEntity(vararg pairs: Pair<ISummoner, Int>) : ISummoner {

    private val entityPairList = listOf(*pairs)

    override fun summon(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID> {
        return entityPairList.flatMap { (entity, n) ->
            (1..n).flatMap { _ ->
                entity.summon(world, summonProxy, players)
            }
        }.toSet()
    }

    override fun summonOnly(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID> {
        throw RuntimeException("can't summon only one on MultiEntity")
    }

}