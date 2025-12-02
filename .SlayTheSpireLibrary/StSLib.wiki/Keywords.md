Keyword | Description | Field
--- | --- | ---
Autoplay | This card automatically plays itself when drawn. |  `AutoplayField.autoplay`
Exhaustive | Exhausts after a certain number of uses. | `ExhaustiveVariable.setBaseValue`
Fleeting | This card Purges and is removed from your deck on use. | `FleetingField.fleeting`
Grave | Start each combat with this card in your discard pile. | `GraveField.grave`
Persist | Only discards after a certain number of uses each turn. | `PersistFields.setBaseValue`
Purge | Removed until end of combat. Does NOT go to your exhaust pile. | `PurgeField.purge`
Refund | Returns energy spent on playing the card, up to the Refund value. | `RefundVariable.setBaseValue`
Retain | Not discarded at the end of your turn. | `AlwaysRetainField.alwaysRetain`
Snecko | When drawn, this card randomizes its cost. | `SneckoField.snecko`
Soulbound | Cannot be removed from your deck. | `SoulboundField.soulbound`
Startup | Triggers at the start of each combat. | `StartupCard` interface

### Usage

For interfaces, implement them in your card class. For fields, set their values in the constructor of the card. The specifics may vary slightly.

For example, to give a card Exhaustive 2 you would do `ExhaustiveVariable.setBaseValue(this, 2);`. For those that are true or false, it will be more along the lines of `PurgeField.purge.set(this, true);`

All keywords should use the `stslib:` prefix in card text. Effects with numeric values also come with their own dynamic variables to use in card text. For Exhaustive, it would be `stslib:Exhaustive !stslib:ex!.`