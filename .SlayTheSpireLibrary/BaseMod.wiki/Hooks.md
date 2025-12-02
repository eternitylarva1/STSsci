# Hooks

## Subscription Model
BaseMod uses a subscription model to handle hooks. If you want to receive a specific hook you `subscribeTo` it.
If you want to stop receiving the event you `unsubscribeFrom` it.

You should generally only use BaseMod subscribers in your main mod file (the one with @SpireInitialize), and then call the desired method(s) from the subscriber method in your main mod file. This is to prevent multiple instances of the same thing from subscribing repeatedly and never unsubscribing.
 
### New Subscription Example
* `BaseMod.subscribe(this)`
    * where `this` is the instance of some subscriber
    * `this` should be the `SpireInitializer` instance
    * this method will subscribe `this` to __all__ hooks it implements subscriber for
* `BaseMod.unsubscribe(this)`
    * this method will unsubscribe `this` from __all__ hooks it implements subscriber for
* `BaseMod.subscribe(this, Class<? extends ISubscriber> toAddClass>)`
    * this method will subscribe `this` to __only__ the `toAddClass` hook
* `BaseMod.unsubscribe(this, Class<? extends ISubscriber> toRemoveClass)`
    * this method will unsubscribe `this` from __only__ the `toRemoveClass` hook
    
### Old Subscription Example (Deprecated API)
* `BaseMod.subscribeTo...(this)`
* `BaseMod.unsubscribeFrom...(this)`

### UnsubscribeLater
Sometimes you need to unsubscribe from an event after it's triggered once. Maybe you want listen only for the first time the event is triggered.

In that case you're going to want to `unsubscribe` from a hook __in__ the callback method for the hook. 
However if you were to directly call `BaseMod.unsubscribe` in your `recieve...` method 
you will throw a `ConcurrentModificationException` which is what happens in Java when you try and modify a list at the same time it is being iterated over.

To avoid this exception you need to use `BaseMod.unsubscribeLater` which will register your subscriber to be unsubscribed __after__ the bit of code that could throw the exception.

---

## Overview
* You can get the Interface with Name+`Subscriber` and the method with `receive`+Name.
   * Exceptions are written out.
* Occurence: Before --> Pre --> Post --> After
   * Pre & Post let's you modify the event objects

### Adder
* Here you can add custom content to the game.
* The content shouldn't be added somewhere else.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|AddAudio| | |Register audio here.|With `BaseMod.addAudio`.<br/>Can be used later with `SFXAction`.|
|AddCustomModeMods|`List<CustomMod> modList`| |Register CustomMods here.|With `customMods.add`.<br/>Check activated mods with `AbstractPlayer.customMods.contains`|
|EditCards| | |Register cards here.|With `BaseMod.addCard`.|
|EditCharacters| | |Register characters here.|With `BaseMod.addCharacter`.|
|EditKeywords| | |Register keywords here.|With `BaseMod.addKeyword`.|
|EditRelics| | |Register relics here.|With `BaseMod.addRelic`.|
|EditStrings| | |Register string files here.|With `BaseMod.loadCustomStringsFile`.<br/>The json-files that centralize/localize text.|
|SetUnlocks| | |Register unlocks here.|???|
|PostInitialize| | |The game finished initializing.|Called after all Adder-hooks.|
---
### Before (Pre)
* Called before the event.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|PreStartGame| | |Before starting or continuing a game.|Before generating/loading the player.|
|OnPlayerTurnStart| | |At start of turn.|Before `GameActionManager.turn` increment.|
|OnPlayerTurnStartPostDraw| | |At start of turn, after draw cards action is queued.| |

#### Pre
* Before the event.
* Event parameter and objects can be modified.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|PostCampfire| |`boolean`|When using a campfire action.<br/>Returning false allows another campfire action.|Occurs before the campfire action effect takes place.<br/>-Don't be confused by the `Post`Campfire.<br/>Cancelling an action calls the hook again.|
|MaxHpChange|`int amount`|`int`|The max hp of the player changes.<br/>The return value overrides the amount.||
|OnCardUse|`AbstractCard card`| |A card is used.| |
|OnCreateDescription|`String rawDescription`<br/>`AbstractCard card`|`String`|The description for a card is created.<br/>The return value overrides the description for this card.| |
|OnPlayerDamaged|`int amount`<br/>`DamageInfo info`|`int`|The player takes damage.<br/>The return value overrides the amount.|Called before block is considered.|
|OnPlayerLoseBlock|`int amount`|`int`|The player loses block at the end of their turn. (For Barricade/Caliper like effects)<br/>The return value overrides the amount.|
|PreMonsterTurn|`AbstractMonster monster`|`boolean`|Before the monster starts its turn.<br/>Returning false skips the monster turn.|Triggered for each monster individually.<br/>Monsters keep the action when skipped.|
|PrePotionUse|`AbstractPotion potion`| |A potion is used.|Is triggered after the target pop-up.|
---
### After (Post)
* Called after the event.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|OnPowersModified| | |A power has been modified.|Applying, Increasing/Reducing, Removing<br/>__Don't imitate__: In the base game sometimes `onModifyPower` is called to compensate the manual applying of a power.|
|PostDeath| | |The player died.|Also triggers with `Abandon Run`.|
|PostDungeonInitialize| | |The dungeon initialized.| |
|PostEnergyRecharge| | |The energy got recharged.| |
|StartAct| | |A new act was started.|The player starts a new game or transitions between acts.|
|StartGame| | |When starting or continuing a game.|After generating/loading the player.<br/>Before dungeon generation.|

