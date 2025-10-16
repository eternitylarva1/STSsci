//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class kaoStrawberry extends CustomRelic {
    public static final String ID = ModHelper.makePath(kaoStrawberry.class.getSimpleName());

    public kaoStrawberry() {
        super(ID, new Texture("SciSTSResources/images/relics/strawberry.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];

    }
    public void onEquip() {
        if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
            return ;
        }
        AbstractDungeon.player.heal(7, true);
    }


    public AbstractRelic makeCopy() {
        return new kaoStrawberry();
    }
}
