## Making Damage Modifiers
Damage modifiers allow you to control how damage from cards, monsters, relics, etc. works during all steps of damage dealing. In simple cases, it allows easy access to piercing damage that bypasses block while still triggering powers like Thorns. In more advanced cases it can trigger certain powers and even use Wallop like followup effects to gain Block or apply debuffs equal to damage dealt.

To make a damage modifier, simply create a new class that extends AbstractDamageModifier and override the functions you wish to use. By default, damage mods will **automatically bind** when applied to a card, requiring no special functions to work in the cards `use()` method. This functionality can be overridden if you wish to manually control when damage mods activate (an example of this is shown further below). 

Damage mods are not limited to cards, as they can also be used by DamageModContainers, an object that holds any number of damage modifiers and a reference to the object using the damage modifiers. DamgageModContainers allow things like relics, monsters, orbs, and powers to apply damage mods to the damage they deal. When using DamageModContainers, manual damage binding must be performed (an example of this is also shown further below).


### Example Modifier
This modifier heals the damage dealer equal to unblocked damage dealt. If it is applied to a card it will also add a tooltip and a descriptor to the card.
```Java
public class LeechDamage extends AbstractDamageModifier {
    public static final String ID = YourModID.makeID("LeechDamage");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    TooltipInfo leechTooltip = null;

    public LeechDamage() {}

    //This hook grabs the lastDamageTaken once it is updated upon attacking the monster.
    //This lets us heal the attacker equal to the damage that was actually dealt to the target
    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (lastDamageTaken > 0) {
            this.addToBot(new HealAction(info.owner, info.owner, lastDamageTaken));
        }
    }

    //This hook allows up to add a custom tooltip to any cards it is added to.
    //In this case, we are using cardstrings to get the localized data
    @Override
    public TooltipInfo getCustomTooltip() {
        if (leechTooltip == null) {
            leechTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return leechTooltip;
    }

    //This allows us to add an stslib descriptor to the card
    // If it was originally an Attack, it will now read "Attack | Leech"
    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent. 
    public boolean isInherent() {
        return true;
    }

}
```
**Accompanying Json** - Using an icon is optional
```Json
  "YourModID:LeechDamage": {
    "NAME": "Leech",
    "DESCRIPTION": "Leech Damage [YourModID:DrainIcon]",
    "EXTENDED_DESCRIPTION": ["Heals the attacker equal to unblocked damage dealt."]
  }
```

## Using Damage Modifiers on Cards

To use a damage modifier that is always active on a card, the syntax is very similar to adding a card modifier: we use DamageModifierManager and add whatever modifier we want to `this`, being the card we are initializing. For these examples, we will assume that the damage mods have already been created. Modifiers used innately on cards should override `isInherent` to return true, or they will end up duplicated.
```Java
    public ShadeFist() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        DamageModifierManager.addModifier(this, new FireDamage());
    }
```
As cards have automatic binding by default, dealing damage in the cards `use()` method will now grab all damage modifiers that have been applied to the card and use them if this functionality has not been overwritten.
```Java
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //This hit will automatically bind as it is in the cards use() method
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
```
Should you disable automatic binding for full control over when damage mods are applied, manual binding is required. The following example showcases how to manually bind damage mods if the target has Vulnerable, but not bind any mods if the target does not have Vulnerable.
```Java
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            //Bind the damage mods if the target has Vulnerable
            this.addToBot(new DamageAction(m, BindingHelper.makeInfo(this, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        } else {
            //Deal normal damage if they don't
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }
```

If you want to manually bind damage mods but your card does not create a damage info to bind, you can instead bind the action it creates. This action will then pass any damage mods on for you.
```Java
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //You can define the action, bind it, and add it
        MyComplicatedAction action = new MyComplicatedAction(p, p.hand.size(), getAverageMonsterHP(), p.currentHealth >= 42);
        BindingHelper.bindAction(this, action)
        this.addToBot(action);
        //You can also directly bind the action while adding it, if you prefer
        this.addToBot(BindingHelper.makeAction(this, new MyComplicatedAction(p, p.hand.size(), getAverageMonsterHP(), p.currentHealth >= 42)));
    }
```

### Example Card
Here an an example card using the previous example modifier. It deals 8 leech damage.
```Java
public class LifeTap extends AbstractDynamicCard {

    public static final String ID = YourModID.makeID(LifeTap.class.getSimpleName());
    public static final String IMG = makeCardPath("LifeTap.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = YourColorHere;

    private static final int COST = 2;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 2;

    public LifeTap() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
        DamageModifierManager.addModifier(this, new LeechDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //This hit will automatically bind as it is in the cards use() method
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
```
**Accompanying Json** - Using an icon is optional
```Json
  "YourModID:LifeTap": {
    "NAME": "Life Tap",
    "DESCRIPTION": "Deal !D! [YourModID:DrainIcon] damage."
  },
```

## Adding Damage Modifiers via Powers / Relics

There may be times where you do not want to bake a damage mod directly into a card, but would rather allow a power/relic to apply mods for you. For this, we have 2 hooks to use, DamageModApplyingPower and DamageModApplyingRelic. The power hooks will trigger for any DamageInfo created where the owner of the power is the damage source, whereas the Relic hooks are instead exclusive to the player. The hooks provided by DamageModApplying interfaces are as follows:

