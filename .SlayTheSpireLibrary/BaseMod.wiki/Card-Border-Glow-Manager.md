Basemod offers a feature where you can set "rules" which can change what color a card will glow. If multiple rules exist, or if the card itself has changed its own glow to something non-standard, the system will automatically split the glow evenly between all the colors so that all effects will be visible to the player.

These rules are unmanaged. Once you add one to the list, it will exist in the game until you remove it from the list yourself, or the game is restarted.

To add a rule, you simply pass an instance of `GlowInfo` to `CardBorderGlowManager.addGlowInfo`. It can easily be done via a lambda expression, like so:

```java
        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard card) {
                //return true if "card" follows this rule, else return false
            }

            @Override
            public Color getColor(AbstractCard card) {
                //return an instance of Color to be used as the color. e.g. Color.WHITE.cpy().
            }

            @Override
            public String glowID() {
                //return a string to be used as a unique ID for this glow.
                //It's recommended to follow the usual modding convention of "modname:name"
            }
        });
```

Aside from those methods, the class has a priority variable available to it which will affect what order the different colors may appear on the card.

because adding the glow is permanent, there is variance in how you want to handle it. For effects that are general rules (for example, you have a special card type that glows pink when its condition is fulfulled), you might call `.addGlowInfo` after your mod gets initialized.

For any temporary glows, you will want to later call `CardBorderGlowManager.removeGlowInfo`. The method takes either an instance of GlowInfo to remove that instance directly, or an ID to remove the info if it exists.

You can also retrieve an existing GlowInfo by ID with `CardBorderGlowManager.getGlowInfo`.