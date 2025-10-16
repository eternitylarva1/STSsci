//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class kaoPear extends CustomRelic {
    public static final String ID = ModHelper.makePath(kaoPear.class.getSimpleName());

    public kaoPear() {
        super(ID, new Texture("SciSTSResources/images/relics/pear.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];

    }
    public void onEquip() {
        if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
            return ;
        }
        AbstractDungeon.player.heal(10, true);
    }


    public AbstractRelic makeCopy() {
        return new kaoPear();
    }
}
