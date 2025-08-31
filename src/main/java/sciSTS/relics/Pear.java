//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

public class Pear extends AbstractRelic {
    public static final String ID = "Sci:Pear";
    private static final int HP_AMT = 10;

    public Pear() {
        super("Sci:Pear", "pear.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] +10+this.DESCRIPTIONS[1];
    }

    public void onEquip() {
        if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
            return ;
        }
        AbstractDungeon.player.increaseMaxHp(10, true);
    }

    public AbstractRelic makeCopy() {
        return new Pear();
    }
}
