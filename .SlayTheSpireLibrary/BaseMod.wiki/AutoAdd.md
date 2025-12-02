AutoAdd allows mods to simplify registering all their cards/relic/etc with BaseMod, instead of manually adding each card like this:
```java
@Override
public void receiveEditCards()
{
    // No more of this
    BaseMod.addCard(new MyCard1());
    BaseMod.addCard(new MyCard2());
    BaseMod.addCard(new MyCard3());
    // etc.
}
```

# API
### Configuration
* `AutoAdd(String modID)`
  * `modID` : The ID of the mod you want to search for classes.
* `overrideClassPool(ClassPool pool)`
  * `pool` : The javassist ClassPool AutoAdd should use to find classes.
  * Defaults to `Loader.getClassPool()`
  * ***Note:** Normally the only time you should have to alter this is if you want to use AutoAdd inside a [Raw patch](https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch#raw).*
* `setDefaultSeen(boolean seen)`
  * `seen` : Whether or not cards should default to seen.
  * By default, cards are NOT seen.
* `packageFilter(String packageName)`
  * `packageName` : The name of a package to search (i.e. `"mymod.cards"`), any classes outside this package will be ignored.
* `packageFilter(Class<?> packageClass)`
  * `packageClass` : The class type of a class in a package you want to search (i.e. `MyAbstractCard.class`), any classes outside the package this class is in will be ignored.
* `filter(ClassFilter filter)`
  * `filter` : Custom filter for which classes to include/exclude during the search.

### Usage
* `<T> Collection<CtClass> findClasses(Class<T> type)`
  * Finds all non-abstract, non-interface classes that inherit from the class given by `type`.
  * This *includes* any classes that have the `@AutoAdd.Ignore` annotation.
* `<T> void any(Class<T> type, BiConsumer<Info, T> add)`
  * Find classes as per `findClasses`, gets info about their annotations (see below), then calls the given function/lambda `add` with that info and a new instance of the class.
  * This *excludes* any classes that have the `@AutoAdd.Ignore` annotation (see below).
  * A class MUST have a constructor that takes no arguments (or be ignored), otherwise `any` will crash.
* `void cards()`
  * Finds all cards and registers them with BaseMod (`BaseMod.addCard`), possibly marking them seen.

## Annotations
The following annotations can be added to individual classes to alter how AutoAdd interacts with them:
* `@AutoAdd.Ignore`
  * A class annotated with this will be completely ignored by AutoAdd.
* `@AutoAdd.Seen`
* `@AutoAdd.NotSeen`
  * Seen and NotSeen will override the default seen setting of AutoAdd, marking a card as seen or not seen by default.

# Examples
## Basic Auto-Adding Cards
```java
@Override
public void receiveEditCards()
{
    // This finds and adds all cards in the same package (or sub-package) as MyAbstractCard
    // along with marking all added cards as seen
    new AutoAdd(MyModID)
        .packageFilter(MyAbstractCard.class)
        .setDefaultSeen(true)
        .cards();
}
```

## Auto-Adding Character Specific Relics for a Modded Character
```java
@Override
public void receiveEditRelics()
{
    // This finds and adds all relics inheriting from CustomRelic that are in the same package
    // as MyRelic, keeping all as unseen except those annotated with @AutoAdd.Seen
    new AutoAdd(MyModID)
        .packageFilter(MyRelic.class)
        .any(CustomRelic.class, (info, relic) -> {
            BaseMod.addRelicToCustomPool(relic, MY_CHARACTER_COLOR);
            if (info.seen) {
                UnlockTracker.markRelicAsSeen(relic.relicId);
            }
        });
}
```