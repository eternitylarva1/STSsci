package sciSTS.cards.red;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.Slot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import sciSTS.utils.Invoker;
import sciSTS.patchs.CombatRewardPatch;

import java.util.ArrayList;
import java.util.Arrays;


import static sciSTS.utils.SpineRegionExtractor.getTextureFromSlot;
import static sciSTS.utils.TextureCache.cacheTexture;

public class jiaoxie extends AbstractCard {
    public static final String ID = "Disarm";
    private static final CardStrings cardStrings;
    public static boolean dropWeapons = false; // 标记是否应该掉落武器

    public jiaoxie() {
        super("Disarm", cardStrings.NAME, "red/skill/disarm", 1, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.RED, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
    @Override
    public void update() {
        Bone bone;
        Slot slot ;
        TextureAtlas atlas;
        ArrayList<Slot> weaponSlots = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            weaponSlots.clear();
            atlas= Invoker.getField(monster,"atlas");
            Skeleton skeleton = Invoker.getField(monster,"skeleton"); // 这里需要你根据游戏实际情况实现获取方法
// 获取指定插槽


            String[] slotsToHide = {"sword", "weaponleft", "weaponright","weapon","weponred", "weponblue","dagger","staff","scythe"};
            for (String slotName : slotsToHide) {
                slot = skeleton.findSlot(slotName);
                bone = skeleton.findBone(slotName);

                if (slot != null) {
                  weaponSlots.add(slot);
                    this.addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, -jiaoxie.this.magicNumber), -jiaoxie.this.magicNumber));

                }
                else if(bone!=null){
                    for (Slot slot1 : skeleton.getSlots()) {
                        if (slot1.getBone()==bone) {
                            weaponSlots.add(slot1);
                            this.addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, -jiaoxie.this.magicNumber), -jiaoxie.this.magicNumber));

                        }
                    }
                }



            }
            for (Slot slot2 : skeleton.getSlots()) {
                if (slot2 != null && slot2.getData().getName().contains("weapon")|| slot2.getData().getName().contains("dagger")|| slot2.getData().getName().contains("shield")|| slot2.getData().getName().contains("spear")) {
                    if(!weaponSlots.contains(slot2)) {
                        weaponSlots.add(slot2);
                    }
                }

            }
            for (Slot slots : weaponSlots) {
                Color currentColor = slots.getColor();

                Texture originalTexture = getTextureFromSlot(slots,atlas);
                String textureKey =slots.getData().getName()+"_"+slots.getData().getIndex();
              cacheTexture(textureKey, originalTexture);
                currentColor.set(currentColor.r, currentColor.g, currentColor.b, 0f);
            /*AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new Customweapon(getCachedTexture(textureKey))));
            */}

            // 如果成功缴械了武器，标记应该掉落武器
            if (weaponSlots.size() > 0) {
                dropWeapons = true;
            }
        }

        isDone=true;
    }
});

    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }


    }

    public AbstractCard makeCopy() {
        return new jiaoxie();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Disarm");
    }
}
