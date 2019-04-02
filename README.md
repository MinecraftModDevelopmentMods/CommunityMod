# CommunityMod
A mod made together by the community!

Coding mods yourself is hard, so why not make one with some help from the community? We will be accepting virtually all pull requests to this repo. Each push to this repo will cause a new mod version to up uploaded to our Maven and to CurseForge. To keep things fun and safe for everyone we will be reviewing PRs for the following things.

1. README.md file must not be edited.
2. Gradle files (including build.gradle) must not be edited. 
3. Any new deps must be soft deps.
4. Must not contain or result in illegal content or activity. Including violating the TOS of any platforms involved.
5. Code must not be obfuscated. 
6. License must not be edited.
7. Sub mods must not crash the game on purpose

PRs may be denied for other reasons as well and ultimately the project maintainers will have the final say. Project maintainers may also grant exceptions to these rules on an individual basis. For example, if you would like to add a soft-dep on a mod please contact us on [Discord](https://discord.mcmoddev.com)

## Sub Mods

To help reduce the number of merge conflicts, we have added a SubMod system. This system allows you to add your content as a module within the rest of the mod. To make a SubMod, add the @SubMod annotation to your class, and make sure it implements ISubMod. This system is very similar to forge's @Mod system. You can find an example SubMod [here](https://github.com/MinecraftModDevelopmentMods/CommunityMod/blob/master/src/main). While you are **NOT** required to use this system, it is highly recommended. 
