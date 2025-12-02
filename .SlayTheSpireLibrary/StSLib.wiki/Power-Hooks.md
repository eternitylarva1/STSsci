Interface Name | Description
--- | ---
BeforeRenderIntentPower | Hook for the check if Intent should be rendered.
BetterOnApplyPowerPower | An improvement to the onApplyPower hook. Allows you to negate the power being applied and change the stack amount.
BetterOnExhaustPower | An improvement to the onExhaust hook. Includes what CardGroup the card comes from and works for powers on monsters.
OnLoseBlockPower | Hook for when the owner loses Block.
OnLoseTempHpPower | Hook for when the owner loses Temporary HP.
OnMyBlockBrokenPower | Hook for when the owner's block is completely broken.
OnPlayerDeathPower | Hook for when the player dies. Can stop the player from dying.
OnReceivePowerPower | Hook for when a buff/debuff is applied to the owner. Can negate the buff/debuff.
HealthBarRenderPower | Not a hook, allows powers to render on the hp bar like Poison.
InvisiblePower | Not a hook, makes a power not appear in a creature's list of powers.
NonStackablePower | Not a hook, allows the same power to non stack and choose when to stack or not stack.
TwoAmountPower | Not a hook, allows a power to have two numbers on it.
DamageModApplyingPower | Hook for when a DamageInfo is created with the owner as the source with the purpose of applying Damage Mods.
OnCreateBlockInstancePower | Hook for when the owner gains Block with the purpose of applying Block Mods.
OnDrawPileShufflePower | Hook for when the draw pile gets shuffled.
OnCreateCardInterface | Hook for when a card is created during combat.

For any of these, add them as an interface your power implements.
```Java
public class MyPower extends AbstractPower implements OnReceivePowerPower
{
    ...
    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        ...
    }
}
```