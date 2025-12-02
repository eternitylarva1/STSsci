# Custom Strings
To centralize all card, relic, power, events, UI, etc text into one place for ease of reading for other modders, or for ease of making your mod translatable the game loads in your strings from a JSON file.

# API
`loadCustomStringsFile(Class class, String filePath)` - Only call this in `receiveEditStrings` callback that you get from the `EditStringsSubscriber` or else your keywords may not be initialized properly.

`class` - Should be the class file of the kind of strings you are using. A full list of strings can be found below.

`filePath` - A file path to your JSON.

You can then get an object containing your strings by calling `CardCrawlGame.languagePack.get(StringType)(myKey)`.
Replacing (StringType) with the proper type.

# List of String Types

```
MonsterStrings
PowerStrings
CardStrings
RelicStrings
EventStrings
PotionStrings
CreditStrings
TutorialStrings
KeywordStrings
ScoreBonusStrings
CharacterStrings
UIStrings
OrbStrings
RunModStrings
BlightStrings
AchievementStrings
```

# Example

```java
 @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "example_mod/localization/ExampleModRelicStrings.json");
        // Loads in strings that contain your relic name, descriptions and flavor.
    }
```

```java
private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(myCardID);
// Gets the name and descriptions of a card by its ID from your CardStrings.
```