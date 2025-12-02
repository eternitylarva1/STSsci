# How do I set up my mod to use BaseMod?

For this I'm going to assume you're using Eclipse but the ideas apply to any other IDE. After you have followed the install instructions to get **BaseMod** setup with **SlayTheSpire** to get your workspace set up to use **BaseMod** you need to add both `BaseMod.jar` and `ModTheSpire.jar` to your Java Build Path for your project. This will allow the code contained in them to be discovered and usable by your project. Also you need to add the **SlayTheSpire** game jar to your Java Build Path. The name of the jar is `desktop-1.0.jar` (currently) and it is found in your **SlayTheSpire** folder.

# What are the differences between BaseMod and ModTheSpire? What do I use?

You should use both actually! **BaseMod** is built off of the functionality provided by **ModTheSpire**. **ModTheSpire** is what allows us to modify the base game to inject patches. **BaseMod** is designed to simplify the modding process by providing API hooks into the base game rather than requiring every mod to try and individually create patches. It should reduce modding overhead and allow for better code reuse.