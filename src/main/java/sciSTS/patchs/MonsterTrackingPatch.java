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
        // 添加调试日志
        System.out.println("MonsterTrackingPatch.PostFix调用 - 检查怪物状态");
        if (__instance.monsters != null) {
            System.out.println("怪物死亡状态: " + __instance.monsters.areMonstersDead());
            System.out.println("怪物基本死亡状态: " + __instance.monsters.areMonstersBasicallyDead());
            System.out.println("房间阶段: " + __instance.phase);
        }

        // 如果所有怪物死亡，将逃跑的怪物重新加入战斗
        if (__instance.monsters != null && __instance.monsters.areMonstersDead()) {
            System.out.println("检测到怪物死亡，开始处理逃跑怪物返回逻辑");
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    escapedMonsters.forEach(monster -> {
                        if (monster != null && !monster.isDead && !monster.isDying) {
                            monster.currentHealth = monster.maxHealth;
                            AbstractDungeon.getCurrRoom().monsters.addMonster(monster);
                            System.out.println("重新添加逃跑怪物: " + monster.id);
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
