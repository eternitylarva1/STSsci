A mod may have a configuration panel associated with it. This is done by registering a `ModPanel` along with a [badge](https://github.com/daviscook477/BaseMod/wiki/Mod-Badges) image, which will be shown in the in-game Mods menu.

Register the badge by calling `BaseMod.registerModBadge` in `receivePostInitialize` after adding `PostInitializeSubscriber` to your main mod file, if it does not already have it. [You can find more details about badges here](https://github.com/daviscook477/BaseMod/wiki/Mod-Badges).


# EasyConfigPanel
If you only need a simple config menu, you can probably use `EasyConfigPanel`. If you want something more complicated, see [the section below](https://github.com/daviscook477/BaseMod/wiki/Mod-Config-and-Panel#making-your-own-config-panel).

To use `EasyConfigPanel`, first create a class extending `EasyConfigPanel`. This example will be utilizing fields and methods common to most Slay the Spire mod templates. `MyMod` would be your main mod file, where `receivePostInitialize` occurs.

```java
public class MyModConfig extends EasyConfigPanel {
    public MyModConfig() {
        super(MyMod.modID, MyMod.makeID("MyModConfig"));
    }
}
```

Next, register the config panel with `registerModBadge`, in `receivePostInitialize` in your main mod file. The first four parameters in the following example are just placeholders. If your mod already calls `registerModBadge`, you should only modify the last parameter to be `new MyModConfig()`.

```java
    @Override
    public void receivePostInitialize() {
        /*
        other code
        */
        BaseMod.registerModBadge(badge texture, mod name, mod author, mod description, new MyModConfig());
    }
```

This results in a config with no options.

`EasyConfigPanel` supports boolean, String, and numeric options. To add a config option, simply define a static field in your config class.

```java
public class MyModConfig extends EasyConfigPanel {
    public static boolean enableSomething = true;
    public static String nameSomething = "foo";
    public static int setSomething = 5;

    public MyModConfig() {
        super(MyMod.modID, MyMod.makeID("MyModConfig"));

        setNumberRange("setSomething", 1, 10);
    }
}
```

These static fields will automatically be used as configuration options. The value they're set to will be their default value. For numeric fields, you will likely want to set a range of values they can be. You can do this by calling `setNumberRange` in the constructor, using the field's name, minimum, and maximum values.

For `String`s, you can call `setupTextField` to control the size of the text field, the character limit, what characters are allowed, and a final check on if the entered value should be allowed.

With this, your mod config should be functional. However, the options in the panel won't have proper names. For this, you'll need to make a UIStrings entry.

### Config Text

If your mod does not have a UIStrings json file, you will have to add one. See [Custom Strings](https://github.com/daviscook477/BaseMod/wiki/Custom-Strings).

Create a new entry with a key matching the second parameter of the constructor. In this example, it's 

`super(MyMod.modID, > MyMod.makeID("MyModConfig") <);`

this part `MyMod.makeID("MyModConfig")`.

Your new entry should look something like this:

```json
  "${modID}:MyModConfig": {
    "TEXT_DICT": {
      "This": "Contains pairs of Strings, used to get values based on keys."
    }
  }
```

Under `TEXT_DICT`, make entries matching the names of the fields in your config class, with the second part of the pair being what will be displayed in the config panel.

```json
  "${modID}:MyModConfig": {
    "TEXT_DICT": {
      "enableSomething": "A Toggle",
      "nameSomething": "A Name:",
      "setSomething": "A Number"
    }
  }
```

### Extras

To use the config fields, simply check them wherever you need to eg. `if (MyModConfig.enableSomething)`.

You can modify the spacing between UI elements on the panel by calling `setPadding` in the constructor. The default value is `8.0f`.

You can use `addUIElement` to set the UI element for a field to something other than the default UI element for its type.

When the config fields are modified using the mod's config panel, they will be updated and saved automatically. If you modify these fields yourself (outside of the normal config panel), you will have to also save them manually. To do this, you'll need to store the instance you register in `BaseMod.registerModBadge` and call `save()` on it.

```java
        //receivePostInitialize
        configVariable = new MyModConfig();
        BaseMod.registerModBadge(badge texture, mod name, mod author, mod description, configVariable);

        //Elsewhere
        MyModConfig.enableSomething = false;
        MyMod.configVariable.save();
```

If you want to use an unsupported type with `EasyConfigPanel` instead of making your own config panel, you can use `EasyConfigPanel.registerFieldType`. You will need to provide a UI element, a new or already existing class, that can support the type. [You can reference the code to register the supported types here.](https://github.com/daviscook477/BaseMod/blob/master/mod/src/main/java/basemod/EasyConfigPanel.java#L256)

# Making Your Own Config Panel

Look at some examples.
[BaseMod example](https://github.com/daviscook477/BaseMod/wiki/Mod-Badges#example)
[Packmaster example](https://github.com/erasels/PackmasterCharacter/blob/main/src/main/java/thePackmaster/SpireAnniversary5Mod.java#L1238)