package sciSTS.patchs;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import sciSTS.cards.red.jiaoxie;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward"
)
public class CombatRewardPatch {
    public static HashMap<String,List<String>> MonsterReward = new HashMap<>();
    public CombatRewardPatch() {
    }
@SpirePostfixPatch
    public static void Postfix(CombatRewardScreen __instance) {
        MonsterGroup monsterGroup = MonsterHelper.getEncounter(AbstractDungeon.lastCombatMetricKey);

        // 检查是否使用了缴械卡，如果没使用则不执行额外奖励逻辑
        if (!jiaoxie.dropWeapons) {
            return;
        }

        // 在设置战斗奖励后，检查是否有怪物需要额外奖励
        for (AbstractMonster monster : monsterGroup.monsters) {
            String monsterId = monster.id;

            // 遍历所有奖励配置，检查当前怪物是否匹配任何奖励配置
            for (Map.Entry<String, List<String>> entry : MonsterReward.entrySet()) {
                String rewardKey = entry.getKey();  // 奖励ID作为key
                List<String> configuredMonsterIds = entry.getValue();  // 怪物ID列表作为value

                if (configuredMonsterIds.contains(monsterId)) {
                    // 根据奖励键值创建对应的额外奖励
                    if (RelicLibrary.getRelic(rewardKey) != null) {
                        RewardItem extraReward = new RewardItem(RelicLibrary.getRelic(rewardKey).makeCopy());
                        __instance.rewards.add(extraReward);
                        System.out.println("为怪物 " + monsterId + " 添加额外奖励: " + rewardKey);
                    }
                    break;  // 每个怪物只匹配一个奖励
                }
            }
        }

        // 重置标记，下次战斗默认不掉落武器
        jiaoxie.dropWeapons = false;
    }




}