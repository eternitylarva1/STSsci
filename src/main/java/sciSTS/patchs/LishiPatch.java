package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.ForgottenAltar;
import com.megacrit.cardcrawl.events.city.MaskedBandits;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import sciSTS.utils.Invoker;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static sciSTS.modcore.SciSTS.getCultistAmountToFleet;

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
            clz = EscapeAction.class,
            method = "update"
    )
    public static class EscapeActionPatch {

        @SpireInsertPatch(
                rloc=0
        )
        public static SpireReturn Prefix(EscapeAction  __instance,float ___duration) {

            if (___duration== 0.5F) {
                AbstractMonster m = (AbstractMonster)__instance.source;
              if (m instanceof Cultist&&AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
                  getCultistAmountToFleet++;
              }
            }
            return SpireReturn.Continue();
        }

    } @SpirePatch(
            clz = Lagavulin.class,
            method = "usePreBattleAction"
    )
    public static class LagavulinPatch {

        @SpirePostfixPatch
        public static SpireReturn PostFix(Lagavulin  __instance,boolean ___asleep) {

            if (___asleep) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Feed()));
            }
            return SpireReturn.Continue();
        }

    }
    @SpirePatch(
            clz = Looter.class,
            method = "die"
    )
    public static class LooterPatch {

        @SpirePrefixPatch
        public static SpireReturn PostFix(Looter  __instance) {

          if (__instance.nextMove==3){
              AbstractDungeon.getCurrRoom().addPotionToRewards(new SmokeBomb());
          }
            return SpireReturn.Continue();
        }

    }  @SpirePatch(
            clz = Mugger.class,
            method = "die"
    )
    public static class MuggerPatch {

        @SpirePrefixPatch
        public static SpireReturn PostFix(Mugger  __instance) {

          if (__instance.nextMove==3){
              AbstractDungeon.getCurrRoom().addPotionToRewards(new SmokeBomb());
          }
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(
            clz =  MaskedBandits.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class MaskedBanditsPatch {

        @SpirePostfixPatch
        public static SpireReturn PostFix(MaskedBandits  __instance) {
          if (AbstractDungeon.player.gold==0) {
              RoomEventDialog.optionList.get(0).isDisabled = true;
              //TODO 补全事件本地化文本
              RoomEventDialog.optionList.get(0).msg="需要：有金币";
          }
            return SpireReturn.Continue();
        }

        @SpirePatch(
            clz = ShowCardAndObtainEffect.class,
            method = SpirePatch.CONSTRUCTOR,
                paramtypez = {AbstractCard.class, float.class, float.class, boolean.class}

    )
    public static class ShowCardAndObtainEffectPatch {

        @SpireInsertPatch(
                rloc=0
        )
        public static SpireReturn Prefix(ShowCardAndObtainEffect  __instance, AbstractCard card, float x, float y, boolean convergeCards) {
       if (CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
           if ( !AbstractDungeon.player.hasRelic(EmotionChip.ID)) {
                if (card instanceof Doubt || card instanceof Regret|| card instanceof Writhe || card instanceof Pride|| card instanceof Shame) {
                    __instance.duration = 0.0F;
                    __instance.isDone = true;
                    Invoker.setField(__instance, "converge", convergeCards);
                }
           }

       }
            return SpireReturn.Continue();


        }

    }@SpirePatch(
            clz = SummonGremlinAction.class,
            method = "getRandomGremlin"
    )
    public static class SummonGremlinPatch {

        @SpireInsertPatch(
                rloc=3
        )
        public static SpireReturn Prefix(SummonGremlinAction  __instance,int slot) {
            float x,y;
            if (AbstractDungeon.aiRng.randomBoolean(0.2F)){
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
                GremlinNob gremlinNob = new GremlinNob(x, y);
                return SpireReturn.Return(gremlinNob) ;
            }

            return SpireReturn.Continue();


        }

    }
}}
