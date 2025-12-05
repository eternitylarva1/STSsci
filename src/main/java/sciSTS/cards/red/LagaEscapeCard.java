package sciSTS.cards.red;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 乐加逃跑卡牌 - 当乐加睡觉时可使用的逃跑卡
 * 使用后立即离开战斗
 */
public class LagaEscapeCard extends AbstractCard {
    public static final String ID = "LagaEscape";
    private static final CardStrings cardStrings;

    public LagaEscapeCard() {
        super("LagaEscape", cardStrings.NAME, "red/skill/escape", 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.RED, CardRarity.SPECIAL, CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 玩家尝试逃跑
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                // 检查玩家是否有敏捷buff
                boolean hasDexterity = p.hasPower("Dexterity");

                // 如果有敏捷，必定成功
                if (hasDexterity) {
                    // 玩家成功逃跑，离开战斗 - 使用正确的房间切换
                    AbstractDungeon.getCurrRoom().isBattleOver = true;
                } else {
                    // 没有敏捷，15%概率惊醒乐加
                    if (AbstractDungeon.aiRng.randomBoolean(0.15F)) {
                        // 逃跑失败，惊醒乐加
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (monster instanceof com.megacrit.cardcrawl.monsters.exordium.Lagavulin) {
                                // 唤醒乐加，设置asleep为false
                                try {
                                    java.lang.reflect.Method wakeUpMethod = monster.getClass().getMethod("wakeUp");
                                    wakeUpMethod.invoke(monster);
                                } catch (Exception e) {
                                    // 如果没有wakeUp方法，尝试直接设置asleep字段
                                    try {
                                        java.lang.reflect.Field asleepField = monster.getClass().getDeclaredField("asleep");
                                        asleepField.setAccessible(true);
                                        asleepField.set(monster, false);
                                    } catch (Exception ex) {
                                        // 忽略错误
                                    }
                                }
                                break;
                            }
                        }
                        // 战斗继续
                    } else {
                        // 逃跑成功
                        AbstractDungeon.getCurrRoom().isBattleOver = true;
                    }
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            // 逃跑卡升级逻辑（如果需要）
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LagaEscapeCard();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("LagaEscape");
    }
}
