## Making custom icons
There may be times where you may want to add an icon to a keyword tooltip for flavor, or even replace a keyword on a card with an icon entirely, such as using an icon to denote different damage types. For this, we have support for adding custom icons like using the small energy orb [E].

To make an icon, first make an icon class by extending AbstractCustomIcon. Your icon requires an ID (which should be prefixed with your mod ID just like making cards and powers), as well as a Texture or TextureAtlas so it knows what to render. By default, icons will render the same size as the small energy orb, though this can be overridden.

The following is an example Icon using the string "Electric" with makeID. The code for using this this Icon will be `[ShadowSiren:ElectricIcon]`, as the `[` and `Icon]` parts are automatically added.
```Java
public class ElectricIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Electric");
    private static ElectricIcon singleton;

    public ElectricIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Electric.png"));
    }

    public static ElectricIcon get()
    {
        if (singleton == null) {
            singleton = new ElectricIcon();
        }
        return singleton;
    }
}
```
This Icon has been created, but we still need to register it to be able to use it. Icons should be registered _before_ any cards that use them, similar to dynamic variables. Making a `get()` method is not mandatory as we could use `new ElectricIcon()` in this example, though it is preferable.
```Java
    @Override
    public void receiveEditCards() {
        //Add icons
        CustomIconHelper.addCustomIcon(ElectricIcon.get());
        ...

        // Add the Dynamic variables
        ...
        
        // Add the cards
        ...
    }
```

## Using Icons in Descriptions
Icons can be added to the descriptions of cards, as well as in tooltips such as power tips, keyword tips, and relic descriptions (basically anywhere you would reasonably use [E]). To use an Icon in your text, simply add the icon code to the text string.

**Cards** - Icons will work in the same was as [E] in the descriptions.
```Json
  "ShadowSiren:ElectroStrike": {
    "NAME": "Electro Strike",
    "DESCRIPTION": "Deal !D! [ShadowSiren:ElectricIcon] damage."
  }
```
**Keywords** - Using PROPER_NAME allows you to use a keyword in a card and have the keyword tooltip use an icon. Icons also work in the descriptions.
```Json
  {
    "PROPER_NAME": "Keyword [YourModID:GearIcon]",
    "NAMES": [
      "keyword"
    ],
    "DESCRIPTION": "[YourModID:HeartIcon] Icons work here too."
  },
  {
    "PROPER_NAME": "Multiword Keyword [YourModID:IncreaseIcon]",
    "NAMES": [
      "Multiword Keyword",
      "multiword_keyword"
    ],
    "DESCRIPTION": "Icons work at the end of a sentence with a space before the period [YourModID:RunningIcon] ."
  },
```
**Powers** - Icons will work in the descriptions for powers. You can also put an icon in the power name and it will appear before the image for the power. It will look silly in most cases however.
```Json
  "YourModID:FancyPower": {
    "NAME": "Power Name [YourModID:BadIdeaIcon]",
    "DESCRIPTIONS": ["Icons [YourModID:BetterIdeaIcon] also work in here."]
  },
```