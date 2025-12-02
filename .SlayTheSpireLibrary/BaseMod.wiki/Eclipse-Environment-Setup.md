# Setting up Eclipse

## Downloads 

1. The Eclipse IDE: [link](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/oxygen/R2/eclipse-inst-win64.exe)
2. Maven: [link](https://maven.apache.org/download.cgi)

## New Project

If this is your first time using Eclipse it will prompt you to select a *Workspace* location when you open it up. For the *Workspace* **DO NOT** use the default workspace. Instead we're going to use your `my_mods` folder as the workspace. If you did end up using the default workspace or have used Eclipse before we're going to switch workspaces over to `my_mods`. The black bars in the image are just covering up some personal information. ![Switch Workspace](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/switch_workspace.png) ![New Workspace](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/new_workspace.png)

Now we can go ahead an create our mod using what Eclipse calls a project. So we will go to `File` -> `New` -> `Java Project`. ![New Java Project](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/new_java_project.png) This will popup a modal. In the spot for the name of the project enter `example_mod` and then click `Finish`. ![New Java Project Modal](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/new_java_project_modal.png). This will create a new project for your mod! However we're not done yet because we have to configure its **dependencies**. Remember how earlier we put `ModTheSpire.jar` and `BaseMod.jar` in the `lib` folder? Well now we have to tell Eclipse that we actually put the dependencies there since it won't automatically know.

## Dependencies
Your mod actually has one more dependency that we didn't download earlier (because if we could have downloaded it earlier it would be piracy). This is because we now need `desktop-1.0.jar` as a dependency. So copy it over from your **Slay The Spire** directory into `lib` so that your `lib` folder now looks like this. ![Final Lib Folder](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/final_lib_folder.png).

To tell Eclipse about these dependencies we're going to need to go into the settings for your `example_mod`. To do this right click on `example_mod` and go down to `Properties`. ![Properties](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/mod_properties.png).

From here we're going to go to `Java Build Path` -> `Libraries`. ![Build Path](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/build_path.png).

Now use `Add External JARs...` to add `ModTheSpire.jar`, `BaseMod.jar`, and `desktop-1.0.jar`. Click okay once you've added all of them. It should look like this: ![All Jars](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/all_jars.png).

## Maven
Slay The Spire mods for the most part use [Maven](https://maven.apache.org/) for building. If you have not installed Maven, go ahead and do that first. Maven requires your mod's folder structure to be set up in a specific way. Specifically all of your source code will need to be in the `src/main/java` folder. To make this happen go back to the settings for your mod. ![Properties Again](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/mod_properties.png).

From here we're going to go to `Java Build Path` -> `Source` and then highlight the `example_mod/src` folder and click `Remove` to remove it. Then use `Add Folder` to add `src/main/java` as a source folder. Click okay once you've done this. It should like like: ![New Source Folder](https://raw.githubusercontent.com/daviscook477/BaseMod/master/github_resources/wiki/new_source_folder.png)

You will need to have a file called `pom.xml` in your top level directory. This is required for Maven to build your project. An example `pom.xml` file can be found in this [gist](https://gist.github.com/alexdriedger/fb74397086ee80073417f19d6305bb05).

Make sure your dependencies are pointing to the location of the correct `.jar` files. `${basedir}` is the directory where your `pom.xml` file is located. `..` is the parent directory.

```Xml
<dependency>
    <groupId>basemod</groupId>
    <artifactId>basemod</artifactId>
    <version>2.10.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/../lib/BaseMod.jar</systemPath> // systemPath should be the path to where BaseMod.jar is located
</dependency>
```

Use **`mvn package`** to build your mod! Maven will build a `.jar` file with your mod. Your compiled `.jar` file location is specified in the `pom.xml` file

```Xml
<target>
    <copy file="target/ExampleMod.jar" tofile="../lib/ExampleMod.jar"/> // tofile location is where you can find your compiled .jar file
</target>
```

## Actually Modding!

You can start making your mod at the next part of the tutorial here:

* Starting Your Mod: [Link](https://github.com/daviscook477/BaseMod/wiki/Starting-Your-Mod)
