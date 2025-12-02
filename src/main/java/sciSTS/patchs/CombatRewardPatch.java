package sciSTS.patchs;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import java.util.HashMap;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward"
)
public class CombatRewardPatch {
    public static HashMap<String,String> MonsterReward;
    public CombatRewardPatch() {
    }
@SpirePostfixPatch
    public static void Postfix(CombatRewardScreen __instance) {


    }

}