package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * AbstractRoom调试补丁 - 检查房间和战斗奖励逻辑
 */
@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class AbstractRoomDebugPatch {

    @SpirePostfixPatch
    public static void Postfix(AbstractRoom __instance) {
        // 检查战斗结束时的关键逻辑
        if (__instance.phase == AbstractRoom.RoomPhase.COMBAT && __instance.isBattleOver) {
            System.out.println("=== 战斗结束检查 ===");
            System.out.println("isBattleOver: " + __instance.isBattleOver);
            System.out.println("monsters areMonstersDead: " + (__instance.monsters != null ? __instance.monsters.areMonstersDead() : "null"));
            System.out.println("rewardAllowed: " + __instance.rewardAllowed);
            System.out.println("rewardTime: " + __instance.rewardTime);
            System.out.println("creen: " + AbstractDungeon.screen);
        }

        // 检查rewardTime被设置时
        if (__instance.rewardTime && !AbstractDungeon.screen.equals(AbstractDungeon.CurrentScreen.COMBAT_REWARD)) {
            System.out.println("=== RewardTime检查 ===");
            System.out.println("rewardTime=true但屏幕不是COMBAT_REWARD");
            System.out.println("mugged: " + __instance.mugged);
            System.out.println("smoked: " + __instance.smoked);
            System.out.println("rewards数量: " + __instance.rewards.size());
        }
    }
}