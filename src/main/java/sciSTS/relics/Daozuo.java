//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.HandDrill;
import sciSTS.helpers.ModHelper;

public class Daozuo extends CustomRelic {
    public static final String ID = ModHelper.makePath(Daozuo.class.getSimpleName());

    public Daozuo() {
        super(ID, new Texture("SciSTSResources/images/relics/daozuo.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {

    }

    public void onBlockBroken(AbstractCreature m) {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(m, this));
        this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, 2, false), 2));
    }



    public AbstractRelic makeCopy() {
        return new Daozuo();
    }
}
