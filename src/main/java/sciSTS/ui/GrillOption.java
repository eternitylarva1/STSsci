package sciSTS.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import sciSTS.helpers.ModHelper;
import sciSTS.relics.kaoIceCream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrillOption extends AbstractCampfireOption {
    public static final String[] TEXT;
    private boolean hasGrillableRelics;
    private int remainingUses = 2; // 每个房间独立的使用次数

    public GrillOption(boolean active) {
        this.label = TEXT[0];
        this.hasGrillableRelics = checkGrillableRelics();
        this.updateUsability(active);
    }

    private static final Map<String, String> GRILLABLE_MAPPING = new HashMap<>();
    static {
        GRILLABLE_MAPPING.put("Sci:Mango", "Sci:kaoMango");
        GRILLABLE_MAPPING.put("Sci:Strawberry", "Sci:kaoStrawberry");
        GRILLABLE_MAPPING.put("Sci:Pear", "Sci:kaoPear");
        GRILLABLE_MAPPING.put("Sci:rouchuan", "Sci:kaoMeat");
        GRILLABLE_MAPPING.put("Dagger", "Sci:kaoPear"); // 树枝烤梨子
        GRILLABLE_MAPPING.put(IceCream.ID, kaoIceCream.ID);

        GRILLABLE_MAPPING.put("Mushroom", "Sci:RoastedMushroom"); // 烤蘑菇
        GRILLABLE_MAPPING.put("Dead Branch", CharonsAshes.ID);
        GRILLABLE_MAPPING.put(BloodVial.ID, BurningBlood.ID);
        // 枯木树枝烤成卡戎灰
        // 后续新增烧烤映射直接在这里添加
        // GRILLABLE_MAPPING.put("原版遗物ID", "烧烤后遗物ID");
    }

    private boolean checkGrillableRelics() {
        List<String> grillableRelicIds = new ArrayList<>();

        // 直接使用静态映射表的键值
        grillableRelicIds.addAll(GRILLABLE_MAPPING.keySet());

        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (grillableRelicIds.contains(relic.relicId)) {
                return true;
            }
        }
        return false;
    }

    public void updateUsability(boolean canUse) {
        this.hasGrillableRelics = checkGrillableRelics();
        this.usable = canUse && this.hasGrillableRelics && remainingUses > 0;

        if (this.hasGrillableRelics && remainingUses > 0) {
            this.description = TEXT[1] + TEXT[2] + " (" + remainingUses + "次剩余)";
        } else {
            this.description = TEXT[1] + TEXT[3];
        }

        this.img = ImageMaster.loadImage("SciSTSResources/images/ui/ritual.png");
    }

    public void useOption() {
        if (this.usable && this.hasGrillableRelics && remainingUses > 0) {
            AbstractDungeon.effectList.add(new GrillEffect());
            remainingUses--;
            this.updateUsability(true); // 刷新按钮状态（次数用完时会变为不可用）
        }
    }

    static {
        String ID = ModHelper.makePath("GrillOption");
        TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    }

}