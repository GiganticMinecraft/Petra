package click.seichi.petra.function

import click.seichi.petra.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * @author tar0ss
 */

/**
 * [BukkitRunnable.runTask]の簡略化版
 *
 * @param f 処理
 * @return task BukkitTask
 */
fun Any.sync(f: () -> Unit): BukkitTask {
    return object : BukkitRunnable() {
        override fun run() {
            f()
        }
    }.runTask(Plugin.INSTANCE)
}

/**
 * [BukkitRunnable.runTaskLater]の簡略化版
 *
 * @param delayTicks 遅延時間(ticks)
 * @param f 処理
 * @return task BukkitTask
 */
fun Any.sync(delayTicks: Long, f: () -> Unit): BukkitTask {
    return object : BukkitRunnable() {
        override fun run() {
            f()
        }
    }.runTaskLater(Plugin.INSTANCE, delayTicks)
}

/**
 * [BukkitRunnable.runTaskTimer]の簡略化版
 *
 * @param delayTicks 遅延時間(ticks)
 * @param repeatingTicks 繰り返し間隔(ticks)
 * @param f 処理 引数:実行番号(0から) 返り値: false->キャンセル
 * @return task BukkitTask
 */
fun Any.sync(delayTicks: Long, repeatingTicks: Long, f: (Long) -> Boolean): BukkitTask {
    var count = 0L
    return object : BukkitRunnable() {
        override fun run() {
            if (!f(count++)) cancel()
        }
    }.runTaskTimer(Plugin.INSTANCE, delayTicks, repeatingTicks)
}

/**
 * [BukkitRunnable.runTaskAsynchronously]の簡略化版
 *
 * @param f 処理
 * @return task BukkitTask
 */
fun Any.async(f: () -> Unit): BukkitTask {
    return object : BukkitRunnable() {
        override fun run() {
            f()
        }
    }.runTaskAsynchronously(Plugin.INSTANCE)
}

/**
 * [BukkitRunnable.runTaskLaterAsynchronously]の簡略化版
 *
 * @param delayTicks 遅延時間(ticks)
 * @param f 処理
 * @return task BukkitTask
 */
fun Any.async(delayTicks: Long, f: () -> Unit): BukkitTask {
    return object : BukkitRunnable() {
        override fun run() {
            f()
        }
    }.runTaskLaterAsynchronously(Plugin.INSTANCE, delayTicks)
}

/**
 * [BukkitRunnable.runTaskTimerAsynchronously]の簡略化版
 *
 * @param delayTicks 遅延時間(ticks)
 * @param repeatingTicks 繰り返し間隔(ticks)
 * @param f 処理 引数:実行番号(0から) 返り値: false->キャンセル
 * @return task BukkitTask
 */
fun Any.async(delayTicks: Long, repeatingTicks: Long, f: (Long) -> Boolean): BukkitTask {
    var count = 0L
    return object : BukkitRunnable() {
        override fun run() {
            if (!f(count++)) cancel()
        }
    }.runTaskTimerAsynchronously(Plugin.INSTANCE, delayTicks, repeatingTicks)
}
