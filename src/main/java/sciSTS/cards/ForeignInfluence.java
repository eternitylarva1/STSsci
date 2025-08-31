//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ForeignInfluenceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.cards.red.Rupture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sciSTS.events.GremlinMiner;

import static sciSTS.screens.EventScreen.Enum.EVENTSCREEN;

public class ForeignInfluence extends AbstractCard {
    public static final String ID = "ForeignInfluence";
    private static final CardStrings cardStrings;

    public ForeignInfluence() {
        super("ForeignInfluence", cardStrings.NAME, "purple/skill/foreign_influence", 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.PURPLE, CardRarity.UNCOMMON, CardTarget.NONE);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone=true;
                AbstractDungeon.player.gold=214;
            }
        });
        this.addToBot(new WaitAction(1.0F));

this.addToBot(new MakeTempCardInHandAction(new Rupture()));
        this.addToBot(new WaitAction(1.0F));
this.addToBot(new MakeTempCardInHandAction(new Feed()));
this.addToBot(new AddCardToDeckAction(new Rupture()));
this.addToBot(new AddCardToDeckAction(new Feed()));


    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }

    }

    public AbstractCard makeCopy() {
        return new ForeignInfluence();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("ForeignInfluence");
    }
}
