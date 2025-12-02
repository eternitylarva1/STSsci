## Making Block Modifiers
To make a block modifier, simply make a new class that extends AbstractBlockModifier and override the methods you wish to use. Your block mod always overrides getName and getDescription so the Block Manager can display your Block successfully. Whenever a creature gains block that has been augmented with a block modifier, it will render an extra icon next to the normal Block symbol that you can hover for the name and description of the block mod.

### Example Block Modifier
This is an example of a Block Type that only decreases by 5 at the start of your turn, but when it does decrease this way, you gain Vigor equal to the amount lost
```Java
public class SpicyBlock extends AbstractBlockModifier {

    public static final String ID = YourModID.makeID("SpicyBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public SpicyBlock() {}

    @Override
    public int amountLostAtStartOfTurn() {
        return 5;
    }

    @Override
    public void onStartOfTurnBlockLoss(int blockLost) {
        this.addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, blockLost)));
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }
}

```

## Using Custom Block Types
Just like damage mods, block mods autobind by default when applied to a card with a GainBlockAction in it's `use()` method. Manual binding can still be performed via a new GainCustomBlockAction that has been created. This action either takes a card that has block mods bound to it via BlockModifierManager, or a BlockModContainer which can be used by anything else.
```Java
    public SnowFort() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.DAMAGE_AND_BLOCK);
        baseBlock = block = BLOCK;
        BlockModifierManager.addModifier(this, new IceBlock());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //This auto binds if IceBlock has autobinding set to true, but gives normal block if autobinding is not set
        this.addToBot(new GainBlockAction(p, block));
        //This manually binds the block mods stored in the card. Using a BlockModContainer in place of `this` is also acceptable and is how to apply block mods via relics, orbs, etc.
        this.addToBot(new GainCustomBlockAction(this, p, block));
    }
```

The Block Manager will then add your block to the list of block types for the owner. If the list is not empty, Block Manager will render UI icon next to their Block amount that shows the list of active blocks and their order. The Block Manager uses a **First In -> Last Out** system. If you add customs blocks 1, 2, 3 in that order, then when taking damage, block 3 will be the *first* to be reduced.

### Using Block Modifiers on Cards
Block Modifiers used innately on cards should override `isInherent` to return true, or they will end up duplicated.