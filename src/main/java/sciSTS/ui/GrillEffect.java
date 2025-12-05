package sciSTS.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
    private List<AbstractCard> selectedRelics = new ArrayList<>();
    private boolean hasSelected = false;

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
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        // 真正的玩家选择界面实现（参考FumoEffect模式）
        if (!AbstractDungeon.isScreenUp && !availableRelics.isEmpty() && !hasSelected) {
            if (this.duration < 0.5F && !this.openedScreen) {
                this.openedScreen = true;

                // 创建卡牌组，每个遗物用一个虚拟卡牌表示
                CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractRelic relic : availableRelics) {
                    AbstractCard relicCard = createRelicCard(relic);
                    cardGroup.addToTop(relicCard);
                }

                // 打开选择界面让玩家选择要烧烤的遗物
                AbstractDungeon.gridSelectScreen.open(cardGroup, 1, TEXT[0], false, false, true, false);
            }
        }

        // 检查玩家是否选择了卡牌
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // 获取玩家选择的卡牌
            selectedRelics.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            hasSelected = true;

            // 烧烤选中的遗物
            for (AbstractCard selectedCard : selectedRelics) {
                for (AbstractRelic relic : availableRelics) {
                    if (relic.relicId.equals(selectedCard.cardID)) {
                        // 先获取烤完后的新遗物
                        String newRelicId = grillableMap.get(relic.relicId);
                        if (newRelicId != null) {
                            AbstractRelic newRelic = createNewRelic(newRelicId);
                            if (newRelic != null) {
                                // 显示烧烤成功后的新遗物效果
                                ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(createRelicCard(newRelic), 0.0F, 0.0F);
                                AbstractDungeon.effectsQueue.add(effect);
                            }
                        }

                        // 执行烧烤逻辑
                        grillRelic(relic);
                        break;
                    }
                }
            }

            selectedRelics.clear();
            hasSelected = false;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            this.cancelComplete();
            this.enableAllButtons();
        }
    }

    private AbstractCard createRelicCard(AbstractRelic relic) {
        // 创建虚拟卡牌来表示遗物
        return new RelicCard(relic);
    }

    // 内部类：用于表示遗物的卡牌
    private static class RelicCard extends AbstractCard {
        private final AbstractRelic targetRelic;

        public RelicCard(AbstractRelic relic) {
            super(relic.name, relic.name, relic.imgUrl, 0, relic.description, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
            this.targetRelic = relic;
            this.cardID = relic.relicId;
            this.rawDescription = relic.description;

            // 使用用户提供的方法：从遗物复制贴图
            changeTexturesFromRelic(relic);

            this.initializeDescription();
        }

        private void changeTexturesFromRelic(AbstractRelic relic) {
            try {
                Object texture = relic.img;

                if (texture != null) {
                    changeTextures((com.badlogic.gdx.graphics.Texture) texture);
                }
            } catch (Exception e) {
                // 忽略错误，使用默认图片
            }
        }

        public void changeTextures(com.badlogic.gdx.graphics.Texture texture) {
            // Step 2: 将Texture转换为TextureAtlas.AtlasRegion
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion customRegion = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            this.portrait = customRegion;
        }

        @Override
        public AbstractCard makeCopy() {
            return new RelicCard(targetRelic);
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            // 这张卡牌不会真正被使用，只用于选择界面
        }

        @Override
        public void upgrade() {
            // 这张卡牌不能升级
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