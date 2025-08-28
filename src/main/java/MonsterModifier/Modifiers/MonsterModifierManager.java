package MonsterModifier.Modifiers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class MonsterModifierManager {

    // 获取怪物的所有修改器列表（假设通过补丁给AbstractMonster添加了modifiers字段）
    public static ArrayList<AbstractMonsterModifier> modifiers(AbstractMonster m) {
        // 类似CardModifierFields，这里假设存在MonsterModifierFields存储修改器列表
        return (ArrayList<AbstractMonsterModifier>) MonsterModifierFields.monsterModifiers.get(m);
    }

    // 给怪物添加修改器
    public static void addModifier(AbstractMonster monster, AbstractMonsterModifier mod) {
        if (mod.shouldApply(monster)) {
            modifiers(monster).add(mod);
            Collections.sort(modifiers(monster)); // 按优先级排序
            mod.onModifierAdded(monster);;
            monster.applyPowers(); // 刷新怪物属性
            mod.addTips( monster);
            ModifyName(monster, mod);
        }
    }
    public static void ModifyName(AbstractMonster monster, AbstractMonsterModifier mod) {
       monster.name= mod.ModifyName( monster);
    }

    // 移除特定修改器
    public static void removeSpecificModifier(AbstractMonster monster, AbstractMonsterModifier mod, boolean includeInherent) {
        if (modifiers(monster).contains(mod) && (!mod.isInherent(monster) || includeInherent)) {
            modifiers(monster).remove(mod);
            mod.onModifierRemoved(monster);
            monster.applyPowers();
        }
    }

    // 按ID移除修改器
    public static void removeModifiersById(AbstractMonster monster, String id, boolean includeInherent) {
        ArrayList<AbstractMonsterModifier> removed = new ArrayList<>();
        modifiers(monster).removeIf(mod -> {
            if (!mod.identifier(monster).equals(id) || (mod.isInherent(monster) && !includeInherent)) {
                return false;
            } else {
                removed.add(mod);
                return true;
            }
        });
        removed.forEach(mod -> mod.onModifierRemoved(monster));
        monster.applyPowers();
    }

    // 检查怪物是否有指定ID的修改器
    public static boolean hasModifier(AbstractMonster monster, String id) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            if (mod.identifier(monster).equals(id)) {
                return true;
            }
        }
        return false;
    }

    // 获取怪物所有指定ID的修改器
    public static ArrayList<AbstractMonsterModifier> getModifiers(AbstractMonster monster, String id) {
        ArrayList<AbstractMonsterModifier> result = new ArrayList<>();
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            if (mod.identifier(monster).equals(id)) {
                result.add(mod);
            }
        }
        return result;
    }

    // 移除所有修改器（可选择保留固有修改器）
    public static void removeAllModifiers(AbstractMonster monster, boolean includeInherent) {
        ArrayList<AbstractMonsterModifier> removed = new ArrayList<>();
        modifiers(monster).removeIf(mod -> {
            if (!includeInherent && mod.isInherent(monster)) {
                return false;
            } else {
                removed.add(mod);
                return true;
            }
        });
        removed.forEach(mod -> mod.onModifierRemoved(monster));
        monster.applyPowers();
    }

    // 复制怪物的修改器到另一个怪物
    public static void copyModifiers(AbstractMonster oldMonster, AbstractMonster newMonster, boolean includeInherent, boolean replace, boolean removeOld) {
        if (replace) {
            removeAllModifiers(newMonster, includeInherent);
        }
        ArrayList<AbstractMonsterModifier> toCopy = new ArrayList<>();
        modifiers(oldMonster).removeIf(mod -> {
            if (!includeInherent && mod.isInherent(oldMonster)) {
                return false;
            } else {
                toCopy.add(mod);
                return removeOld;
            }
        });
        toCopy.forEach(mod -> {
            if (removeOld) {
                mod.onModifierRemoved(oldMonster);
            }
            AbstractMonsterModifier newMod = mod.makeCopy();
            if (newMod.shouldApply(newMonster)) {
                modifiers(newMonster).add(newMod);
                newMod.onModifierAdded(newMonster);
            }
        });
        if (removeOld) {
            oldMonster.applyPowers();
        }
        Collections.sort(modifiers(newMonster));
        newMonster.applyPowers();
    }

    // 延迟移除满足条件的修改器（如回合结束时）
    private static void deferredConditionalRemoval(final AbstractMonster monster, Predicate<AbstractMonsterModifier> condition) {
        final ArrayList<AbstractMonsterModifier> modifiers = modifiers(monster);
        final ArrayList<AbstractMonsterModifier> toRemove = new ArrayList<>();

        for (AbstractMonsterModifier mod : modifiers) {
            if (condition.test(mod)) {
                toRemove.add(mod);
            }
        }

        if (!toRemove.isEmpty()) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    toRemove.forEach(mod -> {
                        modifiers.remove(mod);
                        mod.onModifierRemoved(monster);
                    });
                    monster.applyPowers();
                    this.isDone = true;
                }
            });
        }
    }

    // 移除回合结束时需要移除的修改器
    public static void removeEndOfTurnModifiers(AbstractMonster monster) {
        deferredConditionalRemoval(monster, mod -> mod.removeAtEndOfTurn(monster));
    }

    // 移除怪物死亡时需要移除的修改器
    public static void removeOnDeathModifiers(AbstractMonster monster) {
        deferredConditionalRemoval(monster, mod -> mod.removeOnMonsterDeath(monster));
    }

    // ------------------------------ 事件分发方法 ------------------------------

    // 怪物生成时触发
    public static void onMonsterSpawn(AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onMonsterSpawn(monster);
        }
    }

    // 怪物死亡时触发
    public static void onMonsterDeath(AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onMonsterDeath(monster);
        }
        removeOnDeathModifiers(monster); // 触发死亡时的移除逻辑
    }

    // 回合开始时触发
    public static void atStartOfTurn(AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.atStartOfTurn(monster);
        }
    }

    // 回合结束时触发
    public static void atEndOfTurn(AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.atEndOfTurn(monster);
        }
        removeEndOfTurnModifiers(monster); // 触发回合结束时的移除逻辑
    }

    // 怪物攻击时触发
    public static void onAttack(AbstractMonster monster, DamageInfo info, int damageAmount, AbstractCreature target) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onAttack(monster, info, damageAmount, target);
        }
    }

    // 怪物受到伤害时触发
    public static void onDamageReceived(AbstractMonster monster, DamageInfo info, int damageAmount) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onDamageReceived(monster, info, damageAmount);
        }
    }

    // 怪物格挡被击破时触发
    public static void onBlockBroken(AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onBlockBroken(monster);
        }
    }

    // 怪物施加力量时触发
    public static void onApplyPower(AbstractMonster monster, AbstractPower power, AbstractCreature target) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onApplyPower(monster, power, target);
        }
    }

    // 玩家使用卡牌时触发（怪物视角）
    public static void onPlayerPlayCard(AbstractCard card, AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onPlayCard(card, monster);
        }
    }

    // 怪物使用力量时触发
    public static void onUsePower(AbstractPower power, AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onUsePower(power, monster);
        }
    }

    // ------------------------------ 属性修改方法 ------------------------------

    // 修改最大生命值
    public static float modifyMaxHealth(float maxHealth, AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            maxHealth = mod.modifyMaxHealth(maxHealth, monster);
        }
        return maxHealth;
    }

    // 修改当前生命值
    public static float modifyCurrentHealth(float currentHealth, AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            currentHealth = mod.modifyCurrentHealth(currentHealth, monster);
        }
        return currentHealth;
    }

    // 修改攻击伤害
    public static int modifyMoveDamage(int damage, AbstractMonster monster, byte moveId) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            damage = mod.modifyMoveDamage(damage, monster, moveId);
        }
        return damage;
    }

    // 修改格挡值
    public static int modifyBlock(int block, AbstractMonster monster) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            block = mod.modifyBlock(block, monster);
        }
        return block;
    }

    // ------------------------------ 渲染相关方法 ------------------------------

    // 怪物渲染时触发
    public static void onRender(AbstractMonster monster, SpriteBatch sb) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onRender(monster, sb);
        }
    }

    // 怪物血条渲染时触发
    public static void onHpBarRender(AbstractMonster monster, SpriteBatch sb) {
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            mod.onHpBarRender(monster, sb);
        }
    }

    // ------------------------------ 工具方法 ------------------------------

    // 获取额外描述符
    public static List<String> getExtraDescriptors(AbstractMonster monster) {
        List<String> descriptors = new ArrayList<>();
        modifiers(monster).forEach(mod -> descriptors.addAll(mod.extraDescriptors(monster)));
        return descriptors;
    }

    // 战斗开始时触发
    public static void onBattleStart(AbstractMonster monster) {
        boolean showEffect = false;
        for (AbstractMonsterModifier mod : modifiers(monster)) {
            if (mod.onBattleStart(monster)) {
                showEffect = true;
            }
        }
        if (showEffect) {
            // 示例：显示怪物强化效果
            AbstractDungeon.effectList.add(new HealEffect(monster.hb.cX, monster.hb.cY, 0));
        }
    }

    // 添加动作到动作队列顶部
    private static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    // 添加动作到动作队列底部
    private static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    // 用于存储怪物修改器的字段（需要通过ASM补丁添加到AbstractMonster）
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "<class>"
    )
    public static class MonsterModifierFields {
        public static ArrayList<AbstractMonsterModifier> MonsterModifiers = new ArrayList<>();
        public static final com.evacipated.cardcrawl.modthespire.lib.SpireField<ArrayList<AbstractMonsterModifier>> monsterModifiers =
                new com.evacipated.cardcrawl.modthespire.lib.SpireField<>(() -> MonsterModifiers);
    }
}