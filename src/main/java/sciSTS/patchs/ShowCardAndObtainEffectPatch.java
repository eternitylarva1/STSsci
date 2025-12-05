package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.EmotionChip;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import sciSTS.utils.Invoker;

/**
 * 获得卡牌效果补丁 - 机器人情感诅咒获取限制
 * 只有拥有EmotionChip遗物才能获得情感类诅咒
 */
@SpirePatch(
        clz = ShowCardAndObtainEffect.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCard.class, float.class, float.class, boolean.class}
)
public class ShowCardAndObtainEffectPatch {

    @SpireInsertPatch(rloc = 0)
    public static SpireReturn Prefix(ShowCardAndObtainEffect __instance, AbstractCard card, float x, float y, boolean convergeCards) {
        // 对于机器人角色，如果没有情感芯片遗物，则跳过多张情感诅咒卡的获得动画
        if (CardCrawlGame.chosenCharacter == AbstractPlayer.PlayerClass.DEFECT) {
            if (!AbstractDungeon.player.hasRelic(EmotionChip.ID)) {
                if (card instanceof Doubt || card instanceof Regret || card instanceof Writhe || card instanceof Pride || card instanceof Shame) {
                    __instance.duration = 0.0F;
                    __instance.isDone = true;
                    Invoker.setField(__instance, "converge", convergeCards);
                }
            }
        }
        return SpireReturn.Continue();
    }
}
