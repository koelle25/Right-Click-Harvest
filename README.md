# Harvest [![](http://cf.way2muchnoise.eu/full_simplerharvest_downloads.svg)![](http://cf.way2muchnoise.eu/versions/simplerharvest.svg)](https://minecraft.curseforge.com/projects/simplerharvest)

Adds right click crop harvesting that is configurable via a JSON file.

This branch depends on [Fabric-Loader](https://fabricmc.net/) and [Fabric](https://minecraft.curseforge.com/projects/fabric).

## FAQ
- Client or Server side?
  - This mod is not required on the clientside. However, the hand swinging animation on right clicking will be inconsistent without it.
- Is Fortune accounted for?
  - Yes. The Fortune value is pulled from the item in your **main hand**.
- Forge Port?
  - No. However you are within the license of both this mod and TehNut's original to do so yourself.

## Default Config
```json
{
  "exhaustionPerHarvest": 0.005,
  "additionalLogging": false,
  "crops": [
    {
      "block": "minecraft:wheat",
      "states": {
        "age": "7"
      }
    },
    {
      "block": "minecraft:nether_wart",
      "states": {
        "age": "3"
      }
    },
    {
      "block": "minecraft:carrots",
      "states": {
        "age": "7"
      }
    },
    {
      "block": "minecraft:potatoes",
      "states": {
        "age": "7"
      }
    },
    {
      "block": "minecraft:beetroots",
      "states": {
        "age": "3"
      }
    }
  ]
}
```
