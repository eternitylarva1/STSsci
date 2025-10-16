//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class kaoTurnip extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = ModHelper.makePath(kaoTurnip.class.getSimpleName());

    public kaoTurnip() {
        super(ID, new Texture("SciSTSResources/images/relics/turnip.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];

    }



    public AbstractRelic makeCopy() {
        return new kaoTurnip();
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
       if (abstractCreature instanceof AbstractPlayer){
           if (abstractPower instanceof FrailPower){
               this.addToTop(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,abstractPower.amount)));
           }
       }
        return true;
    }
}
