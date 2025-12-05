package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import static sciSTS.modcore.SciSTS.getCultistAmountToFleet;

/**
 * 逃跑行为补丁 - 处理怪物逃跑逻辑
 * 1. 跟踪邪教徒逃跑次数
 * 2. 记录逃跑的怪物（除掠夺者/抢劫者外）
 */
@SpirePatch(
        clz = EscapeAction.class,
        method = "update"
)
public class EscapeActionPatch {

    @SpireInsertPatch(rloc = 0)
    public static SpireReturn Prefix(EscapeAction __instance, float ___duration) {
        if (___duration == 0.5F) {
            AbstractMonster m = (AbstractMonster) __instance.source;

            // 在Boss房间中跟踪邪教徒逃跑
            if (m instanceof Cultist && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                getCultistAmountToFleet++;
            }

            // 记录非掠夺者/抢劫者类怪物的逃跑（用于战后返回）
            if (!(m instanceof Looter) && !(m instanceof Mugger)) {
                if (AbstractDungeon.getCurrRoom().monsters.monsters.contains(m)) {
                    MonsterTrackingPatch.addEscapedMonster(m);
                }
            }
        }
        return SpireReturn.Continue();
    }
}
