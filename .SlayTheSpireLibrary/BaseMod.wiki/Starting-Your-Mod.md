Congratulations! If you've made it this far you've finished setting up your development environment and should be ready to actually start developing your mod. Before continuing we're going to go over some of the fundamentals of how ModTheSpire and BaseMod work so you can use them to make your mod. ModTheSpire is the modloader for Slay The Spire and allows you to directly make changes to the game's code. This process is a bit tedious and subject to changes **every** time Slay The Spire is patched but may be necessary for some particularly complex features that mods try to add. BaseMod provides a more convenient **API** so mods for the most part can avoid directly changing the game's code. As such you are likely to use BaseMod and ModTheSpire in combination for modding projects. BaseMod's **API** is focused around the idea of firing events and having mods subscribe to/listen for those events. For example if you want to make a mod that heals the player 5 HP every time a card is exhausted you would use BaseMod to subscribe to the `PostExhaust` event and then you would be able to listen for that event using an event handler. In this example we will be creating a simple mod that shows how to use BaseMod's subscription system. In the future there may be tutorials showing how to use the Custom Character system and GUI but for now if you wish to use those systems take a look at the documentation here on the wiki and other mods that already use them.

## Adding your Mod Info

Before you start your mod, be sure to add mod info so MTS can see it. The file goes into the root of your `src/main/resources` folder and you can see how to format the file [here](https://github.com/kiooeht/ModTheSpire/wiki/ModInfo).

## @SpireInitializer
To make a mod that works with BaseMod you're going to want to start out with some boilerplate code.

```Java
package example_mod;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class ExampleMod {

    public ExampleMod() {
        // TODO: make an awesome mod!
    }

    public static void initialize() {
        new ExampleMod();
    }

}
```

Note: If Eclipse can't find `@SpireInitializer` you may have to go through the **Dependencies** step again since Eclipse seems to reset it when you changes source folders.

So what the above code is doing is telling ModTheSpire that we have a class called `ExampleMod` that has a method called `initialize` that we would like to have called once mods are loaded but **before** the game starts up. Inside that `initialize` method we create an instance of our mod and inside that instance is where we're going to be telling BaseMod about the different events we want to listen for.

## ISubscriber
For this example we're going to be counting the number of times a card is exhausted in each battle and then report that value to the log. We will also be tracking the total number of cards exhausted in each act and reporting that after each battle too. Basically this mod could figure out that the player exhausted 2 cards in the 1st battle, then 3 cards in the 2nd battle which is 5 cards overall, etc.. To do this we will need to use three events that BaseMod provides: `PostExhaust`, `PostBattle`, `PostDungeonInitialize`

We will be using those hooks as follows. In `PostDungeonInitialize` we will set the number of cards exhausted this act to 0. In `PostBattle` we will print the total number of cards exhausted and the number of cards exhausted this battle. In `PostExhaust` we will count the number of cards exhausted.

To setup our mod to subscribe to these events we need our `ExampleMod` class to implement the corresponding interfaces: `PostExhaustSubscriber`, `PostBattleSubscriber`, `PostDungeonInitializeSubscriber`. (You can find the full list of such interfaces on the [Hooks page](https://github.com/daviscook477/BaseMod/wiki/Hooks).) Then we tell BaseMod that we want to listen for those events by using `BaseMod.subscribe(this)`.

```Java
package example_mod;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostExhaustSubscriber;

@SpireInitializer
public class ExampleMod implements PostExhaustSubscriber,
	PostBattleSubscriber, PostDungeonInitializeSubscriber {

	public ExampleMod() {
		BaseMod.subscribe(this);
	}
	
	public static void initialize() {
		new ExampleMod();
	}
	
	@Override
	public void receivePostExhaust(AbstractCard c) {

	}
	
	@Override
	public void receivePostBattle(AbstractRoom r) {
		
	}
	
	@Override
	public void receivePostDungeonInitialize() {
		
	}
	
}
```

## Mod Logic

```Java
package example_mod;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostExhaustSubscriber;

@SpireInitializer
public class ExampleMod implements PostExhaustSubscriber,
	PostBattleSubscriber, PostDungeonInitializeSubscriber {

	private int count, totalCount;
	
	private void resetCounts() {
		totalCount = count = 0;
	}
	
	public ExampleMod() {
		BaseMod.subscribe(this);
		resetCounts();
	}
	
	public static void initialize() {
		new ExampleMod();
	}
	
	@Override
	public void receivePostExhaust(AbstractCard c) {
		count++;
		totalCount++;
	}
	
	@Override
	public void receivePostBattle(AbstractRoom r) {
		System.out.println(count + " cards were exhausted this battle, " +
			totalCount + " cards have been exhausted so far this act.");
		count = 0;
	}
	
	@Override
	public void receivePostDungeonInitialize() {
		resetCounts();
	}
	
}
```