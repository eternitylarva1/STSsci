# Rewards
In the base game there are a few types of rewards you can receive they are, "Card", "Gold/Stolen Gold", "Relic", "Potion", "Emerald/Sapphire Key." Custom rewards aims to allow you to give the player something other than the listed rewards. The example will be a reward that lets you give the player hp.

# API

## Register a CustomReward
`BaseMod.registerCustomReward(RewardType type, LoadCustomReward onLoad, SaveCustomReward onSave)` - call this in `receivePostInitialize` to make sure that your reward type is loaded properly.

* `type` - This should be an enum of type `RewardType`, this should be unique to your reward and using a base game type may result in unintended side effects.
* `onLoad` - A lambda that returns a newly constructed instance of your `CustomReward`
* `onSave` - A lambda that returns a newly constructed instance of a RewardSave that saves the data for your reward;

# Example
To create this reward we will need to first create a class to patch the enum for `RewardItem.RewardType` to include our new `RewardType`.
```Java
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class HpRewardTypePatch {
    @SpireEnum
    public static RewardItem.RewardType MYMOD_HPREWARD;
}
```

Then after we have created our new `RewardType` we will create our actual reward class that extends `CustomReward`.
```Java
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HpReward extends CustomReward {
    private static final Texture ICON = new Texture(Gdx.files.internal("[pathtotexturefile]"));
    
    public int amount;

    public HpReward(int amount) {
        super(ICON, "Heal " + amount + " hp.", HpRewardTypePatch.MYMOD_HPREWARD);
        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.heal(this.amount);
        return true;
    }
}
```

All there is left to do is to register this new reward with `BaseMod.registerCustomReward` in your initializer class.
```Java
...
    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(
            HpRewardTypePatch.MYMOD_HPREWARD, 
            (rewardSave) -> { // this handles what to do when this quest type is loaded.
                return new HpReward(rewardSave.amount);
            }, 
            (customReward) -> { // this handles what to do when this quest type is saved.
                return new RewardSave(customReward.type.toString(), null, ((HpReward)customReward).amount, 0);
            });
    }
...
```