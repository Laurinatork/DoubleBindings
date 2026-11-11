DoubleBindings 1.0.0 — Minecraft 1.21.11 / Fabric

WHAT IT DOES
Adds alternative keys to existing Minecraft key bindings. For example, a
binding can be activated by its normal key OR an extra keyboard/mouse key.
It does not make one key trigger multiple different actions.

CONFIGURATION
After the first launch, edit:
  .minecraft/config/doublebindings.json

Example:
{
  "key.jump": ["KEYSYM:82"],
  "key.sneak": ["MOUSE:3"]
}

KEY FORMAT
  KEYSYM:<number>  Keyboard key (GLFW key code)
  MOUSE:<number>   Mouse button (GLFW button index; 0=left, 1=right, 2=middle)

The example maps R (GLFW key code 82) as an extra jump key, and mouse button
3 as an extra sneak key. Keep the normal in-game key assignment as-is.

BUILDING THE JAR
A compiled JAR is not included in this source archive. The build requires
internet access to download Minecraft/Fabric/Gradle dependencies.

Option 1 — local build:
  1. Install JDK 21 and Gradle 8.14.3 or newer compatible with Fabric Loom.
  2. Open a terminal in this folder.
  3. Run: gradle build
  4. Find the JAR in: build/libs/

Option 2 — GitHub Actions:
  1. Create a GitHub repository and upload the contents of this folder.
  2. Open the repository's Actions tab.
  3. Run "Build DoubleBindings" (or push to main/master).
  4. Download the artifact named "DoubleBindings-1.21.11" from the run.

INSTALLATION
Put the compiled JAR in .minecraft/mods. Install Fabric Loader for Minecraft
1.21.11. This is a client-side mod. Back up your config before editing.

NOTE
The mod only provides alternate local key assignments. Check each server's
rules before using key-remapping features there. It does not bypass anti-cheat.
