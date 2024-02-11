# Conditional Tip

This mod extends [Tips](https://www.curseforge.com/minecraft/mc-mods/tips).

## Requirements

Minecraft: 1.20.1<br>
Minecraft Forge: 47.0.0 and above<br>
Tips: 12.0.1 and above<br>

## Conditions

### Dimensions

* Whitelist:

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "dimensions": [
        "minecraft:the_nether",
        "minecraft:the_end"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you're in the nether or the end."
  }
}
```

* Blacklist:

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "excludeDimensions": [
        "minecraft:overworld"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you're not in the overworld."
  }
}
```

### Advancements

* Whitelist:

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "advancements": [
        "minecraft:story/lava_bucket"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you had the \"Hot Stuff\" advancement."
  }
}
```

* Blacklist:

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "unarchivedAdvancements": [
        "minecraft:story/lava_bucket"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you don't have the \"Hot Stuff\" advancement yet."
  }
}
```

### Multiple Conditions

* AND

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "dimensions": [
        "minecraft:the_nether"
      ],
      "advancements": [
        "minecraft:nether/root"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you're in the nether and had the advancement."
  }
}
```

* OR

```json
{
  "type": "conditional_tips:conditional_tip",
  "tip_conditions": [
    {
      "dimensions": [
        "minecraft:the_nether"
      ]
    },
    {
      "dimensions": [
        "minecraft:the_end"
      ]
    }
  ],
  "tip": {
    "text": "This tip is only shown when you're in the nether or the end."
  }
}
```