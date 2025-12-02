# Full Example of how to modify the starting relics for the Ironclad such that he starts with `Black Blood` instead of `Burning Blood`

```java
import java.util.ArrayList;

import basemod.ModLabel;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Ironclad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.PostCreateStartingRelicsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer // this annotation tells ModTheSpire to look at this class to initialize our mod
public class SampleMod implements PostCreateStartingRelicsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(SampleMod.class.getName()); // lets us log output

    public static final String MODNAME = "Sample Mod"; // mod name
    public static final String AUTHOR = "You"; // your name
    public static final String DESCRIPTION = "v1.0.0 - makes the Ironclad OP"; // description (w/ version # if you want)

    public SampleMod() {
        logger.info("subscribing to PostCreateStartingRelics and postInitialize events");
        // when our mod is loaded we tell BaseMod that we want to do something when the starting relics are created for the character when they start a run
        // as well as when the game as finished initializing itself. these are defined earlier (in implements)
        BaseMod.subscribe(this);
    }

    public static void initialize() { // ModTheSpire will call this method to initialize because of the annotation we put at the top of the class definition
        @SuppressWarnings("unused")
        SampleMod mod = new SampleMod();
    }

    @Override
    public void receivePostInitialize() {
        // Mod badge
        Texture badgeTexture = new Texture("badge_img.png");
        ModPanel settingsPanel = new ModPanel();
        ModLabel label = new ModLabel("My mod does not have any settings (yet)!", 400.0f, 700.0f, settingsPanel, (me) -> {});
        settingsPanel.addUIElement(label);
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel); // once the game has initialized we want to set up a **mod badge** which is a little icon on the main menu screen that tells users that our mod has been loaded
    }

    @Override
    public void receivePostCreateStartingRelics(PlayerClass playerClass, ArrayList<String> relicsToAdd) {
        if (playerClass == PlayerClass.IRONCLAD) { // only give the relic if the class is ironclad
            relicsToAdd.add("Black Blood"); // here we simply give the player black blood in addition to their other starting relics
            UnlockTracker.markRelicAsSeen("Black Blood");
        }
    }

}
```

# Some mods using BaseMod
* (https://github.com/Gremious/StS-DefaultModBase) - This Mod is highly recommended. Please use this as an excellent jumping off point with examples and good practices! Some of the others below are outdated.
* (https://github.com/gskleres/FruityMod-StS)
* (https://github.com/daviscook477/CustomClimb)
* (https://github.com/ShikiSeiren/NecroMod)
* (https://github.com/t-larson/ColoredMap)
* (https://github.com/twanvl/sts-hybrid-character)
* (https://github.com/Modernkennnern/STS_AlwaysWhale)