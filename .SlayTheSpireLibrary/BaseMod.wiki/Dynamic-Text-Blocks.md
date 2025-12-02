# Dynamic Text Blocks
There may be times when you want the text of your card to change based on the value of a variable. For an example in English: you draw 1 card, but draw 2 cards. Dynamic text blocks will allow you to make these changes without needing to use any code, so translators can make use of this system without needing any additional work from the mod author.

Dynamic text blocks allow you to define a set of checks with cases based on any registered dynamic variable (!D!, !M!, !B!, !YourModID:YourVariable!, etc.), as well as 2 additional options, discussed below. There is no limit to the amount of blocks that can be in a card description, but they cannot be nested within each other. 

To use these blocks, your card string must contain the sequence `{@@}` somewhere within it. Common convention is at the beginning of the string as an immediate indicator to anyone reading it. Dynamic text can also be used in Card Modifiers, provided they add the sequence to the description. The format of any dynamic block is `{Variable|Check=Case|Check=Case|...etc}`, where `Variable` is any registered dynamic variable, `Check` is a series of numbers or symbols that starts with `|` to compare the variable to, `Case` is the text the block should return if the check is met, and the entire block is enclosed within `{}` You may continue to chain as many checks and cases as you need within 1 block.

## Variables ##
The `Variable` in any dynamic text block is written exactly like using any dynamic variable in text. Damage can be grabbed with !D!, Block via !B!, Magic number via !M!, and any dynamic variable registered with Basemod will work as well. In addition to these, there are 2 extra lookups you can use as the variable, !Location! and !Upgrades!.

As !Location! and !Upgrades! are not actual dynamic variables, they instead return where the card is, or how many times the card has been upgraded.
The outputs of !Location! are as follows:
* -2 = Compendium, or otherwise rendered while not actively in a run
* -1 = Master Deck
* 0 = Hand, Limbo
* 1 = Draw Pile
* 2 = Discard Pile
* 3 = Exhaust Pile
* 4 = A card rendered mid run that is not owned by the player (such as in shops, events, reward screens)

Of note, if the card happens to have Branching Upgrades, the output of !Upgrades! can be positive or negative, as the main upgrade path _increments_ the amount of times it was upgraded, while the second upgrade path _decrements_ it instead:
* 2 = Upgraded twice (Cards like Searing Blow can be upgraded multiple times)
* 1 = Upgraded once (or a branching card upgraded via the main upgrade path)
* 0 = Not upgraded
* -1 = Branching card upgraded via the second upgrade path
* -2 = Branching second upgrade path done twice, etc.

By default, branching cards do not allow for mixed multiple upgrades (like a card that can be upgraded multiple times with taking the main path once, then taking the second path on the next upgrade). However, caution should be used on cards that expressly override this limitation to allow this, as the mixed upgrade types can cancel out.


## Checks ##
The `Check` of a dynamic text block always begins with |, to denote it as a new check, and is compared against the value of the dynamic variable that has been provided. Should a check match, the output of the dynamic text block will be its case. If more than 1 check is matched, the right most match will be returned, with the exception of the default case, which will return if no other case is matched. Should no case be matched, the block will return an empty string.

The formatting options for checks are as follows:
* Default Output: |@=This will return if no other case is matched
* Exact Match: |1=Single
* Greater Than: |>10=Many
* Less Than: |<2=Single
* Divisible By: |%2=Even
* Ends With: |&5=Happens to end in a 5
* Repeat: |repeat=Similar to @, but repeat M times

To save on space, multiple checks can be separated by commas: |%2,%3=Divisible by 2 or 3

## Cases ##
`Cases` are the simplest part of the block, its simply what text will the block be replaced with should its accompanying check be matched. Cases can be of any length and may include energy orbs with [E], any dynamic variables can be used, and keywords will also render. As previously mentioned, dynamic text blocks cannot be nested inside the case of another block. This limitation was implemented as the readability of a card reduces drastically as nested text blocks are introduced.

## Putting it all together ##
To use the previous example, you could use the following to input the correct word based on the variable dictating how many cards will be drawn:
```json 
"{@@}Draw !M! {!M!|@=card.|>1=cards.}"
``` 
In this case, the card will default to saying "card.", but if the variable happens to be greater than 1, it will instead say "cards.". This, however, is not the most efficient use case, as we can also write this as the following:
```json
"{@@}Draw !M! card{!M!|>1=s}."
```
This block will add the 's' in-between "card" and "." to make "cards." if the variable is greater than 1, but add nothing if it is not greater than 1, collapsing instead into "card.".

