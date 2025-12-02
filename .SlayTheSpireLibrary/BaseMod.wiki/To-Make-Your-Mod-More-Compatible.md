Many people play with multiple mods installed, so it's better to make your mod compatible with others. However, since you're going to test with only your mod enabled most of the time, it's hard to consider these things.

This guide is aimed to write most advice about compatibility. Eventually, following this guide will be often enough, and you won't have to worry about this.


## TL;DR

If you are using [StS-Default Mod Base](https://github.com/Gremious/StS-DefaultModBase) as your coding base, you'll be fine if you change every instance of `theDefault` to the name of your mod. If you are not using it, look at [Mystic](https://github.com/JohnnyDevo/The-Mystic-Project)'s code structure and follow it.


## Prevent Duplicate ID or Path

### Prefix your ID

Every card, power or relic has a unique ID. It's defined in your code like this:
`public static final String ID = "Strike_Default"`
Slay the Spire uses this ID to distinguish from other cards, powers or relics, which means there will be a problem if any two IDs collide. Of course, you are not going to make two cards with the same ID, but what if something collides from other mods?

The solution is simple. Prefix your ID with `yourmodname:`, so it looks like this: `yourmodname:strike_default`
This way, it becomes highly unlikely for your IDs to collide with other mods unless the other mod itself has the same name as yours.

### Put your resource files in a subdirectory named after your mod.

Resource folder of all mods is copied to a same directory, which makes a file overriding others if they have the exact same path. 

To prevent this,  move all your resource files into a unique subdirectory so the paths will not be the same. So instead of `resources/localization/relics.json`, it becomes `resources/yourmodname/localization/relics.json`.

This issue is pretty common if you look at older mods and follow their code structure([Example 1](https://github.com/daviscook477/BaseMod/issues/151), [Example 2](https://github.com/kiooeht/ModTheSpire/issues/121)). If you need good examples, look at [StS-Default Mod Base](https://github.com/Gremious/StS-DefaultModBase), [Hubris](https://github.com/kiooeht/Hubris) or [Mystic](https://github.com/JohnnyDevo/The-Mystic-Project).


## SpirePatch

### You SHOULD NOT use Replace patch unless absolutely necessary

A Replace patch will completely replace the target method with something else. If you use it, any other mods trying to patch the same method will not work.


## Compatibility with other popular mods

### Use `BaseMod.MAX_HAND_SIZE` when you need max hand size

In the base game of Slay the Spire, your hand is always maxed at 10 cards. However, BaseMod made it possible to change this value, and many content mods have relics which change it. In order to make those relics to work properly, you need to check against BaseMod.MAX_HAND_SIZE instead of hardcoding 10 for max hand size.

### Make sure your character's starting relic does not crash on other characters

Base game does not allow you to get starting relic from other characters, so you might not care about how they will work on other characters. However, Prism of Light event from [InfiniteSpire](https://github.com/GraysonnG/InfiniteSpire) as well as Backtick relic from [Hubris](https://github.com/kiooeht/Hubris) allows it. It'll be bad if a character's starting relic crashes or soft locks on other characters.

Note: This does NOT mean you should make a starting relic with meaningful effects on other characters. It's up to you for the decision, but starting relic swapping is rare, meaning whatever iteration you make does not worth the time and extra complexity.

