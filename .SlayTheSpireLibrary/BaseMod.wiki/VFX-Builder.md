# VFX Builder

Build your own visual effect using declarative primitives! This will quickly and easily build simple visual effects
without getting too bogged down in the math.

## How to use:
You can chain methods together to describe the desired behavior of the visual effect, then get the AbstractGameEffect
instance using the `build()` method.

Examples:

Shoot a growing star at an enemy. It begins from the player, lasts 0.8 seconds, grows from 80% to 220% in size,
has a yellow color, gets faster as it travels, and rotates clockwise.

```java
AbstractGameEffect shootStar = new VfxBuilder(ImageMaster.TINY_STAR, p.drawX, m.hb.cY, 0.8f)
    .scale(0.8f, 2.2f, VfxBuilder.Interpolations.SWING)
    .setColor(Color.GOLD)
    .moveX(p.drawX, m.drawX, VfxBuilder.Interpolations.EXP5IN)
    .rotate(-400f)
    .build()
```

Drop a rock from above. It bounces, and fades in and out. It also plays a sound partway through.
```java
AbstractGameEffect rockEffect = new VfxBuilder(rockTexture, m.hb.cX, 0f, 2f)
    .moveY(Settings.HEIGHT, m.hb.y + m.hb.height / 6, VfxBuilder.Interpolations.BOUNCE)
    .fadeIn(0.25f)
    .fadeOut(0.25f)
    .setScale(1.25f * m.hb.width / t.getWidth())
    .playSoundAt(0.35f,"BLUNT_HEAVY")
    .build();
```

A ball flies up and is pulled down offscreen.

```java
AbstractGameEffect ballEffect = new VfxBuilder(ballTexture, 1.5f)
    .velocity(MathUtils.random(45f, 135f), 300f)
    .gravity(1000f)
    .build();
```
## Available methods

| Method Name | Description | Notes |
| ----------- | ----------- | ------- |
| setX | Sets the horizontal position to a constant value | The functions that control position |
| setY | Sets the vertical position to a constant value | will override each other. In other |
| moveX | Animates horizontally from one point to another | words, calling e.g. `.arc(...)` |
| moveY | Animates vertically from one point to another | after calling `.moveY(...)` will |
| oscillateX | Animates horizontally back and forth | result in undefined behavior. |
| oscillateY | Animates vertically back and forth | |
| arc | Animates along a rounded arc | |
| gravity | Pulls down (or up) with constant acceleration | |
| velocity | Animates in a direction with constant velocity | |
| setColor | Colorize the image | The color will be copied, so it's OK to use `Color.BLUE`, for example |
| useAdditiveBlending | Use additive blending when rendering | |
| setAlpha | Sets the transparency to a constant value | |
| fadeIn | Fade in from transparent to full opacity | |
| fadeOut | Fade out to fully transparent | |
| oscillateAlpha | Oscillate transparency between two values | |
| setScale | Sets the draw scale to a constant value | |
| scale | Animates the draw scale | |
| oscillateScale | Oscillate the draw scale between two values | |
| setAngle | Sets the draw angle to a constant value | |
| rotateTo | Animates the draw angle from one value to another | |
| rotate | Rotates the draw angle at a constant rate | |
| wobble | Animates the draw angle back and forth between two values | |
| playSoundAt | Plays a sound effect | |
| triggerVfxAt | Trigger another (set of) VFX | To trigger a VFX with these functions, you pass in a `BiFunction` |
| emitEvery | Trigger another VFX periodically | which is called with the current x and y position and returns a new `AbstractGameEffect` |
| andThen | Start a new phase of the animation | |
| postRender | Calls a function after rendering each frame | This can help you do any custom rendering, like text or multiple images |
| whenStarted | Calls a function after starting this animation stage | |
| whenComplete | Calls a function at the end of this animation stage | |

## Interpolations

Many of the animation methods can take an interpolation paramter. See the [libGDX documentation on Interpolation](https://libgdx.com/wiki/math-utils/interpolation) for some helpful diagrams of the various interpolation types. The following interpolations are provided:

- LINEAR
- BOUNCE
- BOUNCEIN
- BOUNCEOUT
- CIRCLE
- CIRCLEIN
- CIRCLEOUT
- ELASTIC
- ELASTICIN
- ELASTICOUT
- EXP5
- EXP5IN
- EXP5OUT
- EXP10
- EXP10IN
- EXP10OUT
- FADE
- POW2
- POW2IN
- POW2IN_INVERSE
- POW2OUT
- POW2OUT_INVERSE
- POW3
- POW3IN
- POW3IN_INVERSE
- POW3OUT
- POW3OUT_INVERSE
- POW4
- POW4IN
- POW4OUT
- POW5
- POW5IN
- POW5OUT
- SINE
- SINEIN
- SINEOUT
- SMOOTH
- SMOOTH2
- SMOOTHER
- SWING
- SWINGIN
- SWINGOUT
