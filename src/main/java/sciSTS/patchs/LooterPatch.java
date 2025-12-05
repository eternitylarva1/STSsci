package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.potions.SmokeBomb;

/**
 * 小偷补丁 - 逃跑时掉落烟雾弹
 */
@SpirePatch(
        clz = Looter.class,
        method = "die"
)
public class LooterPatch {

    @SpirePrefixPatch
    public static SpireReturn Prefix(Looter __instance) {
        // 如果小偷使用逃跑技能（nextMove=3）时死亡，掉落烟雾弹
        if (__instance.nextMove == 3) {
            AbstractDungeon.getCurrRoom().addPotionToRewards(new SmokeBomb());
        }
        return SpireReturn.Continue();
    }
}
