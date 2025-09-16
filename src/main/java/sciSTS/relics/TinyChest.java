//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.relics.CrackedCore;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TinyChest extends AbstractRelic implements ClickableRelic {
    public static final String ID = "Tiny Chest";
    public static final int ROOM_COUNT = 4;

    @Override
    public void onRightClick() {

        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            @Override
            public void render(SpriteBatch spriteBatch) {
                isDone=true;
                AbstractDungeon.player.loseRelic(TinyChest.ID );
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(RelicTier.COMMON);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2),relic);
            }
            @Override
            public void dispose() {

            }
        });
    }

    public TinyChest() {
        super("Tiny Chest", "tinyChest.png", RelicTier.COMMON, LandingSound.SOLID);
        this.counter = -1;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + 4 + this.DESCRIPTIONS[1];
    }

    public void onEquip() {
        this.counter = 0;
    }

    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 35;
    }

    public AbstractRelic makeCopy() {
        return new TinyChest();
    }
}
