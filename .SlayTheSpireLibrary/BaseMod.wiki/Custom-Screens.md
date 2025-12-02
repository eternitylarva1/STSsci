# API

## Basemod.
* `addCustomScreen(CustomScreen screen)`
  * Register a new screen. Call this in `receivePostInitialize`.
* `getCustomScreen(AbstractDungeon.CurrentScreen screen)`
  * If you ever want to get the instance of your screen.

* `openCustomScreen(AbstractDungeon.CurrentScreen screen, Object... args)`
  * When you want to open your screen. The arguments to pass will attempt to be passed to an `open` method of your screen's class.

## CustomScreen
* `curScreen()`
  * Return the CurrentScreen enum value for your screen.
  * You need to create a new CurrentScreen enum value using [SpireEnum](https://github.com/kiooeht/ModTheSpire/wiki/SpireEnum).
  * This is the same value you need to use in `getCustomScreen` and `openCustomScreen` to get/open your screen.
* `open(...)`
  * Called when your screen is opened. You an define it with any parameters you want but they must match when you call `openCustomScreen`
  * You may also override `open(Object... args)` to have full control over how the arguments get passed.
* `reopen()`
  * Called when your screen is reopened, i.e. if your screen is open, then the settings screen is opened, then settings is closed, your screen is then "reopened".
* `close()`
  * Called when `AbstractDungeon.closeCurrentScreen` is called while your screen is open.
* `update()`
  * Called every frame your screen is open for updating.
* `render(SpriteBatch sb)`
  * Called every frame your screen is open for rendering.
* `allowOpenDeck()`
  * Return whether or not to allow the master deck screen to be opened while your screen is open. Defaults to false.
* `allowOpenMap()`
  * Return whether or not to allow the map screen to be opened while your screen is open. Defaults to false.
* `openingSettings()`
  * Called when the settings screen is opened while your screen is open.
* `openingDeck()`
  * Called when the master deck screen is opened while your screen is open. Requires you to return true from `allowOpenDeck()` first.
* `openingMap()`
  * Called when the map screen is opened while your screen is open. Requires you to return true from `allowOpenMap()` first.

# Example

```java
public class MyScreen extends CustomScreen
{
	public static class Enum
	{
		@SpireEnum
		public static AbstractDungeon.CurrentScreen MY_SCREEN;
	}
	
	@Override
	public AbstractDungeon.CurrentScreen curScreen()
	{
		return Enum.MY_SCREEN;
	}
	
	// Note that this can be private and take any parameters you want.
	// When you call openCustomScreen it finds the first method named "open"
	// and calls it with whatever arguments were passed to it.
	private void open(String foo, AbstractCard bar)
	{
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE)
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
		// Call reopen in this example because the basics of
		// setting the current screen are the same across both
		reopen();
	}
	
	@Override
	public void reopen()
	{
		AbstractDungeon.screen = curScreen();
		AbstractDungeon.isScreenUp = true;
	}
	
	@Override
	public void openingSettings()
	{
		// Required if you want to reopen your screen when the settings screen closes
		AbstractDungeon.previousScreen = curScreen();
	}
	
	// ...
}
```
```java
// Register
BaseMod.addCustomScreen(new MyScreen());

// Open
BaseMod.openCustomScreen(MyScreen.Enum.MY_SCREEN, "foobar", new Shiv());
```