package click.seichi.petra.stage.summoner

import click.seichi.petra.stage.summon.SummonProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
interface ISummoner {
    fun summon(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID>
    fun summonOnly(world: World, summonProxy: SummonProxy, players: Set<UUID>): Set<UUID>
}