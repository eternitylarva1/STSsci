package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.Pear;

/**
 * 梨遗物限制补丁 - 只对机器人(DEFECT)生效
 */
@SpirePatch(
        clz = Pear.class,
        method = "onEquip"
)
public class PearPatch {

    @SpirePrefixPatch
    public static SpireReturn Prefix(Pear __instance) {
        // 只对机器人角色生效，其他角色跳过
        if (CardCrawlGame.chosenCharacter == AbstractPlayer.PlayerClass.DEFECT) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
