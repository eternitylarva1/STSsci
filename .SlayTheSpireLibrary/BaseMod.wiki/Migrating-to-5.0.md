```Java
@Override
public String getTitle(PlayerClass playerClass) {}
```
Should return class name as it appears next to your player name in game

```Java
@Override
public AbstractCard.CardColor getCardColor() {}
```
Should return the card color enum to be associated with your character.

```Java
@Override
public AbstractCard getStartCardForEvent() {}
```
Should return an instance of whatever basic rarity card you want to appear in the "gremlin match" event.

```Java
@Override
public Color getCardTrailColor() {}
```
Should return a color object to be used to color the trail of moving cards

```Java
@Override
public int getAscensionMaxHPLoss() {}
```
Should return how much HP your maximum HP reduces by when starting a run at ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)

```Java
@Override
public BitmapFont getEnergyNumFont() {}
```
Should return a BitmapFont object that you can use to customize how your energy is displayed from within the energy orb

```Java
@Override
public void doCharSelectScreenSelectEffect() {}
```
Method is called when your character's button is clicked within the character select screen. Example pulled from ironclad:
```Java
CardCrawlGame.sound.playA("ATTACK_HEAVY", MathUtils.random(-0.2f, 0.2f));
CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
```

```Java
@Override
public String getCustomModeCharacterButtonSoundKey() {}
```
Should return a String to act as a key for what sound effect plays when you click a character button from within the custom mods screen. Key points at hashmap `CardCrawlGame.sound.map`.

```Java
@Override
public String getLocalizedCharacterName() {}
```
Should return class name as it appears in run history screen.

```Java
@Override
public AbstractPlayer newInstance() {}
```
Should return a new instance of your character, sending this.name as its name parameter.

```Java
@Override
public String getSpireHeartText() {}
```
Should return a string containing what text is shown when your character is about to attack the heart. For example, the defect is "NL You charge your core to its maximum..."

```Java
@Override
public Color getSlashAttackColor() {}
```
Should return a Color object to be used as screen tint effect when your character attacks the heart.

```Java
@Override
public String getVampireText() {}
```
The vampire events refer to the base game characters as "brother", "sister", and "broken one" respectively. This method should return a String containing the _**full**_ text that will be displayed as the first screen of the vampires event. Ironclad, for example, returns this: 
```"Navigating an unlit street, you come across several hooded figures in the midst of some dark ritual. As you approach, they turn to you in eerie unison. The tallest among them bares fanged teeth and extends a long, pale hand towards you. NL ~\"Join~ ~us~ ~brother,~ ~and~ ~feel~ ~the~ ~warmth~ ~of~ ~the~ ~Spire.\"~"```

```Java
@Override
public Color getCardRenderColor() {}
```
Should return a Color object to be used to color the miniature card images in run history.

```Java
@Override
public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {}
```
Should return an AttackEffect array of any size greater than 0. These effects will be played in sequence as your character's finishing combo on the heart. Attack effects are the same as used in damage action and the like.

```Java
@Override
public ArrayList<String> getStartingDeck() {}, public ArrayList<String> getStartingRelics() {}
```
Functions the same as before, but methods are no longer static.

```Java
@Override
public CharSelectInfo getLoadout() {}
```
Method is no longer static, and should call "this" instead of your class enum for its 8th parameter.

Inside main mod class:
`BaseMod.addCharacter` has new parameters. Now needs an instance of the class you're adding, the card color enum, the file path to character button, file path to character portrait, and your custom class enum.
Example:
```Java
BaseMod.addCharacter(
    new MyCustomCharacter(CardCrawlGame.playerName),
    MyCardColorEnum.CHARACTER_COLOR,
    PATH_TO_CHAR_BUTTON,
    PATH_TO_CHAR_PORTRAIT,
    MyCharacterEnum.CHARACTER_CLASS
);
```