#### Post
* Called after the event.
* Event objects can still be modified.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|OnStartBattle|`AbstractRoom room`| |A battle started.||
|PostBattle|`AbstractRoom room`| |A battle ended.|Doesn't trigger when losing a battle.|
|PostCreateShopPotion|`ArrayList<StorePotion> storePotions`<br/>`ShopScreen screenInstance`| |The shop potions were created.|Called for all shop potions at once.<br/>Only called for potions inside the initialization.<br/>- E.g. `The Courier` doesn't trigger it.|
|PostCreateShopRelic|`ArrayList<StoreRelic> storeRelics`<br/>`ShopScreen screenInstance`| |The shop relics were created.|Called for all shop potions at once.<br/>Only called for relics inside the initialization.<br/>- E.g. `The Courier` doesn't trigger it.|
|PostCreateStartingDeck|`PlayerClass chosenClass`<br/>`CardGroup cardGroup`| |The starting deck was created.|CustomMods can have different impacts:<br/>1) The cardGroup is still the standard deck.<br/>2) The cardGroup is emptied.<br/>3) The cardGroup was edited.<br/><br/>CustomMods can add/remove cards later.|
|PostCreateStartingRelics|`PlayerClass chosenClass`<br/>`ArrayList<String> startingRelics`| |The starting relics were created.<br/>A list of relicIds.|CustomMods can have different impacts:<br/>1) The relic list is still the standard relic.<br/>2) The relic list is emptied.<br/>3) The relic list was edited.<br/>CustomMods can add/remove relics later.|
|PostDraw|`AbstractCard card`| |After a card was drawn.|The card and power draw effects are triggered before.<br/>Relic draw effects are triggered after.|
|PostExhaust|`AbstractCard card`| |Card was exhausted.|The card is still in hand.<br/>- Before the card exhaust effect is triggered.<br/>After relic and power exhaust effects are triggered.|
|PostPotionUse|`AbstractPotion potion`||A potion has been used.| |
|PostPowerApply|`AbstractPower power`<br/>`AbstractCreature target`<br/>`AbstractCreature source`| |A power was applied.|When `ApplyPowerAction` is used.<br/>So sometimes a power can increase its counter without triggering.<br/>- like `Deva`|
|PotionGet|`AbstractPotion potion`| |The player got a potion.| |
|RelicGet|`AbstractRelic relic`| |The player got a relic.|Isn't triggered for the starting relics.|
---
### Render
* The game renders the image.
* Called once per frame.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|ModelRender|`ModelBatch batch`<br/>`Environment env`||Before the image is rendered.|Used in conjunction with receiveCameraRender|
|PostRender|`SpriteBatch sb`| |After everything rendered.|Above everything.|
|PreRender<br/>receiveCameraRender|`OrthographicCamera camera`| |Before the image is rendered.| |
|PreRoomRender|`SpriteBatch sb`| |Before the room is rendered.|Under the player and monsters, over the background.|
|Render|`SpriteBatch sb`| |Right before everything is rendered.|Under tips and the cursor, above everything else. Make sure to call `spritebatch.setColor(Color.WHITE);` before drawing anything, or else it won't appear.|

### Update
* The game updates something.
* Called once per frame.

|Name|Parameter|Return|Description|Notes|
|---|---|---|---|---|
|PostDungeonUpdate| | |The dungeon was updated.| |
|PostPlayerUpdate| | |The player was updated.| |
|PostUpdate| | |Everything was updated.|Immediately before the input is disposed.|
|PreDungeonUpdate| | |Before the dungeon is updated.| |
|PrePlayerUpdate| | |Before the player is updated.| |
|PreUpdate| | |Before anything updates.|Immediately after the input is read.|

---

## Order

### Update & Render
1) PreRenderSubscriber
2) ModelRenderSubscriber
3) __Input read__
4) PreUpdateSubscriber
5) PreDungeonUpdateSubscriber
6) PrePlayerUpdateSubscriber
7) PostPlayerUpdateSubscriber
8) PostDungeonUpdateSubscriber
9) PostUpdateSubscriber
10) __Input disposed__
11) PreRoomRenderSubscriber
12) RenderSubscriber
13) PostRenderSubscriber

### Adder (Initialization)
1) EditStringsSubscriber
2) AddAudioSubscriber
3) EditKeywordsSubscriber
4) SetUnlocksSubscriber
5) EditCardsSubscriber
6) EditRelicsSubscriber
7) PostCreateStartingRelicsSubscriber
8) EditCharactersSubscriber
9) PostInitializeSubscriber