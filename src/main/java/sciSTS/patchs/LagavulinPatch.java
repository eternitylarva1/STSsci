package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;

/**
 * 乐加补丁 - 睡眠时给予喂食卡牌
 */
@SpirePatch(
        clz = Lagavulin.class,
        method = "usePreBattleAction"
)
public class LagavulinPatch {

    @SpirePostfixPatch
    public static SpireReturn PostFix(Lagavulin __instance, boolean ___asleep) {
        if (___asleep) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Feed()));
        }
        return SpireReturn.Continue();
    }
}
