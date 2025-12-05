package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class RoastedMushroom extends CustomRelic implements OnApplyPowerRelic {
    public static final String ID = ModHelper.makePath(RoastedMushroom.class.getSimpleName());

    public RoastedMushroom() {
        super(ID, new Texture("SciSTSResources/images/relics/mushroom.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new RoastedMushroom();
    }

    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        // 当有生物获得易伤时，阻止其效果
        if (power instanceof VulnerablePower) {
            this.flash();
            return false; // 阻止易伤效果
        }
        return true; // 允许其他power正常生效
    }
}