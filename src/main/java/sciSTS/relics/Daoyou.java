//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class Daoyou extends CustomRelic {
    public static final String ID = ModHelper.makePath(Daoyou.class.getSimpleName());

    public Daoyou() {
        super(ID, new Texture("SciSTSResources/images/relics/daoyou.png"), RelicTier.STARTER, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {

    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {

        return super.onAttacked(info, damageAmount);
        }



    public AbstractRelic makeCopy() {
        return new Daoyou();
    }
}
