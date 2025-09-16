//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CoffeeDripper;
import com.megacrit.cardcrawl.relics.CrackedCore;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class UncrackedCore extends CustomRelic {
    public static final String ID = "FixedCore";

    public UncrackedCore() {
        super(ID, new Texture("SciSTSResources/images/relics/crackedOrb.png"), RelicTier.STARTER, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {

    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {

        if (info.owner != null &&info.owner instanceof AbstractMonster && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 1) {
        if (AbstractDungeon.cardRandomRng.randomBoolean(0.5F)){
                AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
                    @Override
                    public void render(SpriteBatch spriteBatch) {
                        isDone=true;
                        AbstractDungeon.player.loseRelic(UncrackedCore.ID );
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new CrackedCore());
                    }
       @Override
                    public void dispose() {

                    }
                });

        }}
        return super.onAttacked(info, damageAmount);
        }

    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }


    public AbstractRelic makeCopy() {
        return new UncrackedCore();
    }
}
