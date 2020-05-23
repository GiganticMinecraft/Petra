package click.seichi.petra.coroutine

import click.seichi.petra.Plugin
import kotlin.coroutines.CoroutineContext

/**
 * @author tar0ss
 */
object DispatcherContainer {
    /**
     * Gets the async coroutine context.
     */
    val async: CoroutineContext by lazy {
        AsyncCoroutineDispatcher(Plugin.INSTANCE)
    }

    /**
     * Gets the sync coroutine context.
     */
    val sync: CoroutineContext by lazy {
        MinecraftCoroutineDispatcher(Plugin.INSTANCE)
    }
}