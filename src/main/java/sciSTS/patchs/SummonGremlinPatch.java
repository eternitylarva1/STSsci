package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;

/**
 * 召唤地精补丁 - 20%概率召唤地精贵族
 */
@SpirePatch(
        clz = SummonGremlinAction.class,
        method = "getRandomGremlin"
)
public class SummonGremlinPatch {

    @SpireInsertPatch(rloc = 3)
    public static SpireReturn Prefix(SummonGremlinAction __instance, int slot) {
        float x, y;
        // 20%概率召唤地精贵族而非普通地精
        if (AbstractDungeon.aiRng.randomBoolean(0.2F)) {
            // 根据位置槽位计算坐标
            switch (slot) {
                case 0:
                    x = GremlinLeader.POSX[0];
                    y = GremlinLeader.POSY[0];
                    break;
                case 1:
                    x = GremlinLeader.POSX[1];
                    y = GremlinLeader.POSY[1];
                    break;
                case 2:
                    x = GremlinLeader.POSX[2];
                    y = GremlinLeader.POSY[2];
                    break;
                default:
                    x = GremlinLeader.POSX[0];
                    y = GremlinLeader.POSY[0];
            }

            // 召唤地精贵族
            GremlinNob gremlinNob = new GremlinNob(x, y);
            return SpireReturn.Return(gremlinNob);
        }

        return SpireReturn.Continue();
    }
}
