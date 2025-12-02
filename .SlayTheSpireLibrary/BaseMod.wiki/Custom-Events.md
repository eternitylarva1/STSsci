# Creating an Event.

The first thing you want to do is create a class that extends AbstractEvent (or any abstract class that extends it) 

Here is a very simple example of an event.

```java
public class MyFirstEvent extends AbstractImageEvent {
    public static final String ID = "My First Event";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    //This text should be set up through loading an event localization json file
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public MyFirstEvent() {
        super(NAME, DESCRIPTIONS[0], "img/events/eventpicture.png");
        
        //This is where you would create your dialog options
        this.imageEventText.setDialogOption(OPTIONS[0]); //This adds the option to a list of options
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //It is best to look at examples of what to do here in the basegame event classes, but essentially when you click on a dialog option the index of the pressed button is passed here as buttonPressed.
    }
}
```

A more advanced example of a custom event can be found [Here](https://github.com/GraysonnG/InfiniteSpire/blob/master/src/main/java/infinitespire/events/EmptyRestSite.java)

[BaseMod also provides an abstract class `PhasedEvent` which can simplify the process of making events.](#phasedevent) It also makes the creation of events with combat involved (like the Colosseum) easier, as it provides customization regarding rewards and how the event proceeds after combat. To make an event with combat otherwise would require you to create patches for your event to work correctly.

# Adding the Event

Now that you have created your event, you will want to add that event to one of the games event pools. Thankfully that process is now handled by BaseMod.

All you need to do is call this method in receivePostInitialize: 

`BaseMod.addEvent(String eventID, Class<? extends AbstractEvent> class, String dungeonID)`
* `eventID` : The ID of the event. This should be the same as in your eventStrings.json
* `class` : The class of the event you are adding.
* `dungeonID` : The ID of the dungeon whose pool you want to add the event to. (e.g. `Exordium.ID`, `TheCity.ID`, `TheBeyond.ID`). Leaving off this argument adds the event to all pools.

A quick example using the method above would be:

```java
@Override
public void receivePostInitialize() {
    BaseMod.addEvent(MyFirstEvent.ID, MyFirstEvent.class);
    BaseMod.addEvent(MySecondEvent.ID, MySecondEvent.class, TheCity.ID);
}
```

# Custom Event Conditions

In some cases, you might want your event to have more specific spawning conditions or change what kind of event it is. For that, you should use this method:

`BaseMod.addEvent(AddEventParams params)`
* `AddEventParams` : The detailed parameters for the event to add. This should be built using `AddEventParams.Builder`.

The method should be called as
`BaseMod.addEvent(new AddEventParams.Builder(eventID, eventClass).create());`

Additional methods to add more conditions should be appended on the builder before the `create()` call. For example:

```java
BaseMod.addEvent(new AddEventParams.Builder(MySecondEvent.ID, MySecondEvent.class).dungeonID(TheCity.ID).create());
```

### Builder methods:

* `dungeonID(String dungeonID)` `dungeonIDs(String... dungeonIDs)` : Limits the event to appearing in the specified acts. If not specified, the event can appear in any act. For one time events, this doesn't function as they are all added to the event list at the start of the game. If you wish to limit the acts for a one time event, use the bonusCondition.

* `playerClass(AbstractPlayer.PlayerClass playerClass)` : Limits the event to appearing when playing the specified character.

* `spawnCondition(Condition spawnCondition)` : This lets you implement any condition you want. This will be checked at the start of an act to determine whether the event should be added to the pool.

* `bonusCondition(Condition bonusCondition)` : This lets you implement any condition you want. This will be checked when a random event is rolled to determine whether the event can currently appear.

* `overrideEvent(String overrideEventID)` : This will cause the event to override the specified event. There are two modes of replacement: Override and Full Replace.

   Override will conditionally replace the event when it is encountered using bonusCondition. If the conditions fail, the normal event will occur. Spawn condition has no effect, as spawning is based on the original event.

   Full Replace will perform the spawnCondition check when the dungeon is instantiated, completely replacing the original event if it returns true. If it returns false, the original event will appear as normal.

   By default, the Override type will be used. To use Full Replace, specify the type using the next method:

* `eventType(EventUtils.EventType eventType)` : This specifies the type of event. 

   `EventType.NORMAL` : Normal events can only be seen once, regardless of where they can appear.

   `EventType.ONE_TIME` : SpecialOneTimeEvents can only be seen once, but are rarer.

   `EventType.SHRINE` : Shrines can be seen once per act (but are added back to the list when save and quit occurs). Shrines are also rarer than normal events.

   `EventType.OVERRIDE/EventType.FULL_REPLACE` : As described for `overrideEvent`. If this type is used but an override event is not specified, it will be treated as a normal event.

* `endsWithRewardsUI(bool value)` : If your event ends with rewards UI, which usually means the event ends with a combat, you should set the value to true. Thus, when you click "Skip Rewards" button, the rewards UI won't close and map UI will popup. You can go back to rewards from map UI after that.

# PhasedEvent

https://github.com/daviscook477/BaseMod/blob/master/mod/src/main/java/basemod/abstracts/events/PhasedEvent.java

In a normal event, you process input in the `buttonEffect` method by determining what button was pressed based on the button's index and the current screen ID.

The `PhasedEvent` class treats each individual interaction as a separate "phase" which can have their properties individually defined, with the `buttonEffect` method handled by the `PhasedEvent` class.

```java
public class PhasedEventExample extends PhasedEvent {
    public static final String ID = "Phased Event Example";
    //These eventStrings should be defined in a json file and loaded in your main mod file. See https://github.com/daviscook477/BaseMod/wiki/Custom-Strings
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public PhasedEventExample() {
        super(ID, title, "img/events/eventpicture.png"); //Note, this constructor is different than that of a normal event.
        
        //This is where you define all the event's phases.
        registerPhase("Start", new TextPhase("Body Text").addOption("Option 1", (i)->transitionKey("Phase 2")).addOption("Option 2", (i)->transitionKey("Phase 3")));
        //This example registers a TextPhase, which is the standard event screen with some text and buttons you can click.
        //The event's text should be from DESCRIPTIONS and OPTIONS. For this example, it will have two buttons labeled "Option 1" and "Option 2", which will take you to Phase 2 and Phase 3 respectively.

        registerPhase("Phase 2", new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->openMap()));
        //This is another TextPhase with only one option, which will open the map, which ends the event.

        registerPhase("Phase 3", new CombatPhase(MonsterHelper.CULTIST_ENC)
                .addRewards(true, (room)->room.addRelicToRewards(AbstractRelic.RelicTier.RARE))
                .setNextKey("Phase 2"));
        //This will cause the player to enter combat when Phase 3 starts.
        //The combat will have a card reward and a random rare relic reward.
        //After combat ends, it will transition to Phase 2.
        //CombatPhase has many customization options. Look at the class for details.

        //This sets the starting point of the event.
        transitionKey("Start");
    }
}
```

This example uses Strings for each key, but you can use anything.
The last type of phase (unused in this example) is InteractionPhase. It should be used for things like the card matching game, as it allows you to update/render in any way you wish to process the player's input.

In general: When a phase ends, it should call `transitionKey` to move to another phase, or `openMap` to end the event. If neither happens, your event will cause a softlock.

A more complex example can be found [here](https://github.com/erasels/MtSAnniversary-BattleTowers/blob/main/BattleTowers/src/main/java/BattleTowers/events/CoolExampleEvent.java).