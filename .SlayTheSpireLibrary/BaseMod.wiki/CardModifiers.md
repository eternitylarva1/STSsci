# Overview

CardModifiers are something that you can attach directly to a card in order to modify it. They function much like powers in that they all extend an Abstract class, and have several hooks supporting various functions. CardModifiers are structured in a way that you have a great amount of control in your extending class, so you can do something as simple as "+2 damage" or as complicated as "spend gold instead of energy." or "The next two times this is played, deal double damage".

A card modifier must extend `AbstractCardModifier`, and must Override the method `makeCopy()`, in which it will return a new instance of itself.

In order to add a modifier to a card, you use the `addModifier()` method from `CardModifierManager`. `CardModifierManager` is found in BaseMod's `helpers` package.

Example, a card that adds a modifier to itself when played:
```java
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		CardModifierManager.addModifier(this, new ExampleModifier());
	}
```

### Helper method documentation
For a full list of methods available to you for manipulating modifiers, check the individual method documentation in [CardModifierManager](https://github.com/daviscook477/BaseMod/blob/master/mod/src/main/java/basemod/helpers/CardModifierManager.java).

### Hook documentation
For full documentation of hooks, check [AbstractCardModifier](https://github.com/daviscook477/BaseMod/blob/master/mod/src/main/java/basemod/abstracts/AbstractCardModifier.java)'s page.


# Identifying Strings

One thing CardModifiers do differently from powers, cards, or relics is ID strings. Since the structure of CardModifiers is open-ended, I wanted unique identifying strings to be completely optional, and even offer the chance, via a method, to make it more dynamic, depending on the programmer's preferences.

At the very basic level, you implement an ID string by Overriding the method `identifier()`, and you use it with the `hasModifier()` and `getModifiers()` methods. What you want to use these for is up to you.

# Inherent Modifiers

Some features that `AbstractCardModifier` offer are things that you might want to make as part of a card, since it would be harder to implement it otherwise. For example, if you wanted a card to always able be cast with HP if you don't have the energy for it, you might use the CardModifier feature for that, but that modifier would also Override `isInherent()` and return true, in order to prevent the modifier from being copied, removed, or otherwise manipulated. If, for example, a card modifier was added to a card in its own constructor and the modifier was not inherent, that modifier would get duplicated every time the card gets added to your deck at the start of combat!

# Priority

Like powers, CardModifiers implement a priority system. priority is stored in an int, and, like powers, all calculations respect priority by order of smaller to greater. 

For example, if you had a modifier that added "foo" to the description and had priority 0, and another modifier that added "bar" and priority 1, the description would have "foobar" added to it.

# Card Descriptions

One particularly powerful feature is the ability to dynamically change descriptions without interfering with any other logic that cards might do to change the description.

When a CardModifier's `modifyDescription()` method is called, it is passed the card's current `rawDescription`. The String that you return is the string that will be used by the game as the card's final `rawDescription` to be parsed by `initializeDescription()`. How you want to affect the string is completely up to you, but do note that all regular formatting rules for card text still apply. As yet, localization is not directly supported by `AbstractCardModifier`, but it is possible to use the existing localization.

# Alternate Costs

note: Alternate costs have been moved to a standalone interface. To use them, your card modifier (or relic, power, other card, etc) must `implements AlternateCardCostModifier`

Perhaps the most powerful feature is the ability to implement alternate costs. `AbstractCardModifier` has several methods which support the creation of a way to make a card castable using just about any resource you can imagine, ranging from simple health or gold, to more abstract concepts like letters in the card's description. The methods allow complete freedom of what you count as a resource, and what qualifies as "spending" that resource.

### getAlternateResource

This method is used both to determine whether the card can be cast using an alternate resource, and how much of that resource (measured as energy-equivalent) is available. For no alternate resource, the default return is -1.

Example: the alternate resource is HP, two points of HP is worth 1 energy, and the player can't kill themselves casting it.
```java
    @Override
    public int getAlternateResource(AbstractCard card) {
        return (AbstractDungeon.player.currentHealth - 1) / 2;
    }
```

### prioritizeAlternateCost

This method is used to determine whether the game will attempt to consume this alternate resource before or after considering current energy. return true to make it try to use the alternate energy first.

Example: the alternate resource is stacks of a power called "notEnergy", so the programmer wants it to be spent first.

```java
    @Override
    public boolean prioritizeAlternateCost(AbstractCard card) {
        return true;
    }
```

### canSplitCost

This method is used to determine whether the game can spend partial amounts of this resource. Return true to make it able to be split between this and energy or other splittable resources. Note that this does play well with other alternate resources. For example, if you had one modifier allow you to spend 100 gold per energy before energy, a modifier that allowed you to spend 10 health per energy after energy, a card that costs 3, 1 energy, 150 gold, and 15 HP, you could cast the card for 1 energy, 100 gold, and 10 health, instead of 3 energy, or 300 gold, or 30 health.

### spendAlternateCost

This method is the meat of the alternate cost system, where you should handle how a resource is "spent". The main goal of the method is to remove as much of your resource as you can, up to the int that is passed to the method, and then return the remainder of the cost that you could not spend. **_It is very important that you spend resources based on the int passed, and not based on the card's costForTurn, because other CardModifiers may have already spent some of the energy required to play the card._**

Once you have spent as much energy as you can afford, _**you must return how much energy you didn't spend**_, so the rest of the CardModifiers can finish paying the cost.

Example: The alternate resource is any stacks of vulnerable after 5.

```java
    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend) {
        int resource = -1;
        if (AbstractDungeon.player.hasPower(VulnerablePower.POWER_ID)) {
            if (AbstractDungeon.player.getPower(VulnerablePower.POWER_ID).amount > 5) {
                resource = AbstractDungeon.player.getPower(VulnerablePower.POWER_ID).amount - 5;
            }
        }
        if (resource > costToSpend) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, VulnerablePower.POWER_ID, costToSpend));
            costToSpend = 0;
        } else if (resource > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, VulnerablePower.POWER_ID, resource));
            costToSpend -= resource;
        }
        return costToSpend;
    }
```

# Example CardModifiers

## CardModifier that increases the damage of a card by an amount for the next few times it's played.

```java
package examplemod.cardmodifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TempDamageModifier extends AbstractCardModifier {
    private int uses;
    private int damage;

    public TempDamageModifier(int uses, int damage) {
        this.uses = uses;
        this.damage = damage;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        --uses;
        return uses == 0;
    }
    
    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage + this.damage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempDamageModifier(uses, damage);
    }
}
```

## CardModifier that lets you spend 100 gold for each energy you're short of casting it, and marks itself in the card's description

```java
package examplemod.cardmodifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RipoffModifier extends AbstractCardModifier {
    private static int GOLD_COST = 100;

    public RipoffModifier() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL Can be cast for less energy at a cost of gold.";
    }

    @Override
    public int getAlternateResource(AbstractCard card) {
        return AbstractDungeon.player.gold / GOLD_COST;
    }
    
    //note: prioritizeAlternateCost defaults to false, so you don't have to override it if you want your resource spent after energy.
    
    @Override
    public boolean canSplitCost(AbstractCard card) {
        return true;
    }

    @Override
    public int spendAlternateCost(AbstractCard card, int costToSpend) {
        int resource = AbstractDungeon.player.gold / GOLD_COST;
        if (resource > costToSpend) {
            AbstractDungeon.player.loseGold(costToSpend * GOLD_COST);
            costToSpend = 0;
        } else if (resource > 0) {
            AbstractDungeon.player.loseGold(resource * GOLD_COST);
            costToSpend -= resource;
        }
        return costToSpend;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RipoffModifier();
    }
}
```