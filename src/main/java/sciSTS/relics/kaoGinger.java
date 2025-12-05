//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

class kaoGinger extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = ModHelper.makePath(kaoGinger.class.getSimpleName());

    public kaoGinger() {
        super(ID, new Texture("SciSTSResources/images/relics/ginger.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];

    }



    public AbstractRelic makeCopy() {
        return new kaoGinger();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
       if (abstractCreature instanceof AbstractPlayer){
           if (abstractPower instanceof WeakPower){
               this.addToTop(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,abstractPower.amount)));
           }
       }
        return true;
    }
}
