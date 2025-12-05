package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.events.RoomEventDialog;

/**
 * 蒙面强盗补丁 - 无金币时禁用选项
 */
@SpirePatch(
        clz = MaskedBandits.class,
        method = SpirePatch.CONSTRUCTOR
)
public class MaskedBanditsPatch {

    @SpirePostfixPatch
    public static SpireReturn PostFix(MaskedBandits __instance) {
        // 如果玩家金币为0，禁用给钱选项
        if (AbstractDungeon.player.gold == 0) {
            RoomEventDialog.optionList.get(0).isDisabled = true;
            RoomEventDialog.optionList.get(0).msg = "需要：有金币";
        }
        return SpireReturn.Continue();
    }
}
