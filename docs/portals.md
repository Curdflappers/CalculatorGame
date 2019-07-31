# Portals

Portals are a complexifying mechanic in the game. Some levels have portals: a pair of them, to be exact. One portal sits below the value, another floats above it. Each portal corresponds to exactly one position in the level's value, the ones position, tens position, hundreds position, etc. The portal on the bottom will always be to the left of the portal on the top.

When the value of the game is such that there is a digit directly above the bottom portal, that digit "falls through" the bottom portal and is re-added to the value through the top portal.

## Examples

In the examples below, the portals are represented with a `Y`.

### Stable

```txt
  Y
 12
Y
```

Nothing happens, this game is __stable__. But if we were to pad 3 to this value:

### Unstable

```txt
  Y
123
Y
```

This game is unstable. Since the `1` is directly above the lower portal, it will fall:

```txt
  Y
 24
Y
```

It gets added to the `3`, so the new digit is `4`.

### Cascade

Below is a __cascade__ (multiple iterations of falling before the game is stable):

```txt
  Y
991
Y

  Y
100
Y

  Y
  1
Y
```

If _multiple digits are to the left of the bottom portal_, they each fall individually:

```txt
   Y
1232
  Y

   Y
 125
  Y

   Y
  17
  Y

   Y
   8
  Y
```
