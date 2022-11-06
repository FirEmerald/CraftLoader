[![Discord](https://img.shields.io/discord/176190900945289237?style=flat-square&logo=discord&logoColor=ffffff&label=Discord)](https://discord.gg/ykHRhmC)  
[![Curseforge](https://cf.way2muchnoise.eu/short_590683_downloads.svg?badge_style=flat)](https://www.curseforge.com/minecraft/mc-mods/craftloader)  

# CraftLoader
CraftLoader is a small framework mod that allows other modders to register "loaders" for their own crafting systems, such as machines, and loads them from resource packs, saves, and mods themselves.

A very bad explanation of how to use:  
 * Recipe factories, registered the same way as vanilla crafting table recipes, are found in assets/\<recipe owner mod ID\>/crafting/\<loader owner mod ID\>/\<name\>_factories.json for mods and resource packs, and data/crafting/\<recipe owner mod ID\>/\<loader owner mod ID\>/\<name\>_factories.json - for instance, with a mod of ID "examplemod", smelting factories would be found in assets/examplemod/crafting/minecraft/smelting_factories.json for mods and resourcepacks, and data/crafting/examplemod/minecraft/smelting_factories.json for saves
 * Similarly, the recipes themselves are defined in assets/\<recipe owner mod ID\>/crafting/\<loader owner mod ID\>/\<name\>.json and data/crafting/\<recipe owner mod ID\>/\<loader owner mod ID\>/\<name\>.json, respectively.
 * Transformers, which are a map of String types to maps of string keys to lists of replacement values, can be used to greatly reduce the number of required recipes. They are defined in assets/\<recipe owner mod ID\>transformers.json and data/crafting/\<recipe owner mod ID\>/transformers.json.
 * A recipe's name is \<recipe owner mod ID\>:\<recipe key name from JSON after being transformed\>. Other mod's loaders may require additional data from inside the recipe definition to create a more specific recipe key.
 * A recipe can be disabled by adding "disabled":true to the JSON. if another recipe JSON re-declares that recipe with "disabled:false" afterwards, the recipe is not disabled.
 * A recipe's transformers are applied by listing them in a string array under "transformers", I.E. "transformers": [ "vanilla_metals" ]
 * to use another mod's transformers, prefix the transformer name with the mod ID.
 * recipes can use constants from the recipe owner's mod's constants JSON as inputs as well as outputs - in the case of output, the first matching stack is used.
 * Examples can be found in the examples folder.
 * To register your own loader, use the methods in the CraftLoaderAPI class.
