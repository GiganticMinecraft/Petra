package click.seichi.petra.util

import click.seichi.petra.function.sync
import click.seichi.petra.function.warning

/**
 * @author tar0ss
 */
open class Timer(
        private val finishSeconds: Int,
        private val onStart: () -> Unit = {},
        /**
         * ex) [finishSeconds] = 5の時
         * i = 5,4,3,2,1,0
         */
        private val onNext: (Int) -> Unit = {},
        private val onComplete: () -> Unit = {},
        private val onCancelled: () -> Unit = {}
) {

    var isStarted = false
        private set
    var isCancelled = false
        private set

    fun start() {
        if (isStarted) {
            warning("Timer is already started.")
            return
        }
        isStarted = true
        isCancelled = false
        onStart()

        sync(0L, 20L) { elapsedSeconds ->
            if (isCancelled) {
                isStarted = false
                onCancelled()
                return@sync false
            }

            val remainSeconds = finishSeconds - elapsedSeconds.toInt()
            onNext(remainSeconds)

            if (remainSeconds <= 0) {
                isStarted = false
                onComplete()
                return@sync false
            }
            return@sync true
        }
    }

    fun cancel() {
        isCancelled = true
    }

}