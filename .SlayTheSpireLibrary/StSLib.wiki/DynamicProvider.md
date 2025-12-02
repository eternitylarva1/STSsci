A system is in place to allow you to more easily make use of Basemod's dynamic variables system in a way that need not be attached to any specific self-implementation on a card. You could use Dynamic Provider to make a power or a card modifier use a dynamic variable on a card, for example, and `DynamicDynamicVariable` will handle all the overhead of making sure the right value finds the right dynamic variable.

To make use of this, you simply implement `DynamicProvider` on any class and handle the inherited methods the way you would handle a dynamic variable's methods, except attached to your class. Additionally, you are required to implement an `UUID` variable to uniquely identify the proper instance of `DynamicProvider`.

Next, whenever a `DynamicProvider` comes to affect a new card, you register it with `DynamicDynamicVariable.register`.

And finally, you use this dynamic variable inside card text using the auto-generated key in any place you might normally manipulate the card's text, such as from inside a cardmod or through basemod's subscriber. You can easily retrieve the variable's key by calling `DynamicProvider.generateKey`

an example cardmod might look like this:
```java
public class ExampleCardmod extends AbstractCardModifier implements DynamicProvider {
    private final UUID id;
    private int baseDamage, damage;

    public ExampleCardmod(int damage) {
        id = UUID.randomUUID();
        this.baseDamage = this.damage = damage;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL Deal " + DynamicProvider.generateKey(card, this, true) + " damage. This amount scales only with Dexterity.";
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        DynamicDynamicVariable.registerVariable(card, this);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        super.onUse(card, target, action);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(AbstractDungeon.player, damage)));
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        AbstractPower dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        if (dexterity != null) {
            damage = baseDamage + dexterity.amount;
        }
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        AbstractPower dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        if (dexterity != null) {
            damage = baseDamage + dexterity.amount;
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExampleCardmod(baseDamage);
    }

    @Override
    public UUID getDynamicUUID() {
        return id;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return damage != baseDamage;
    }

    @Override
    public int value(AbstractCard card) {
        return damage;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return baseDamage;
    }
}
```
The above code, when attached to a strike in game, looks like this:
![example code implemented in game screenshot](https://i.imgur.com/hDBubwU.png)

You can find a more complicated example of how to implement it in [`ExtraEffectModifier`](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/extraeffects/ExtraEffectModifier.java).