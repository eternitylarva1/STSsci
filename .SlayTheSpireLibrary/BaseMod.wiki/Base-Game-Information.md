# What is this page?

This page is intended to be the place where we can describe how systems in the base game work so people new to modding can get better acquainted with the game's code base. This page is also a WIP so please feel free to add any new knowledge to it.

# Cards and Actions

As you well know the gameplay centers around cards and what those cards do when they're played. The game internally represents all cards as Java classes that inherit from `AbstractCard`. Each card specifies how it works by providing it's properties in a call to `super()` which invokes the constructor for `AbstractCard`. Cards also specify their details by overriding the generic methods provided/defined in `AbstractCard`.

The main method that cards override is the `use()` method which is where cards actually have all the logic of what they do. This is the method that is **actually called** when the player uses a card. What you'll notice about cards is that the cards themselves don't actually do much of the heavy lifting when it comes to modifying game state. This is where actions come in. All actions inherent from `AbstractGameAction` which defines the basics of what actions do. Game actions are the code that actually modifies the game state with things like damaging enemies, drawing cards, etc... The game provides many default actions in the `actions.common` package for things like damage, card draw, healing, etc... The way the game builds cards and more complicated actions is by combining actions together. For example a card that draws 2 cards and gives you one energy could use two actions, one for each of those things.

# Action Queue

The way the game actually executes actions is through an action queue, specifically `AbstractDungeon.actionManager`. The `actionManager` is a `public static` object that allows you to queue up actions to be executed. Any time you want something to happen in game as the result of a card being played or as some hook being triggered, you should either add something to the front of the `actionManager`'s queue or to the end of it. To add to the front of the queue use `addToTop()`, and to add to the end of the queue use `addToBottom()`.