package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import sciSTS.helpers.ModHelper;
import sciSTS.ui.GrillOption;
import sciSTS.utils.Invoker;

import java.util.ArrayList;

public class BranchFuel extends CustomRelic {
    public static final String ID = ModHelper.makePath(BranchFuel.class.getSimpleName());

    public BranchFuel() {
        super(ID, new Texture("SciSTSResources/images/relics/drag.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BranchFuel();
    }

    public void onObtain() {
        this.flash();
        restoreCampfireOption();
        AbstractDungeon.player.loseRelic(this.ID);
    }

    private void restoreCampfireOption() {
        if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
            RestRoom restRoom = (RestRoom) AbstractDungeon.getCurrRoom();

            // 使用你确认存在的buttons字段
            ArrayList<AbstractCampfireOption> buttons = Invoker.getField(restRoom.campfireUI, "buttons");

            if (buttons != null) {
                boolean grillOptionRestored = false;
                for (AbstractCampfireOption button : buttons) {
                    if (button instanceof GrillOption) {
                        GrillOption grillOption = (GrillOption) button;
                        if (!grillOption.usable) {
                            grillOption.updateUsability(true);
                            grillOptionRestored = true;
                            break;
                        }
                    }
                }

                if (grillOptionRestored) {
                    System.out.println("火堆续火成功！烧烤选项已恢复！");
                } else {
                    System.out.println("没有已使用的烧烤选项可以恢复！");
                }
            }
        }
    }
}