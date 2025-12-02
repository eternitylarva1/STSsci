# Decompiling
Decompiling the game will allow you to (essentially) look at the game's source code. This isn't necessary for modding if you only use BaseMod's subscription system. However, it is a very useful tool for

* Doing any modding beyond what BaseMod currently provides
* Understanding how to achieve basic card effects
* Seeing examples for things you want to do like making an Action

There are multiple ways to look at decompiled source code. The three most popular ways are:
* Inbuilt decompiler from intellij (Fernflower) by tapping shift four times in a row and ctrl-clicking on a method or class. Has issues with for-each loops and displays them as iterator loops which confuses beginners
* JD-GUI a powerful compiler with the best search feature but issues with switch-cases
* luyten the most accurate decompiler however it has the weakest search feature and no nested display of class structure so generally less recommended than the others

For this tutorial we'll cover JD-GUI since that one tend to be the most accurate and effective for most use-cases.

# How to install and use JD-GUI
Follow these steps to decompile the game:

1. Download the [JD-GUI Java Decompiler](http://jd.benow.ca/). Click the download tab and then grab the `.jar` version which will look something like `jd-gui-1.4.0.jar`
2. Launch JD-GUI with `jd-gui.exe` and then when it prompts you to find a jar to decompile go ahead and navigate to your **Slay The Spire** folder (for Steam users - check `Program Files/Steam/steamapps/common`) and select `desktop-1.0.jar`. If it does not automatically prompt you go to *File* -> *Open File...* ![Open File Image](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/decompile_open.png)
3. This will open a view in which you can look at the decompiled source code.

# Tips
## Where to look for code
Now that you have full access to the decompiled source code you'll likely be a bit overwhelemed. There's a lot of packages in front of you, however the only package you really have to look at is `com.megacrit.cardcrawl`. 
![JDGUI StS code package](https://user-images.githubusercontent.com/20698806/221273477-c87c1bf7-c3da-4813-91b0-18e5eaa54bb8.png)  
Within this package you find pretty much everything you'll need to to make a mod. Of special interest to somebody that is making a character would be the actions, powers and cards packages. These contain the code for pretty much every card effect you see in the base game.  
  
## How to effectively navigate
Clicking through the packages might be pretty helpful when you're first getting started to get a good feel for where you can find what. However, if you already have a good idea what you're look for, e.g. wanting to see how `Dual Wield` copies a card in hand, then there's an easier way to go about it.  
  
At the top of your window you'll see File/Edit/Navigation/etc menus, below that you'll see five icons. The first icon is there to open a jar to decompile it. The second and third icon will be the important ones here.  
The second icon, the folder with the two colored circles, allows you to search for a class that starts with your input. For example, typing in `Strike` will display the following:  
![JDGUI Type Search Window](https://user-images.githubusercontent.com/20698806/221275139-b39c5b6d-cd4b-427e-92ce-79c1e96b878f.png)  
You can also add in Wildcards as described above the input box. So if you know the class has `Action` towards the end of its name, you may want to type in `*Action`. This type of search is **not** case-sensitive.  
  
The third icon, a flashlight, is a very powerful search tool with many options to limit what is matched and what isn't.  
![JDGUI Search](https://user-images.githubusercontent.com/20698806/221275574-b1b500b9-ba4a-4aa3-8e6d-6bc9c11e2032.png)  
Some example use-cases where this feature can help you:  
* Searching for where a method/hook on a relic is called in the code.
* Finding cards that use a specific action.
* Finding classes that override a specific method, and a lot more.  
  
You'll want to enter the **the full** method-, field-, or class-name to find it. Furthermore, you'll need to also check at least one of the boxes in the `Search For` group. The most important ones here are:  
* Field: for variables
* Method: for methods
* Type: for classes
* String Constant: mostly for final static strings which the compiler replaces the variables with (e.g. if you have `public static final ID = "Chemical X";` defined on a relic and then access ChemicalX.ID somewhere, the decompiler will show "Chemical X" instead.  
  
After that, you need to check at least one of the boxes in your `Limit To` group.  
Declarations are fields, methods, etc that are created or overriden. Often times they'll have a qualifier and a type like `public int foo;`.  
References are the usage of the declared items. So for the above example it would be if the class has `if(foo > 10)` somewhere in the code.  
  
So, here are some examples of how you can search for certain things:  
**Searching for classes that use the onAttacked hook**  
![onAttacked search](https://user-images.githubusercontent.com/20698806/221278105-7fa055cd-dac0-419f-a354-ee6200e1e668.png)
  
**Searching for where the field purgeOnUse is checked**  
![purgeOnUse search](https://user-images.githubusercontent.com/20698806/221278333-9a6d0a8a-5e4a-4787-bf28-a76774be23da.png)
  
**Searching for where Chemical X is checked for**  
![ChemX Search](https://user-images.githubusercontent.com/20698806/221278620-a8812873-d400-4ad5-9d6e-ee09818878de.png)  
**Note**: this is actually a static final variable that the compiler replaced, please actually use `ChemicalX.ID` in your code.