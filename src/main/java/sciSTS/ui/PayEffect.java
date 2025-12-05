//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.CoffeeDripper;
import com.megacrit.cardcrawl.relics.FusionHammer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sciSTS.helpers.ModHelper;
import sciSTS.utils.Invoker;

import java.util.ArrayList;


public class PayEffect extends AbstractGameEffect {

    public static final String[] TEXT;
    private static final float DUR = 2.5F;
    private boolean openedScreen = false;
    private boolean openedScreen2 = false;
    private Color screenColor;

    public PayEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 0.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }



        if (!AbstractDungeon.isScreenUp ) {


        }



        if (this.duration < 0.0F) {
            this.isDone = true;
            // hasselected=true; // 注释掉，变量未定义
            // AbstractDungeon.player.loseGold(getTrueCost()); // 注释掉，方法未找到
            cancelComplete();
            EnableAllbuttons();
                }

        }
public static void cancelComplete(){
    AbstractRoom.waitTimer = 0.0F;
    AbstractDungeon.getCurrRoom().phase = RoomPhase.COMPLETE;
    ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
    RestRoom rest=(RestRoom )AbstractDungeon.getCurrRoom();
    CampfireUI cm=rest.campfireUI;
    cm.hidden=false;
    cm.somethingSelected=false;

}
    public static void EnableAllbuttons(){
        RestRoom rest=(RestRoom )AbstractDungeon.getCurrRoom();
        CampfireUI cm=rest.campfireUI;
        ArrayList<AbstractCampfireOption> buttons = Invoker.getField(cm, "buttons");
        for (AbstractCampfireOption button : buttons) {
            if (!(button instanceof PayOption)){
                button.usable=true;
            }else {
                // if (Trueduoci>=1&&AbstractDungeon.player.gold>= getTrueCost()){ // 注释掉，变量方法未找到
                //     button.usable=true;
                //     Trueduoci--;
                // }else {
                //     button.usable=false;
                // }
                // canuseCampfire=true; // 注释掉，变量未定义
                ((PayOption) button).updateUsability(true);
            }
            if (button instanceof RestOption &&AbstractDungeon.player.hasRelic(CoffeeDripper.ID)) {
                button.usable = false;
            }if (button instanceof SmithOption &&AbstractDungeon.player.hasRelic(FusionHammer.ID)) {
                button.usable = false;
            }
        }
    }
     public static String removeUnwantedCharacters(String input) {
        if (input == null) return null;

        return input.replace("#b", "")
                .replace("#y", "")
                .replace("NL", "");
    }

    private void updateBlackScreenColor() {
        if (this.duration > 0.5F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 0.5F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID) {

        }

    }

    public void dispose() {
    }

    static {
        String ID = ModHelper.makePath("PayOption");
        TEXT =( CardCrawlGame.languagePack.getUIString(ID)).TEXT;
    }
}
