# Custom Savable
When you need to store a value on a card or relic, between saves and loads of a run, that isn't a relic's `counter` value or a card's `misc` value, you use a custom savable to do that. 

# API

`CustomSavable<Object>` - Implement this in the class you wish to save a value to with the type of object you want to save.

# Example

```java
public class MyCustomBottleRelic extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer>
{
    private AbstractCard card;
    // The field value you wish to save. 

      @Override
    public Integer onSave()
    {
        return AbstractDungeon.player.masterDeck.group.indexOf(card);
        // Return the location of the card in your deck. AbstractCard cannot be serialized so we use an Integer instead.
    }

     @Override
    public void onLoad(Integer cardIndex)
    {
    // onLoad automatically has the Integer saved in onSave upon loading into the game.

        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                MyCustomBottledField.inCustomBottle.set(card, true);
                setDescriptionAfterLoading();
            }
        }
        // Uses the card's index saved before to search for the card in the deck and put it in a custom SpireField.
    }
}
```
If the Methods aren't called, try adding this method to the relics/cards code:
```java
@Override
public Type savedType()
{
    return new TypeToken</*yourTypeHere*/>(){}.getType();
}
```
If your savable isn't a card, relic, or potion, you must register it by calling `BaseMod.addSaveField(String key, CustomSavableRaw saveField)`.