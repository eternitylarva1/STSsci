//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.relics.Torii;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import sciSTS.cards.PokeBall;
import sciSTS.cards.PokeBall1;

import java.util.ArrayList;

import static sciSTS.cards.PokeBall.isBird;

public class FullCage extends CustomRelic implements ClickableRelic {
    public static final String ID = "FullCage";
    private boolean cardsSelected = true;
private boolean canused=true;
    public FullCage() {
        super(ID, new Texture(Gdx.files.internal("SciSTSResources/images/relics/cage.png")), RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip() {

    }

    public void update() {
        super.update();

    }
    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        canused=true;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null &&info.owner instanceof AbstractMonster && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 1&&AbstractDungeon.player.hasRelic(PreservedInsect.ID)) {
           if (!isBird((AbstractMonster) info.owner)){
               return damageAmount;
           }
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            if (AbstractDungeon.cardRandomRng.randomBoolean(0.25f)){
                AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
                    @Override
                    public void render(SpriteBatch spriteBatch) {
                        isDone=true;
                        AbstractDungeon.player.loseRelic(PreservedInsect.ID );
                        addToBot(new HealAction(info.owner,info.owner,20));
                    }

                    @Override
                    public void dispose() {

                    }
                });
            }
        }
            return damageAmount;

    }
    public void deleteCards(ArrayList<AbstractCard> group) {
        this.cardsSelected = true;
        float displayCount = 0.0F;

        for(AbstractCard card : group) {
            card.untip();
            card.unhover();
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float)Settings.WIDTH / 3.0F + displayCount, (float)Settings.HEIGHT / 2.0F));
            displayCount += (float)Settings.WIDTH / 6.0F;
            AbstractDungeon.player.masterDeck.removeCard(card);
        }

        AbstractDungeon.getCurrRoom().phase = RoomPhase.COMPLETE;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    public AbstractRelic makeCopy() {
        return new FullCage();
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT||!(canused)){
            return;
        }
        if (AbstractDungeon.player!=null&&AbstractDungeon.player.hasRelic(this.relicId)){
            this.addToTop(new MakeTempCardInHandAction(new PokeBall1( this)));
            canused=false;
        }
    }
}
