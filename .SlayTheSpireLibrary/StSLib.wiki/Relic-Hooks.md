Interface Name | Description
--- | ---
ClickableRelic | Gives a relic right-click to activate functionality.
OnChannelRelic | Hook for when an Orb is Channeled.
BeforeRenderIntentRelic | Hook for the check if Intent should be rendered.
BetterOnLoseHpRelic | An improvement to the onLoseHp hook. Includes the DamageInfo and allows you to change the damage amount.
BetterOnSmithRelic | An improvement to the onSmith hook. Actually triggers on the smith happening and passes the smithed card.
BetterOnUsePotionRelic | An improvement to the onUsePotion hook. Includes what potion was used.
SuperRareRelic | Think Rare isn't rare enough? Make your relic super rare!
OnReceivePowerRelic | Hook for when a buff/debuff is applied to the player. Can negate the buff/debuff.
OnApplyPowerRelic | Hook for when the player applies a buff/debuff to something. Can negate the buff/debuff.
OnAnyPowerAppliedRelic | Hook for when a buff/debuff is applied to any creature by any creature. Can negate the buff/debuff.
OnAfterUseCardRelic | Mirrors the AbstractPower hook `onAfterUseCard` to relics
OnSkipCardRelic | Hook for when a card reward is skipped.
OnRemoveCardFromMasterDeckRelic | Hook for when a card is removed from the master deck.
OnLoseTempHpRelic | Hook for when owner loses Temporary Hp.
OnLoseBlockRelic | Hook for when owner loses Block.
OnPlayerDeathRelic | Hook for when the player dies. Can stop the player from dying.
DamageModApplyingRelic | Hook for when a DamageInfo is created with the player as a source with the purpose of applying Damage Mods.
OnCreateBlockInstanceRelic | Hook for when the player gains Block with the purpose of applying Block Mods.
CardRewardSkipButtonRelic | Interface for a relic that adds a button to skip card rewards that trigger an effect, like Singing Bowl.
OnCreateCardInterface | Hook for when a card is created during combat.
 
For any of these, add them as an interface your relic implements.
```Java
public class MyRelic extends AbstractRelic implements OnChannelRelic
{
    ...
    @Override
    public void onChannel(AbstractOrb orb)
    {
        ...
    }
}
```