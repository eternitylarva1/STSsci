package MonsterModifier.patchs;

import MonsterModifier.Modifiers.AbstractMonsterModifier;
import MonsterModifier.Modifiers.MonsterModifierManager;
import MonsterModifier.Modifiers.WeakModifier;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static MonsterModifier.Modifiers.MonsterModifierManager.modifiers;
import static com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.BOSS;
import static com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.ELITE;

public class AddModifierPatch {
    @SpirePatch(clz = SpawnMonsterAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractMonster.class, boolean.class, int.class})
    public static class BurnPatch1
    {
        @SpirePostfixPatch
        public static SpireReturn<Boolean> Insert(SpawnMonsterAction __instance,AbstractMonster m, boolean isMinion, int slot) {
            MonsterModifierManager.addModifier(m, new WeakModifier());
            return SpireReturn.Continue();
        }
    }/*
    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { String.class, String.class, int.class, float.class, float.class, float.class, float.class, String.class, float.class, float.class, boolean.class})
    public static class AddModifier2
    {
        @SpirePostfixPatch
        public static SpireReturn<Boolean> Insert(AbstractMonster __instance,String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
            MonsterModifierManager.addModifier(__instance, new WeakModifier());
            return SpireReturn.Continue();
        }
    }*/
    @SpirePatch(
            cls = "com.megacrit.cardcrawl.monsters.AbstractMonster",
            method = "renderTip"
    )
    public static class RenderTipPatch {
        @SpireInsertPatch(
                loc = 308
        )
        public static SpireReturn Insert(AbstractCreature __instance, SpriteBatch sb, ArrayList ___tips) throws NoSuchFieldException, IllegalAccessException {
          if(__instance instanceof AbstractMonster) {
              for (AbstractMonsterModifier mod : modifiers((AbstractMonster) __instance)) {
                  mod.addTips( (AbstractMonster) __instance);
              }
          }
            return SpireReturn.Continue();
        }
    }


}
