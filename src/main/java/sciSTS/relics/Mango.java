//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

public class Mango extends AbstractRelic {
    public static final String ID = "Sci:Mango";
    private static final int HP_AMT = 14;

    public Mango() {
        super("Sci:Mango", "mango.png", RelicTier.RARE, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + 14 +this.DESCRIPTIONS[1];
    }

    public void onEquip() {
        if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
            return ;
        }
        AbstractDungeon.player.increaseMaxHp(14, true);
    }

    public AbstractRelic makeCopy() {
        return new Mango();
    }
}
