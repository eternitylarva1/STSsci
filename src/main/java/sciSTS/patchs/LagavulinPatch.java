package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import sciSTS.cards.red.LagaEscapeCard;

/**
 * 乐加补丁 - 睡眠时给予逃跑卡牌
 * 当乐加处于睡眠状态时，给予玩家一张逃跑卡牌
 */
@SpirePatch(
        clz = Lagavulin.class,
        method = "usePreBattleAction"
)
public class LagavulinPatch {

    @SpirePostfixPatch
    public static SpireReturn PostFix(Lagavulin __instance, boolean ___asleep) {
        // 如果乐加睡觉，给一张逃跑卡
        if (___asleep) {
            AbstractCard escapeCard = new LagaEscapeCard();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(escapeCard));
        }
        return SpireReturn.Continue();
    }
}