```Java
boolean shouldPushMods(DamageInfo infoMayBeNull, Object instigator, List<AbstractDamageModifier> activeDamageModifiers)

List<AbstractDamageModifier> modsToPush(DamageInfo infoMayBeNull, Object instigator, List<AbstractDamageModifier> activeDamageModifiers)

void onAddedDamageModsToDamageInfo(DamageInfo info, Object instigator)
```
`shouldPushMods` takes a DamageInfo (which may be null), an Object that instigated the creation of the damage info (if an instigator was bound, otherwise it may be null), and a list of damagemods that have already been bound to the DamageInfo. If it returns true, the list of mods in modsToPush will be added.

`modsToPush` takes the same params as the previous, and returns a list of damage mods you want to add. Be sure to return a new list, and don't modify the one passed as a parameter. Of note, this will also emulate adding mods for the purpose of damage calculation within a cards applyPowers and calculateCardDamage hooks. For instance, pushing a mod that deals double damage to enemies with full hp will render the increased damage on the card even though no DamageInfo is created yet.

`onAddedDamageModsToDamageInfo` is an optional hook that triggers upon this _actually_ applying damage mods to a DamageInfo. It is not called when pushing mods for the purpose of card damage calculation. This acts as an easy way to know the power actually triggered in a tangible way. An example use case would be if a power only applied a damage mod to the next 3 cards you play, this hook can be used to reduce the power. If you do this route, do be mindful that if a card creates 2 damageinfos, this hook will get called twice for that card.

### Example Power
This power applies FireDamage to Attack cards if they dont already have FireDamage, and will not trigger on DamageInfos created from other instigators such as Orbs
```Java
public class FireDamagePower extends AbstractPower implements DamageModApplyingPower {

    ...

    public FireDamagePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.loadRegion("firebreathing");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return o instanceof AbstractCard && ((AbstractCard) o).type == AbstractCard.CardType.ATTACK && list.stream().noneMatch(mod -> mod instanceof FireDamage);
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new FireDamage());
    }
}
```

## Using Damage Modifiers on Anything Else

To use a damage modifier on something other than a card, we no longer need DamageModiferManager, we instead use DamageModContainers and manual damage binding. In these example, we use a DamageModContainer to make a relic that deals 5 Fire damage to all enemies, and an Orb that deals Fire damage to the enemy with the most HP when evoked. We will again assume the required damage mods were already created.
```Java
public class FireFlower extends CustomRelic {

    ...

    private static final int DAMAGE = 5;
    private final DamageModContainer container;

    public FireFlower() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        container = new DamageModContainer(this, new FireDamage());
    }

    @Override
    public void atTurnStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(BindingHelper.makeAction(container, new DamageAllEnemiesAction(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)));
    }
   
    ...
}
```

```Java
public class FireOrb extends AbstractOrb {

    ...
    private final DamageModContainer container;

    public FireOrb () {
        super(...);
        ...
        container = new DamageModContainer(this, new FireDamage());
    }

    @Override
    public void onEvoke() {
        AbstractMonster t = null;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                if (t == null || mon.currentHealth > t.currentHealth) {
                    t = mon;
                }
            }
        }
        if (t != null) {
            int damage = evokeAmount;
            if (t.hasPower(LockOnPower.POWER_ID)) {
                damage *= LockOnPower.MULTIPLIER;
            }
            this.addToBot(new DamageAction(t, BindingHelper.makeInfo(container, p, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    ...

}
```

## Manual Card Binding Examples

### Example Modifier 2
This modifier ignores Block and applies Poison instead of dealing damage to the target. If the object is a card then it also adds a Poison keyword and descriptor to the card using Icons. This modifier also allows you disabling automatic binding, which we will see a use case for in the card example below.
```Java
public class PoisonDamage extends AbstractDamageModifier {
    public static final String ID = YourModID.makeID("PoisonDamage");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    TooltipInfo poisonTooltip = null;

    public PoisonDamage() {
        this(true);
    }

    public PoisonDamage(boolean autoBind) {
        this.automaticBindingForCards = autoBind;
    }

    //A simple override allows this damage to directly ignore any Block the target has
    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    //Using onAttackToChangeDamage here will grab the damage that would have been dealt and allows us to modify it, returning 0.
    //Since we have the damage amount at this point, we can also simply apply Poison to the target equal this amount.
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        this.addToTop(new ApplyPowerAction(target, info.owner, new PoisonPower(target, info.owner, damageAmount)));
        return 0;
    }

    @Override
    public TooltipInfo getCustomTooltip() {
        if (poisonTooltip == null) {
            poisonTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return poisonTooltip;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    public boolean isInherent() {
        return true;
    }
}
```
**Accompanying Json** - Using an icon is optional
```Json
  "YourModID:PoisonDamage": {
    "NAME": "Poison",
    "DESCRIPTION": "Poison Damage [YourModID:PoisonIcon]",
    "EXTENDED_DESCRIPTION": ["Ignores #yBlock and applies #yPoison instead of dealing damage."]
  }
```

### Example Card 2
Here an an example card using the above modifier. It deals 4 damage, then deals 4 poison damage.
```Java
public class PoisonShock extends AbstractDynamicCard {

    public static final String ID = YourModID.makeID(PoisonShock.class.getSimpleName());
    public static final String IMG = makeCardPath("PoisonShock.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = YourColorHere;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;

    public PoisonShock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        DamageModifierManager.addModifier(this, new PoisonDamage(false)); //We disable auto binding here so the mod doesnt apply to both hits
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //This hit is unbound so it deals normal damage
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        //This hit is bound and will do our poison damage
        this.addToBot(new DamageAction(m, BindingHelper.makeInfo(this, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
```
**Accompanying Json** - Using an icon is optional
```Json
  "YourModID:PoisonShock": {
    "NAME": "Poison Shock",
    "DESCRIPTION": "Deal !D! damage. NL Deal !D! [YourModID:PoisonIcon] damage."
  },
```