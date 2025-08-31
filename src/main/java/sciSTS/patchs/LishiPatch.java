package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.relics.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class LishiPatch {
    @SpirePatch(
            clz = StoneCalendar.class,
            method = "atTurnStart"
   )
    public static class LishidePatch {

        @SpirePrefixPatch
        public static void Prefix(StoneCalendar  __instance) {
if (__instance.counter==0){
    LocalDate today = LocalDate.now();
    DayOfWeek dayOfWeek = today.getDayOfWeek();
    __instance.counter+=dayOfWeek.getValue();
    if ( __instance.counter>=7){
        __instance.counter=7;
    }
}

        }

    }   @SpirePatch(
            clz = Pear.class,
            method = "onEquip"
    )
    public static class PearPatch {

        @SpirePrefixPatch
        public static SpireReturn Prefix(Pear  __instance) {
          if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
              return SpireReturn.Return();
          }
            return SpireReturn.Continue();

        }

    }  @SpirePatch(
            clz = ExplosivePower.class,
            method = "duringTurn"
    )
    public static class ExplosivePatch {

        @SpireInsertPatch(
                rloc=3
        )
        public static SpireReturn Prefix(ExplosivePower  __instance) {
            if (__instance.amount == 1 && !__instance.owner.isDying) {
                DamageInfo damageInfo = new DamageInfo(__instance.owner, 30, DamageInfo.DamageType.THORNS);
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(damageInfo.base, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
            }
            return SpireReturn.Continue();

        }

    }
}
