package click.seichi.petra.stage

import io.reactivex.Observable

/**
 * @author tar0ss
 */
interface Wave {
    val canStart: Boolean
    fun start()
    fun endAsObservable(): Observable<Unit>
}