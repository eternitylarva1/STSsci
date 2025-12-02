# Branching Upgrades on Cards
An interface that has to be implemented on the card that is supposed to have a branching upgrade. This allows the card to have two distinct upgrade paths which the player can choose between when smithing.

Implementation example:
```Java
public class CoolCard extends CustomCard implements BranchingUpgradesCard
{
    ...
  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      if (isBranchUpgrade()) {
        branchUpgrade();
      } else {
        baseUpgrade();
      } 
    } 
  }

  public void baseUpgrade() {
    ...
  }
  
  public void branchUpgrade() {
    ...
  }
}
```

# Common Keyword Icons
Boolean SpireField on cards that, if set to true, will render small icons representing certain common keywords in the top-right of the card. 
If the icons are present, the tooltips for the card are automatically added. This allows the card text to not include them and saving some space.

The following keywords can be replaced by an icon:

Innate, Ethereal, Retain, Purge, and Exhaust.

Implementation example:
```Java
public class NoKeywordTextCard extends CustomCard
{
    public NoKeywordTextCard () {
        ...
        CommonKeywordIconsField.useIcons.set(this, true);
    }
}
```

# Spawn Modification Cards
This is an interface you can implement on cards that allows you to fully control how and if a card will be spawned and what happens if it does.
  
This interface has 3 methods that achieve this:  

## canSpawn  
When the card implementing this interface is rolled, this method gets called before it is added to the ArrayList containing the cards that will be in the card reward and before the code checks for duplicates.  
  
Parameters:   
**currentRewardCards** ArrayList\<AbstractCard\>   
List of the cards that were already rolled excluding the card that calls this method. This is not guaranteed to contain all cards of the reward, it may even be empty at the time.  
  
Return: boolean  
Allows you to either let the card spawn by returning **true** or rolling a new card if you return **false**. By default, this method always returns **true**.  
  
Example:  
The card has a 50% chance to actually spawn if rolled and you can only have one instance of it in your deck.
```Java
public class TestCard extends CustomCard implements SpawnModificationCard {
    public TestCard(...)
    ...

    @Override
    public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
        //50% chance to not be allowed to spawn.
        if(AbstractDungeon.cardRng.randomBoolean()) {
            return false;
        }
        
        //Player can't already have the card.
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cardID.equals(this.cardID)) {
                return false;
            }
        }
        
        //Card will spawn.
        return true;
    }
}
```

## replaceWith  
This method is called if **canSpawn** returns **true** and allows you to replace the rolled card with a new card instance that will be checked against the currentRewardCards to see if it's a duplicate or not. By default, the card doesn't get modified.  
  
Parameters:  
**currentRewardCards** ArrayList\<AbstractCard\>   
List of the cards that were already rolled excluding the card that calls this method. This is not guaranteed to contain all cards of the reward, it may even be empty at the time.  
  
Return: AbstractCard  
The instance of the card that gets added to the list. 
  
Example:
If the player has a rare card in their deck, the card reward will contain a curse instead.
```Java
public class TestCard extends CustomCard implements SpawnModificationCard {
    public TestCard(...)
    ...

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if(c.rarity == CardRarity.RARE) {
                return new Pain();
            }
        }
        return this;
    }
}
```  
  
## onRewardListCreated  
This method allows you to make final modifications to any card in the list right before the card reward is created.  
**Note**: I advise against replacing cards at this stage because **AbstractRelic:onPreviewObtainCard** and upgrade chances have already been calculated before this point.
  
Parameter:  
**rewardCards** ArrayList\<AbstractCard\>  
The list of cards that will be in the card reward.  
  
Example:  
If this card is in a card reward, all other cards will be upgraded.  
```Java
public class TestCard extends CustomCard implements SpawnModificationCard {
    public TestCard(...)
    ...

    @Override
    public void onRewardListCreated(ArrayList<AbstractCard> rewardCards) {
        for(AbstractCard c : rewardCards) {
            if (c != this && c.canUpgrade()) {
                c.upgrade();
            }
        }
    }
}
```