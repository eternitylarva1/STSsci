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
        // 如果所有怪物死亡，将逃跑的怪物重新加入战斗
        if (__instance.monsters != null && __instance.monsters.areMonstersDead()) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    escapedMonsters.forEach(monster -> {
                        if (monster != null && !monster.isDead && !monster.isDying) {
                            monster.currentHealth = monster.maxHealth;
                            AbstractDungeon.getCurrRoom().monsters.addMonster(monster);
                        }
                    });
                    this.isDone = true;
                }
            });
        }
    }

    /**
     * 添加逃跑的怪物到追踪列表
     */
    public static void addEscapedMonster(AbstractMonster monster) {
        escapedMonsters.add(monster);
    }
}
