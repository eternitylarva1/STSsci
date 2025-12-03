package sciSTS.patchs;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
                        RewardItem extraReward = createExtraReward(rewardKey, monster);
                        if (extraReward != null) {
                            __instance.rewards.add(extraReward);
                        }
                    }
                }
            }
        }
    }

    // 根据奖励键值创建额外奖励
    private static RewardItem createExtraReward(String rewardKey, AbstractMonster monster) {
        if (rewardKey == null) {
            return null;
        }

        switch (rewardKey) {
            case "COMMON_RELIC":
                return new RewardItem();
            case "GOLD_SMALL":
                return new RewardItem(25);
            case "GOLD_MEDIUM":
                return new RewardItem(50);
            case "GOLD_LARGE":
                return new RewardItem(75);
            case "POTION":
                return new RewardItem(RewardType.POTION, null);
            case "CARD_COMMON":
                return new RewardItem();
            case "CARD_UNCOMMON":
                return new RewardItem();
            case "CARD_RARE":
                return new RewardItem();
            default:
                return null;
        }
    }

}