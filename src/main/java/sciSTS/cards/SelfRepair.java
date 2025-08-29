//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CrackedCore;
import com.megacrit.cardcrawl.relics.FrozenCore;
import sciSTS.relics.UncrackedCore;

public class SelfRepair extends AbstractCard {
    public static final String ID = "Self Repair";
    private static final CardStrings cardStrings;

    public SelfRepair() {
        super("Self Repair", cardStrings.NAME, "blue/power/self_repair", 1, cardStrings.DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 7;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new RepairPower(p, this.magicNumber), this.magicNumber));
       if (p.hasRelic(CrackedCore.ID)){
           this.addToBot(new AbstractGameAction() {
               @Override
               public void update() {

                   isDone=true;
                   if (!p.hasRelic(CrackedCore.ID)){
                       return;
                   }
                   AbstractPlayer player = AbstractDungeon.player;
                   AbstractRelic relic=new UncrackedCore();
                   player.relics.stream()
                           .filter(r -> r instanceof CrackedCore)  // 过滤出所有是OldRevolver类型的遗物
                           .findFirst()                            // 找到第一个匹配的遗物
                           .map(r -> player.relics.indexOf(r))     // 获取该遗物在列表中的索引位置
                           .ifPresent(index ->relic.instantObtain(player, index, true)); // 如果找到，则调用instantObtain方法

               }
           });
       }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(3);
        }

    }

    public AbstractCard makeCopy() {
        return new SelfRepair();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Self Repair");
    }
}
