Flavor text is a great way to add a little extra color to your cards.  They are a tooltip without a header.  You can add the flavor text to cards within the CardStrings.json.  Add a FLAVOR field like so:
```
  "${ModID}:PhantomFist": {
    "NAME": "Phantom Fist",
    "DESCRIPTION": "Deal !D! damage.",
    "FLAVOR": "It's like hitting them with a fist, except the fist is the size of a watermelon and made of steel."
  },
```
Cards without a FLAVOR field will simply not use the flavor text feature.

If for some reason you need to modify or read the flavor text in your code, there are two SpireFields you can use to do so.  
 `CardStringsFlavorField.FLAVOR` for the field in a CardStrings object, and `AbstractCardFlavorFields.flavor` for the field in an AbstractCard object.

It is recommended that you change the box color and text color from their default values.  Cards can have the color of the flavor text box set with the SpireField `AbstractCardFlavorFields.boxColor`.  They can also have the color of their text set with the SpireField `AbstractCardFlavorFields.textColor`.

The box type can also be set with `AbstractCardFlavorFields.boxType`.  WHITE is a white box with grey edges.  Setting the color is straightforward and the edges of the box are darker than the central part.  TRADITIONAL looks like a normal tip before applying color, so when you apply a light color it will still be darker.  The edges are also lighter than the central part.  CUSTOM is where you supply your own images for the box.  `AbstractCardFlavorFields.boxTop` needs to be set to a Texture object, this is an image for the top part of the box.  Ditto for boxMid and boxBot.  The middle texture will get stretched depending on the height of the box, so keep that in mind when designing a texture.

Flavor text for potions is now available!  Use `PotionFlavorFields` instead of `AbstractCardFlavorFields`.  The options are the same.

<br/><br/>

![Fist](https://user-images.githubusercontent.com/56576391/167802823-e6f76221-256e-4dc4-90ea-e7b5223f357a.png)
<p align="center"> Flavor text on a card. <\p>

<br/><br/>

![PotionFlavor](https://user-images.githubusercontent.com/56576391/167801617-d4e69ef1-2a8a-4f20-8560-b0bb7a19887a.png)
<p align="center"> Flavor text on a potion. </p>