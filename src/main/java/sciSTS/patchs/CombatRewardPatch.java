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
        System.out.println("=== CombatRewardPatch 开始处理额外奖励 ===");

        // 打印当前所有配置的掉落关系
        System.out.println("当前配置的掉落关系:");
        for (Map.Entry<String, List<String>> entry : MonsterReward.entrySet()) {
            String rewardId = entry.getKey();
            List<String> monsterIds = entry.getValue();
            System.out.println("  遗物[" + rewardId + "] 可由怪物掉落: " + monsterIds);
        }

        MonsterGroup monsterGroup = MonsterHelper.getEncounter(AbstractDungeon.lastCombatMetricKey);
        System.out.println("当前战斗怪物数量: " + monsterGroup.monsters.size());

        // 在设置战斗奖励后，检查是否有怪物需要额外奖励
        boolean addedAnyReward = false;
        for (AbstractMonster monster : monsterGroup.monsters) {
            String monsterId = monster.id;
            System.out.println("检查怪物: " + monsterId + " (是否存活: " + !monster.isDead + ")");

            // 遍历所有奖励配置，检查当前怪物是否匹配任何奖励配置
            for (Map.Entry<String, List<String>> entry : MonsterReward.entrySet()) {
                String rewardKey = entry.getKey();  // 奖励ID作为key
                List<String> monsterIds = entry.getValue();  // 怪物ID列表作为value

                if (monsterIds.contains(monsterId)) {
                    System.out.println("  ✓ 怪物 " + monsterId + " 匹配到遗物掉落配置: " + rewardKey);

                    // 根据奖励键值创建对应的额外奖励
                    if (RelicLibrary.getRelic(rewardKey) != null) {
                        RewardItem extraReward = new RewardItem(RelicLibrary.getRelic(rewardKey).makeCopy());
                        __instance.rewards.add(extraReward);
                        System.out.println("  → 成功为怪物 " + monsterId + " 添加额外奖励: " + rewardKey);
                        addedAnyReward = true;
                    } else {
                        System.err.println("  ✗ 错误: 遗物 " + rewardKey + " 不存在于遗物库中!");
                    }
                } else {
                    System.out.println("  - 怪物 " + monsterId + " 不匹配遗物 " + rewardKey + " 的掉落列表");
                }
            }
        }

        System.out.println("战斗奖励处理完成, 是否添加了额外奖励: " + addedAnyReward);
        System.out.println("当前奖励总数: " + __instance.rewards.size());
        System.out.println("=== CombatRewardPatch 处理结束 ===\n");
    }




}