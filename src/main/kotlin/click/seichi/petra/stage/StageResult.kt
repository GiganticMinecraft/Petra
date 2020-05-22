package click.seichi.petra.stage

import click.seichi.message.Message
import click.seichi.message.TitleMessage
import click.seichi.petra.GameSound

/**
 * @author tar0ss
 */
enum class StageResult(val message: Message) {
    WIN(TitleMessage("勝利!!", "").add(GameSound.WIN)),
    DEATH_ALL_PLAYERS(TitleMessage("敗北", "全滅してしまった").add(GameSound.LOSE)),
    DEATH_TARGET_PLAYERS(TitleMessage("敗北", "守り切れなかった").add(GameSound.LOSE)),
    OVER_THE_TIME_LIMIT(TitleMessage("敗北", "時間切れ").add(GameSound.LOSE))
}