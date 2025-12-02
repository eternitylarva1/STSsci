# Opening

Use the `` ` `` key to open the console. The console must be enabled in the in-game mod config for BaseMod. The keybind can also be changed in the config.

# Commands

## Deck Modification
* `deck add [id] {cardcount} {upgrades}` add card to deck (optional: integer # of times you want to add this card) (optional: integer # of upgrades)
* `deck remove [id]` remove card from deck
* `deck remove all` remove all cards from deck

## During Combat
* `draw [num]` draw cards
* `energy add [amount]` gain energy
* `energy inf` toggles infinite energy
* `energy remove [amount]` lose energy
* `hand add [id] {cardcount} {upgrades}` add card to hand with (optional: integer # of times to add the card) (optional: integer # of upgrades)
* `hand remove all` exhaust entire hand
* `hand remove [id]` exhaust card from hand
* `kill all` kills all enemies in the current combat
* `kill self` kills your character
* `power [id] [amount]` bring up a targetting reticle to apply amount stacks of a power to either the player or an enemy

## Outside of Combat
* `fight [name]` enter combat with the specified encounter
* `event [name]` start event with the specified name

## Anytime
* `gold add [amount]` gain gold
* `gold lose [amount]` lose gold
* `info toggle` Settings.isInfo
* `potion [pos] [id]` gain specified potion in specified slot (0, 1, or 2)
* `hp add [amount]` heal amount
* `hp lose [amount]` hp loss
* `maxhp add [amount]` gain max hp
* `maxhp remove [amount]` lose max hp
* `debug [true/false]` sets `Settings.isDebug`
## Relics
* `relic add [id]` generate relic
* `relic list` logs all relic pools
* `relic remove [id]` lose relic

## Unlocks
* `unlock always` always gain an unlock on death
* `unlock level [level]` set the gained unlock to be the specified level

## Acts
* `act boss` brings you directly to the bossroom of your current act
* `act [actname]` brings you to the start of the specified act

## Keys to Act 4
* `key add [color | all]` adds the corresponding key/s to your current run
* `key lose [color | all]` removes the corresponding key/s from your current run

## History
* `history random` gives you the relics and deck of a past successful run with your current character at random
* `history last` gives you the relics and deck of the last successful run with your current character

# Adding your own commands
Doing this requires 2 things: A class that handles your command, and an unoccupied key that triggers it in the console.

## The java class
All commands must be extensions of the Abstract class basemod.devcommands.ConsoleCommand.
The class has a few parameters that you can set in the constructor that do the handling of your command.
Note that none of these are mandatory and all have default values.
```java
public class YourCommand extends ConsoleCommand {
    public YourCommand() {
        maxExtraTokens = 2; //How many additional words can come after this one. If unspecified, maxExtraTokens = 1.
        minExtraTokens = 0; //How many additional words have to come after this one. If unspecified, minExtraTokens = 0.
        requiresPlayer = true; //if true, means the command can only be executed if during a run. If unspecified, requiresplayer = false.
        simpleCheck = true;
        /**
         * If this flag is true and you don't implement your own logic overriding the command syntax check function, it checks if what is typed in is in the options you said the command has.
         * Note that this only applies to the autocompletion feature of the console, and has no bearing on what the command does when executed!
         * If unspecified, simpleCheck = false.
        */

        
        followup.put("whateveryouwantmetobebaby", YourSecondCommand.class);
        /**
         * Doing this adds this word as a possible followup to your current command, and passes it to YourSecondCommand.
         * You may add as many of these as you like.
        */
    }
    ...
}
```

Mandatory for every command is the method `void execute(String[] tokens, int depth)`.
* tokens is the entire input in the devconsole, split by `/\s/` (whitespace).
* depth is the index of where in the tokens you currently are.
e.g. `tokens[depth]` would be the first word after the word that led into your command.

This method contains the syntaxcheck for your command, as well as what it actually does.
For transitionary commands, `execute(...)` usually just calls `errorMsg();`

You can also override the following methods:
* `ArrayList<String> extraOptions(String[] tokens, int depth)`
* `void errorMsg(String[]? tokens)`

What extraOptions returns will be displayed in the autocompletion feature the console has along with what you add to `followup` in the constructor.
```java
...
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("add");
        result.add("lose");
        
        if(tokens[depth].equals("add") || tokens[depth].equals("lose")) {
            complete = true;
            /**
             * Setting complete to true displays "Command is complete" in the autocomplete window.
             * This is not necessary if "simpleCheck = true" in the constructor and you don't have additional logic for it!
            */
        }
        
        return result;
    }
...
```
This would display `add` and `lose` autocompletion options when the console recognizes that you want to access your command.

`errorMsg()` is a standardized way for you to display error messages that happen in `execute(...)`.
An errormessage that would fit the options displayed in `extraOptions(...)` in the above example would be:
```java
    @Override
    public void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* add [amt]");
        DevConsole.log("* lose [amt]");
    }
```
Note that this method has to be called specifically in your execute.

## Registering your command
In your mod's main file, in receivePostInitialize, call the following static function:
```java
    public void receivePostInitialize() {
        ...
        ConsoleCommand.addCommand("yourphrasehere", YourCommand.class);
        ...
    }
```
The keyword you use here can only consist of letters and colons. If the keyword is already taken or invalid, basemod will not register your command and print an errormessage in BaseMod logwindow, but it will not crash the game.