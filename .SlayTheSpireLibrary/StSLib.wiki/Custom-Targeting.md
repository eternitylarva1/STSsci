In some cases, you may want a card that you can aim (targeting arrow) at something other than an enemy.
This is what custom targeting is for.

Three things are required.
1. A TargetingHandler
2. A CardTarget enum
3. Registering your custom targeting

For an example, this is an implementation of targeting the player or an enemy, which you can use if it happens to be what you need.
https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java

At the top, you can see
```
    @SpireEnum
    public static AbstractCard.CardTarget SELF_OR_ENEMY;
```
This is the `CardTarget` for this handler.

To properly create a [TargetingHandler](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/TargetingHandler.java), you need at minimum the following:

1. Extend the `TargetingHandler` class with an appropriate type parameter depending on what you want to target. In the [example](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L20), this is `AbstractCreature`, as both the player and enemies extend this class.

2. Define the following methods:
```
    public abstract void updateHovered(); //Update/check what valid target is currently hovered
    public abstract ? getHovered(); //Returns what is currently hovered (should just be null if none). ? is whatever the class you're targeting is.
    public abstract void clearHovered(); //Clear current target when leaving targeting mode
    public abstract boolean hasTarget(); //Whether a valid target is hovered
```

Everything else is optional. Some of it for keyboard input support, and some of it for visuals.

In Slay the Spire, targeting works in two steps.
First, while you're dragging the card and aiming the arrow, it tracks what target you're currently hovering.
Then, when you release, the card is queued with that currently hovered target.

So, the first step is to create a tracking variable in your class that extends `TargetingHandler` to store the currently hovered target.
You can see this [here](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L28) in the example.

Next, is the `updateHovered` method. This is the method that actually will check the possible targets to see what's currently hovered.
In [this](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L36) case, the targets are the player or an enemy, so it checks those and stores the first one that's hovered.

Then there's the [getHovered](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L57), [clearHovered](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L62), and [hasTarget](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/cards/targeting/SelfOrEnemyTargeting.java#L31) methods. These are relatively self-explanatory, just performing basic actions on the hovered target variable.

Then the last step: registering it. This can be done at basically any time, but `postInitialize` is a good option.
You can see this [here](https://github.com/kiooeht/StSLib/blob/master/src/main/java/com/evacipated/cardcrawl/mod/stslib/StSLib.java#L61); register your `CardTarget` and an instance of your `TargetHandler`.

## Using Custom Targeting

First, it would need to use the `CardTarget`. Set the card's CardTarget to your defined enum like you would with any other targeting option.
Then, in the `use` method, access where you've stored the card's target. In the case of `SelfOrEnemyTargeting`, it might look like
```
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);

        if (target == null)
            target = AbstractDungeon.player;

        addToBot(new GainBlockAction(target, p, this.block));
    }
```

You should always include a null check for safety. It's hard to guarantee that the card won't get played through some unusual method that doesn't target it, such as Mayhem, which only assigns a random monster target. Depending on your setup, you could have `getTarget` return a random valid target if it would return null.