This specific example would be useful in a case where you have a card with draw 1 that upgrades into draw 2. Instead of writing a second UPGRADE_DESCRIPTION, you can let the text dynamically update. As a bonus: if any other mechanic alters your card to increase the magic number on it, you wont be stuck with a weird "Draw 2 card" until you upgrade it to set the new string.

## Note for translators ##
If you are a translator and the mod you are working with uses dynamic text for pluralization, but you do not need it in your language, you can simply remove it along with the {@@}, and proceed as normal. Likewise, if your language needs complicated pluralization or word ending modifications (like in Russian and Polish), you can simply add {@@} to the translated cardstrings and proceed with using dynamic text blocks as needed. By design, the functionality of the entire system is based on {@@} being present in the text rather than needing any special work or communication from the mod author.

# Examples
```json
"General use case for something that normally draws 1"
"DESCRIPTION": "{@@}Gain !B! Block. NL Draw !M! card{!M!|>1=s}."

"Example of using repeat to show energy icons from magic number."
"DESCRIPTION": "{@@}Gain{!M!|repeat= [E]|>4= !M! [E]}."

"Example with 2 dynamic text blocks and where a dynamic variable is used inside a case"
"DESCRIPTION": "{@@}The first {!M!|@=Orb|>1=!M! Orbs} you Channel each turn {!M!|@=is|>1=are} Evoked."

"Example of using a registered dynamic variable"
"DESCRIPTION": "{@@}Apply !M! Vulnerable and Lock-On. NL Draw !M10Robot:SecondMagic! card{!M10Robot:SecondMagic!|>1=s}."

"Example used to pluralize a keyword. Make sure you actually register pluralized keyword!"
"DESCRIPTION": "{@@}Deal !D! damage. NL Channel !M! m10robot:Bit{!M!|>1=s} for each Attack played this turn."

"Example to add extra text if a card is in your hand, similar to how Finisher and Flechettes work."
"Note that you will need to put the value you care about (total damage, cards played, skills in hand, etc.) into a dynamic variable for this to work."
"DESCRIPTION": "{@@}Deal !D! damage for each Skill played this turn.{!Location!|0= NL (Deals !M10Robot:SecondDamage! damage)}"
```

# Custom Checks
There may be a time where you need to dynamically use text, but simply using a dynamic variable is insufficient. For this, you can define your own functions to use in dynamic text, where you can control the output of the function for the checks you need.

To begin, call the `DynamicTextBlocks.registerCustomCheck()` method in your main mod file (receivePostInitialize or receiveEditCards are good candidate locations to do this). This method takes a `String`, which will be the identifier to your custom function, and a `Function<AbstractCard, Integer>` which allows you to input a lambda that takes a card and returns what values your function will output. 

As an example, the following custom check will be named `YourMod:hasStrength` and will return 1 if the payer happens to have the positive Strength power, -1 if they happen to have negative Strength, and 0 if they do not have Strength (or if the player does not exist).

```java
DynamicTextBlocks.registerCustomCheck("YourMod:hasStrength"), card -> {
    if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
        if (AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount > 0) {
            return 1;
        }
        return -1;
    }
    return 0;
});
```

To then use this check in your card we can add a dynamic text block using the name of your custom check, and what text to add for the -1, 0, and 1 output values we set it to return. Do note the name follows the same format as using a dynamic variable and is wrapped in `!`.
```json
  "${ModID}:Strike": {
    "NAME": "Strike",
    "DESCRIPTION": "{@@}Deal !D! damage.{!YourMod:hasStrength!|1= NL I am very strong!|-1 = NL Plz don't hurt me.}"
  }
```
An advantage of having full control over the output values is that it allows you to define any level of complexity for your cases. For example, should you want the previous example to only show the Strength related flavor text while in your hand, you can add that restriction to your function like so:
```java
DynamicTextBlocks.registerCustomCheck("YourMod:hasStrength"), card -> {
    if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(StrengthPower.POWER_ID) && AbstractDungeon.player.hand.contains(card)) {
        if (AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount > 0) {
            return 1;
        }
        return -1;
    }
    return 0;
});
```