package MonsterModifier.Modifiers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

public abstract class AbstractMonsterModifier implements Comparable<AbstractMonsterModifier> {
    public int priority = 0;
    public String ModifyName(AbstractMonster  monster){
        return monster.name;
    }

    // 生命周期管理
    public boolean removeOnMonsterDeath(AbstractMonster monster) {
        return false;
    }

    public boolean removeAtEndOfTurn(AbstractMonster monster) {
        return false;
    }

    // 属性修改
    public float modifyMaxHealth(float maxHealth, AbstractMonster monster) {
        return maxHealth;
    }
    
    public float modifyCurrentHealth(float currentHealth, AbstractMonster monster) {
        return currentHealth;
    }

    public int modifyMoveDamage(int damage, AbstractMonster monster, byte moveId) {
        return damage;
    }

    public int modifyBlock(int block, AbstractMonster monster) {
        return block;
    }

    // 行为控制
    public void onMonsterSpawn(AbstractMonster monster) {
    }

    public void addTips(AbstractMonster  monster){
    }
    public String GetModifierName(){
        return "";
    };
    public void onMonsterDeath(AbstractMonster monster) {
    }

    public void atStartOfTurn(AbstractMonster monster) {
    }

    public void atEndOfTurn(AbstractMonster monster) {
    }

    public void onAttack(AbstractMonster monster, DamageInfo info, int damageAmount, AbstractCreature target) {
    }

    public void onDamageReceived(AbstractMonster monster, DamageInfo info, int damageAmount) {
    }

    public void onBlockBroken(AbstractMonster monster) {
    }

    public void onApplyPower(AbstractMonster monster, AbstractPower power, AbstractCreature target) {
    }

    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
    }

    public void onUsePower(AbstractPower power, AbstractMonster monster) {
    }

    // 渲染相关
    public void onRender(AbstractMonster monster, SpriteBatch sb) {
    }

    public void onHpBarRender(AbstractMonster monster, SpriteBatch sb) {
    }

    // 抽象方法
    public abstract AbstractMonsterModifier makeCopy();

    // 工具方法
    public String identifier(AbstractMonster monster) {
        return "";
    }

    public boolean shouldApply(AbstractMonster monster) {
        return true;
    }

    public List<String> extraDescriptors(AbstractMonster monster) {
        return Collections.emptyList();
    }

    public void onModifierAdded(AbstractMonster monster) {
    }

    public void onModifierRemoved(AbstractMonster monster) {
    }


    public int compareTo(AbstractMonsterModifier other) {
        return this.priority - other.priority;
    }

    public void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public boolean isInherent(AbstractMonster monster) {
        return false;
    }

    public boolean onBattleStart(AbstractMonster monster) {
        return false;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SaveIgnore {
    }
}