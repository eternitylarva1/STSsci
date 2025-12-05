package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.potions.SmokeBomb;

/**
 * 抢劫者补丁 - 逃跑时掉落烟雾弹
 */
@SpirePatch(
        clz = Mugger.class,
        method = "die"
)
public class MuggerPatch {

    @SpirePrefixPatch
    public static SpireReturn Prefix(Mugger __instance) {
        // 如果抢劫者使用逃跑技能（nextMove=3）时死亡，掉落烟雾弹
        if (__instance.nextMove == 3) {
            AbstractDungeon.getCurrRoom().addPotionToRewards(new SmokeBomb());
        }
        return SpireReturn.Continue();
    }
}
