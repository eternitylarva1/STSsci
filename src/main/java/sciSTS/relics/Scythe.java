//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicDome;
import sciSTS.helpers.ModHelper;

public class Scythe extends CustomRelic {
    public static final String ID = ModHelper.makePath(Scythe.class.getSimpleName());

    public Scythe() {
        super(ID, new Texture("SciSTSResources/images/relics/scythe.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {

            if (monster.getIntentDmg()>=30&&!AbstractDungeon.player.hasRelic(RunicDome.ID)){
                addToBot(new DamageAction(monster,new DamageInfo(AbstractDungeon.player,45, DamageInfo.DamageType.THORNS)));
            }
        }


    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {

        return super.onAttacked(info, damageAmount);
        }



    public AbstractRelic makeCopy() {
        return new Scythe();
    }
}
