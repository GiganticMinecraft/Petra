package click.seichi.cache.key

import click.seichi.cache.Cache

/**
 * @author tar0ss
 */
interface DatabaseKey<S : Cache<S>, V, E> : Key<S, V> {
    fun read(entity: E): V

    fun store(entity: E, value: V)
}