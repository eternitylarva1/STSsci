package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
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

    } @SpirePatch(
            clz = Strawberry.class,
            method = "onEquip"
    )
    public static class StrawberryPatch {

        @SpirePrefixPatch
        public static SpireReturn Prefix(Strawberry  __instance) {
            if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();

        }

    } @SpirePatch(
            clz = Mango.class,
            method = "onEquip"
    )
    public static class MangoPatch {

        @SpirePrefixPatch
        public static SpireReturn Prefix(Mango  __instance) {
            if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();

        }

    }
}
