//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MeatOnTheBone;
import com.megacrit.cardcrawl.relics.Turnip;
import sciSTS.helpers.ModHelper;

public class KaoMeat extends CustomRelic {
    public static final String ID = ModHelper.makePath(KaoMeat.class.getSimpleName());

    public KaoMeat() {
        super(ID, new Texture("SciSTSResources/images/relics/meat.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];

    }

    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        if ((float)p.currentHealth <= (float)p.maxHealth / 2.0F && p.currentHealth > 0) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
           p.increaseMaxHp(2,true);
            this.stopPulse();
        }


    }

    public AbstractRelic makeCopy() {
        return new KaoMeat();
    }
}
