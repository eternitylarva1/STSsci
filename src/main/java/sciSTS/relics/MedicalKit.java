//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MedicalKit extends AbstractRelic implements ClickableRelic {
    public static final String ID = "Medical Kit";

    public MedicalKit() {
        super("Medical Kit", "medicalKit.png", RelicTier.SHOP, LandingSound.MAGICAL);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MedicalKit();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == CardType.STATUS) {
            AbstractDungeon.player.getRelic("Medical Kit").flash();
            card.exhaust = true;
            action.exhaustCard = true;

        }

    }

    @Override
    public void onRightClick() {
        if (!CardCrawlGame.isInARun()){
            return;
        }
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT){
            return;
        }
        //TODO 补全随机逻辑
        if (!AbstractDungeon.player.hasRelic(OrangePellets.ID)){
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new OrangePellets());

                }
            });
            return;
        }
        if (!AbstractDungeon.player.hasRelic(ToughBandages.ID)){
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new ToughBandages());

                }
            });
            return;
        }
    }
}
