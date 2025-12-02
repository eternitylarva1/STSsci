**SEE:** https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0

# Enums

The base game denotes character options using the `AbstractPlayer.PlayerClass` enum so in order to add a new character to the game you must patch the enum using `ModTheSpire`'s enum patching feature in addition to registering it with **BaseMod**. Here is an example of how to do so:

```java
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class MyPlayerClassEnum {

	@SpireEnum
	public static AbstractPlayer.PlayerClass MY_PLAYER_CLASS;
	
	
}

```

# Requirements
1. (prereq) Register a new custom color with BaseMod for the player - take a look here to see how (https://github.com/daviscook477/BaseMod/wiki/Custom-Colors)
2. Define a custom player class that has a public static CharSelectInfo getLoadout() method
3. Register the custom player with BaseMod

# API
Note that `addCharacter` should only be called in the `receiveEditCharacters` callback of `EditCharactersSubscriber`.

`addCharacter(Class characterClass, String titleString, String classString, String color, String selectText, String selectButton, String portrait, String characterID)`
* `character` - An instance of your character
* `color` - The name of the custom color for this character, e.g. `MY_CUSTOM_COLOR.toString()` where `MY_CUSTOM_COLOR` is the enum value for this character's color
* `selectButtonPath` - The path to the select button texture (starting at the root of your jar)
* `portraitPath` - The path to your character select portrait texture (starting at the root of your jar) (size: 1920px x 1200px)
* `characterID` - Should be `MY_PLAYER_CLASS` where `MY_PLAYER_CLASS` is the enum value for this character's class

# Example

Too add the custom player you must do two steps - define the custom player and register it with **BaseMod**.

## Define a Custom Player

```java
import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class MyCharacter extends CustomPlayer {
	public static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
	public static final String MY_CHARACTER_SHOULDER_2 = "img/char/shoulder2.png"; // campfire pose
        public static final String MY_CHARACTER_SHOULDER_1 = "img/char/shoulder1.png"; // another campfire pose
	public static final String MY_CHARACTER_CORPSE = "img/char/corpse.png"; // dead corpse
        public static final String MY_CHARACTER_SKELETON_ATLAS = "img/char/skeleton.atlas"; // spine animation atlas
        public static final String MY_CHARACTER_SKELETON_JSON = "img/char/skeleton.json"; // spine animation json

	public MyCharacter (String name) {
		super(name, MyPlayerClassEnum.MY_PLAYER_CLASS);
		
		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values
		
		initializeClass(null, MY_CHARACTER_SHOULDER_2, // required call to load textures and setup energy/loadout
				MY_CHARACTER_SHOULDER_1,
				MY_CHARACTER_CORPSE, 
				getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
		
		loadAnimation(MY_CHARACTER_SKELETON_ATLAS, MY_CHARACTER_SKELETON_JSON, 1.0F); // if you're using modified versions of base game animations or made animations in spine make sure to include this bit and the following lines
		
		AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	public static ArrayList<String> getStartingDeck() { // starting deck 'nuff said
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add("MyCard0");
		retVal.add("MyCard0");
		retVal.add("MyCard0");
		retVal.add("MyCard0");
		retVal.add("MyCard1");
		retVal.add("MyCard1");
		retVal.add("MyCard1");
		retVal.add("MyCard1");
		retVal.add("MyCard2");
		return retVal;
	}
	
	public static ArrayList<String> getStartingRelics() { // starting relics - also simple
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add("MyRelic");
		UnlockTracker.markRelicAsSeen("MyRelic");
		return retVal;
	}
	
        public static final int STARTING_HP = 75;
        public static final int MAX_HP = 75;
        public static final int STARTING_GOLD = 99;
        public static final int HAND_SIZE = 5;

	public static CharSelectInfo getLoadout() { // the rest of the character loadout so includes your character select screen info plus hp and starting gold
		return new CharSelectInfo("My Character", "My character is a person from the outer worlds. He makes magic stuff happen.",
				STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE,
			this, getStartingRelics(), getStartingDeck(), false);
	}
	
}
```

## Add to BaseMod

Note that for this to work your mod must add the necessary custom color for the character like the wiki page on custom colors says.

```java
@SpireInitializer
public class MyMod implements EditCharactersSubscriber {
	
        public MyMod() {
                BaseMod.subscribeToEditCharacters(this);
        }

        public static void initialize() {
                MyMod mod = new MyMod();
        }

        @Override
	public void receiveEditCharacters() {
		logger.info("begin editing characters");
		
		logger.info("add " + MyPlayerClassEnum.MY_PLAYER_CLASS.toString());
		BaseMod.addCharacter(new MyCharcter(CardCrawlGame.playerName),
				MY_CHARACTER_BUTTON,
				MY_CHARACTER_PORTRAIT,
				MyPlayerClassEnum.MY_PLAYER_CLASS);
		
		logger.info("done editing characters");
	}

}
```