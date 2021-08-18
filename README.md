# Harvest [![](http://cf.way2muchnoise.eu/full_simplerharvest_downloads.svg)![](http://cf.way2muchnoise.eu/versions/simplerharvest.svg)](https://www.curseforge.com/minecraft/mc-mods/simplerharvest)

Simple right-click crop harvesting and replanting. Configurable via ModMenu (or by manually editing the JSON config file).

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
  "crops": {
    "wheatEnabled": true,
    "wheatStage": 7,
    "netherWartEnabled": true,
    "netherWartStage": 3,
    "carrotEnabled": true,
    "carrotStage": 7,
    "potatoEnabled": true,
    "potatoStage": 7,
    "beetrootEnabled": true,
    "beetrootStage": 3
  }
}
```
