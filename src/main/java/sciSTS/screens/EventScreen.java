package sciSTS.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.ForeignInfluence;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventScreen extends CustomScreen  {

    ArrayList<AbstractCard> cards = new ArrayList<>();
    ArrayList<AbstractMonster> Monsterlist = new ArrayList<>();
    Map<String, AbstractMonster> monsterMap = new HashMap<>();
    Map<String, AbstractMonster> monsterCombinations = new HashMap<>();
    List<List<AbstractMonster>> monsterGroups = new ArrayList<>();

    private ScrollBar scrollBar;
    private float grabStartY = 0.0F;
    private float currentDiffY = 0.0F;
    private float scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
    private float scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
    private boolean grabbedScreen = false;
    private boolean canScroll = false;
    private float monsterSpacing = 200.0F * Settings.scale; // 怪物之间的间隔

    private AbstractEvent event;

    public static class Enum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen EVENTSCREEN;
    }

    public EventScreen() {
  }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return Enum.EVENTSCREEN;
    }

    private void open(AbstractEvent event) {
this.event=event;
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE) {
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }


        // Call reopen in this example because the basics of
        // setting the current screen are the same across both
        reopen();
    }

    private void calculateScrollBounds() {
        float totalHeight = 0.0F;
        for (int i = 0; i < monsterGroups.size(); i++) {
            List<AbstractMonster> group = monsterGroups.get(i);
            float maxHeight = Math.max(group.get(0).hb.height, Math.max(group.get(1).hb.height, group.get(2).hb.height));
            totalHeight += maxHeight + 50.0F * Settings.scale; // 50.0F * Settings.scale 是固定间距
        }

        if (totalHeight > Settings.HEIGHT* Settings.scale) {

            int scrollTmp = monsterGroups.size() / 5 - 1;
            if (monsterGroups.size() % 5 != 0) {
                ++scrollTmp;
            }
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT +totalHeight- (Settings.HEIGHT * Settings.scale);
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }
        this.canScroll = true;
    }

    @Override
    public void reopen() {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
    }

    @Override
    public void close() {
        genericScreenOverlayReset();
    }

    @Override
    public void update() {
     this.event.update();

    }

    private void updateScrolling() {
        if (!canScroll) {
            return;
        }

        int y = InputHelper.mY;
        boolean isDraggingScrollBar = this.scrollBar.update();
        if (!isDraggingScrollBar) {
            if (!this.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    this.currentDiffY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.currentDiffY -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    this.grabbedScreen = true;
                    this.grabStartY = (float)y - this.currentDiffY;
                }
            } else if (InputHelper.isMouseDown) {
                this.currentDiffY = (float)y - this.grabStartY;
            } else {
                this.grabbedScreen = false;
            }
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
        spriteBatch.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT - 64.0F * Settings.scale);
        this.event.render(spriteBatch);
    }

    @Override
    public void openingSettings() {
        // Required if you want to reopen your screen when the settings screen closes
        AbstractDungeon.previousScreen = curScreen();
    }

    private float convertX(float x) {
        return x * 235.0F * Settings.scale + 640.0F * Settings.scale;
    }

    private float convertY(float y) {
        return y * -235.0F * Settings.scale + 850.0F * Settings.scale - 50.0F * Settings.scale;
    }
}
