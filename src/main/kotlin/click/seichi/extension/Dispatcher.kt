package click.seichi.extension

import click.seichi.coroutine.DispatcherContainer
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * @author tar0ss
 */
/**
 * Minecraft async dispatcher.
 */
val Dispatchers.async: CoroutineContext
    get() = DispatcherContainer.async

/**
 * Minecraft sync dispatcher.
 */
val Dispatchers.minecraft: CoroutineContext
    get() = DispatcherContainer.sync