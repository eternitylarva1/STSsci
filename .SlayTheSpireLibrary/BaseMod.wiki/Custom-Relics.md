# API
To add relics you need to put your relic manipulation code inside the `receiveEditRelics` callback from `EditRelicsSubscriber`.

* `addRelic(AbstractRelic relic, RelicType type)` - this can be used to add custom relics to either the shared pool or the pool for the ironclad or the pool for the silent; this method **should not** be used to add relics specific to a *custom* character
* `addRelicToCustomPool(AbstractRelic relic, CardColor color)` - this can be used to add a relic to the relic pool for a custom character. **Do not** attempt to use this to add a relic to the shared pool or one of the vanilla character's pool.


To remove a relic from the pools, where the code needs to be depends on whether the relic is in a custom character's pool or not. Note: removed vanilla or shared relics will still appear in the compendium, but will not appear in a run.

* `removeRelic(AbstractRelic relic)` for vanilla or shared relics, in the `receivePostInitialize` callback from `PostInitializeSubscriber`
* `removeRelicFromCustomPool(AbstractRelic relic, CardColor color)` for a modded character's relics, inside the `receiveEditRelics` callback from `EditRelicsSubscriber`

# RelicStrings
You are **REQUIRED** to set up RelicStrings (see: [Custom Strings](https://github.com/daviscook477/BaseMod/wiki/Custom-Strings)) otherwise your relic(s) will crash the game during startup.

# CustomRelic Class
The `CustomRelic` class can be extended to make relics easier. It handles loading the texture for you (if you extend AbstractRelic you need to setup a workaround to load the texture). The constructor is:
* `CustomRelic(String id, Texture texture, RelicTier tier, LandingSound sfx)`

The size of the relic texture should be `128x128px` with the relic image itself only occupying the center ~48 to 64px box. The rest of the texture should be completely transparent.

A template supplied by the devs can be found [here](https://cdn.discordapp.com/attachments/484898639391621143/529958811570798592/unknown.png). Make your relic fit snugly inside the innermost square.

# Example
Here's an example of how you could add a relic that gives the player max hp when they pick it up

```Java
public class Blueberries extends CustomRelic {
	public static final String ID = "Blueberries";
	private static final int HP_PER_CARD = 1;
	
	public Blueberries() {
		super(ID, MyMod.getBlueberriesTexture(), // you could create the texture in this class if you wanted too
				RelicTier.UNCOMMON, LandingSound.MAGICAL); // this relic is uncommon and sounds magic when you click it
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + HP_PER_CARD + DESCRIPTIONS[1]; // DESCRIPTIONS pulls from your localization file
	}
	
	@Override
	public void onEquip() {
		int count = 0;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.isEthereal) { // when equipped (picked up) this relic counts how many ethereal cards are in the player's deck
				count++;
			}
		}
		AbstractDungeon.player.increaseMaxHp(count * HP_PER_CARD, true);
	}
	
	@Override
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new Blueberries();
	}
	
}
```