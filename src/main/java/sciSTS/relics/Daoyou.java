//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class Daoyou extends CustomRelic {
    public static final String ID = ModHelper.makePath(Daoyou.class.getSimpleName());

    public Daoyou() {
        super(ID, new Texture("SciSTSResources/images/relics/daoyou.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {

    }

    @Override
    public void onBlockBroken(AbstractCreature m) {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(m, this));
        this.addToBot(new DamageAction(m,new DamageInfo(null,3)));
    }


    public AbstractRelic makeCopy() {
        return new Daoyou();
    }
}
