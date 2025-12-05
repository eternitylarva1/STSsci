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
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sciSTS.helpers.ModHelper;
import sciSTS.relics.*;
import sciSTS.utils.Invoker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrillEffect extends AbstractGameEffect {

    public static final String[] TEXT;
    private static final float DUR = 2.5F;
    private boolean openedScreen = false;
    private Color screenColor;

    // 使用静态哈希表便于后续添加新的烧烤映射
    private static final Map<String, String> GRILLABLE_MAPPING = new HashMap<>();
    static {
        GRILLABLE_MAPPING.put("Sci:Mango", "Sci:kaoMango");
        GRILLABLE_MAPPING.put("Sci:Strawberry", "Sci:kaoStrawberry");
        GRILLABLE_MAPPING.put("Sci:Pear", "Sci:kaoPear");
        GRILLABLE_MAPPING.put("Sci:rouchuan", "Sci:kaoMeat");
        GRILLABLE_MAPPING.put("Dagger", "Sci:kaoPear"); // 树枝烤梨子
        GRILLABLE_MAPPING.put("Staff", "Sci:BranchFuel"); // 树枝续火
        GRILLABLE_MAPPING.put("Mushroom", "Sci:RoastedMushroom"); // 烤蘑菇
        // 后续新增烧烤映射直接在这里添加
        // GRILLABLE_MAPPING.put("原版遗物ID", "烧烤后遗物ID");
    }

    private final Map<String, String> grillableMap;
    private List<AbstractRelic> availableRelics;
    private int selectedRelicIndex = -1;

    public GrillEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 0.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();

        // 引用静态映射表
        this.grillableMap = GRILLABLE_MAPPING;

        setupScreen();
    }

    private void setupScreen() {
        availableRelics = new ArrayList<>();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (grillableMap.containsKey(relic.relicId)) {
                availableRelics.add(relic);
            }
        }

        if (!availableRelics.isEmpty()) {
            // 简化实现：自动选择第一个可烧烤的遗物
            this.openedScreen = true;
        }
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        // 自动处理选择逻辑
        if (this.openedScreen && this.selectedRelicIndex == -1 && !availableRelics.isEmpty()) {
            this.selectedRelicIndex = 0;
            grillRelic(availableRelics.get(0));
            this.openedScreen = false;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            this.cancelComplete();
            this.enableAllButtons();
        }
    }

    private void grillRelic(AbstractRelic originalRelic) {
        String newRelicId = grillableMap.get(originalRelic.relicId);

        if (newRelicId != null) {
            // 移除原遗物
            AbstractDungeon.player.loseRelic(originalRelic.relicId);

            // 根据新遗物ID创建对应的遗物
            AbstractRelic newRelic = createNewRelic(newRelicId);

            if (newRelic != null && !newRelicId.equals("Sci:BranchFuel")) {
                // 普通烧烤遗物 - 添加到玩家遗物列表
                // 使用游戏原版的instantObtain方法
                newRelic.instantObtain();
                newRelic.flash();
            } else if (newRelicId.equals("Sci:BranchFuel")) {
                // 树枝续火 - 特殊处理，创建并立即触发效果
                BranchFuel branchFuel = (BranchFuel) newRelic;
                branchFuel.onObtain();
            }
        }
    }

    private AbstractRelic createNewRelic(String relicId) {
        switch (relicId) {
            case "Sci:kaoMango":
                return new kaoMango();
            case "Sci:kaoStrawberry":
                return new kaoStrawberry();
            case "Sci:kaoPear":
                return new kaoPear();
            case "Sci:kaoMeat":
                return new KaoMeat();
            case "Sci:RoastedMushroom":
                return new RoastedMushroom();
            case "Sci:BranchFuel":
                return new BranchFuel(); // 树枝燃料，需要特殊处理
            default:
                return null;
        }
    }

    public void cancelComplete() {
        AbstractRoom.waitTimer = 0.0F;
        AbstractDungeon.getCurrRoom().phase = RoomPhase.COMPLETE;
        ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
        RestRoom rest = (RestRoom) AbstractDungeon.getCurrRoom();
        CampfireUI cm = rest.campfireUI;
        cm.hidden = false;
        cm.somethingSelected = false;
    }

    public void enableAllButtons() {
        RestRoom rest = (RestRoom) AbstractDungeon.getCurrRoom();
        CampfireUI cm = rest.campfireUI;
        ArrayList<AbstractCampfireOption> buttons = Invoker.getField(cm, "buttons");
        for (AbstractCampfireOption button : buttons) {
            if (!(button instanceof GrillOption)) {
                button.usable = true;
            } else {
                ((GrillOption) button).updateUsability(true);
            }
        }
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
            // 简化实现，不渲染复杂选择界面
        }
    }

    public void dispose() {
    }

    static {
        String ID = ModHelper.makePath("GrillOption");
        TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    }
}