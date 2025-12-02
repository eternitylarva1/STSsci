Adding an entirely new effect to a card is made _easier_ with cardmods, however, getting those effects to scale as the player might expect and _displaying_ that scaling is more cumbersome to do.

`ExtraEffectModifier` is intended to make use of [`DynamicProvider`](https://github.com/kiooeht/StSLib/wiki/DynamicProvider) to help make both of these things much easier to implement in a standardized fashion. It also includes features intended to make it easier to handle the stacking of similar modifiers.

An extending class must implement a makeCopy as is standard with cardmods and must also implement an ID to be used for checking stackability, and then must specify what extra effects happen when `use` is called as well as what text will be added to the card.

`ExtraEffectModifier` helps you by automatically formatting this text, so you only need to return a string where `%s` is in place where you would normally put the dynamic variable.

An example extra effect modifier (skipping proper localization) might look like this:

```java
public class ExampleExtraEffect extends ExtraEffectModifier {
    private static final String ID = "stslib:ExampleExtraEffect";

    public ExampleExtraEffect() {
        super(VariableType.DAMAGE, 6);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m, UseCardAction useAction) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, value(card))));
    }

    @Override
    public String getExtraText(AbstractCard card) {
        return "Deal %s damage.";
    }

    @Override
    public String getEffectId(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExampleExtraEffect();
    }
}
```
The above code results in this when applied to a strike, including automatic stacking behavior:
![example code shown in game screenshot](https://i.imgur.com/TrCpO8d.png)

Full documentation of the features available to `ExtraEffectModifier` can be found in its javadocs [here](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/extraeffects/ExtraEffectModifier.java).