Cards can be tagged with arbitrary tags to keep track of flags on cards.

To add a tag to a card, use `card.tags.add(TAG)`. <br/>
You can check if a card has a tag with `card.hasTag(TAG)`.

**As of Slay the Spire version 10-04-2018, the base game includes a card tag system. If you were using the BaseMod system before this time, you will have to convert. The old BaseMod card tag system still works for backwards compatibility.**

## Default Tags
Slay the Spire comes with some tags by default:
* `AbstractCard.CardTags.BASIC_STRIKE`
  * A card tagged with this counts as a basic Strike card for events such as The Vampires and Back to Basics.
  * Your basic Strike should be tagged with both `BASIC_STRIKE` and `STRIKE` if it is to work with Perfected Strike.
* `AbstractCard.CardTags.BASIC_DEFEND`
  * A card tagged with this counts as a basic Defend card for events such as Back to Basics.
* `AbstractCard.CardTags.STRIKE`
  * A card tagged with this counts as a "Strike" for Perfected Strike.
  * Your basic Strike should be tagged with both `BASIC_STRIKE` and `STRIKE` if it is to work with Perfected Strike.
* `AbstractCard.CardTags.HEALING`
  * A card tagged with this cannot be randomly generated during combat. It's purpose is to avoid players stalling in battle to randomly generate healing or other forms of meta-scaling.

BaseMod comes with some tags by default:
* `BaseModCardTags.FORM`
  * A card tagged with this counts like Demon Form, Wraith Form, and Echo Form for the My True Form custom modifier.

```Java
public TestStrike()
{
    super(ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.RED, CardRarity.RARE, CardTarget.ENEMY);

    tags.add(CardTags.STRIKE);
    tags.add(CardTags.BASIC_STRIKE);
    ...
}
```

## Custom Tags
You can define your own tags like so:
```Java
public class CustomTags
{
	@SpireEnum public static AbstractCard.CardTags MY_TAG;
	@SpireEnum public static AbstractCard.CardTags MY_OTHER_TAG;
	@SpireEnum public static AbstractCard.CardTags LOOK_AT_ME;
}

	//In a card:
	tags.add(CustomTags.MY_TAG);
```