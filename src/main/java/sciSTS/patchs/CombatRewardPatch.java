package sciSTS.patchs;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import java.util.HashMap;
import java.util.Map;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward"
)
public class CombatRewardPatch {
    public static HashMap<String,String> MonsterReward = new HashMap<>();
    public CombatRewardPatch() {
    }
@SpirePostfixPatch
    public static void Postfix(CombatRewardScreen __instance) {
        // 在设置战斗奖励后，检查是否有怪物需要额外奖励
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.isDead || monster.isDying || monster.isEscaping) {
                    String monsterId = monster.id;
                    String rewardKey = MonsterReward.get(monsterId);

                    if (rewardKey != null) {
                        // 根据奖励键值创建对应的额外奖励
                        RewardItem extraReward = new RewardItem(RelicLibrary.getRelic(rewardKey).makeCopy());
                        if (extraReward != null) {
                            __instance.rewards.add(extraReward);
                        }
                    }
                }
            }
        }
    }




}