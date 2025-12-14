package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.ArrayList;

/**
 * 怪物追踪补丁 - 跟踪逃跑的怪物并在战后返回
 */
@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class MonsterTrackingPatch {

    private static ArrayList<AbstractMonster> escapedMonsters = new ArrayList<>();

    @SpirePostfixPatch
    public static void PostFix(AbstractRoom __instance) {
    return ;
    }

    /**
     * 添加逃跑的怪物到追踪列表
     */
    public static void addEscapedMonster(AbstractMonster monster) {
        escapedMonsters.add(monster);
    }
}
