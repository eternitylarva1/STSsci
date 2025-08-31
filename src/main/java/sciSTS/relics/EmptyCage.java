//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import java.util.ArrayList;
import sciSTS.cards.PokeBall;
public class EmptyCage extends AbstractRelic implements ClickableRelic {
    public static final String ID = "Sci:Empty Cage";
    private boolean cardsSelected = true;
    private boolean canused=true;
    public EmptyCage() {
        super("Sci:Empty Cage", "cage.png", RelicTier.BOSS, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.getCurrRoom().phase = RoomPhase.INCOMPLETE;
        CardGroup tmp = new CardGroup(CardGroupType.UNSPECIFIED);

        for(AbstractCard card : AbstractDungeon.player.masterDeck.getPurgeableCards().group) {
            tmp.addToTop(card);
        }

        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
        } else {
            if (tmp.group.size() <= 2) {
                this.deleteCards(tmp.group);
            } else {
                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 2, this.DESCRIPTIONS[1], false, false, false, true);
            }

        }
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        canused=true;
    }

    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 2) {
            this.deleteCards(AbstractDungeon.gridSelectScreen.selectedCards);
        }

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
        return new EmptyCage();
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT||!(canused)){
            return;
        }
        if (AbstractDungeon.player!=null&&AbstractDungeon.player.hasRelic(this.relicId)){
this.addToTop(new MakeTempCardInHandAction(new PokeBall( this)));
            canused=false;
        }
    }
}
