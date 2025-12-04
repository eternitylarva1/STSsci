//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import sciSTS.helpers.ModHelper;



public class PayOption extends AbstractCampfireOption {
    public static final String[] TEXT;

    public PayOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        this.description = TEXT[1] +TEXT[2];
        this.img = ImageMaster.loadImage("PayToFireResources/images/smith3.png");
    }

    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new PayEffect());
            this.usable = false;
        }

    }

    static {
        String ID = ModHelper.makePath("PayOption");
        TEXT =( CardCrawlGame.languagePack.getUIString(ID)).TEXT;

    }
    @Override
    public void render(SpriteBatch sb) {
        Color color=sb.getColor();
        sb.setColor(Color.WHITE);
        sb.setColor(color);
        super.render(sb);
    }
}
