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

public class kaoMango extends CustomRelic {
    public static final String ID = ModHelper.makePath(kaoMango.class.getSimpleName());

    public kaoMango() {
        super(ID, new Texture("SciSTSResources/images/relics/mango.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0]+14;

    }
    public void onEquip() {
        if(CardCrawlGame.chosenCharacter== AbstractPlayer.PlayerClass.DEFECT){
            return ;
        }
        AbstractDungeon.player.heal(10, true);
    }


    public AbstractRelic makeCopy() {
        return new kaoMango();
    }
}
