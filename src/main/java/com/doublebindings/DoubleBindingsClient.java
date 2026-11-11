package com.doublebindings;

import com.doublebindings.mixin.KeyBindingAccessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoubleBindingsClient implements ClientModInitializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {
        Path config = net.minecraft.client.MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("config").resolve("doublebindings.json");
        try {
            Files.createDirectories(config.getParent());
            if (!Files.exists(config)) {
                JsonObject example = new JsonObject();
                example.add("key.jump", GSON.toJsonTree(List.of("KEYSYM:82")));
                Files.writeString(config, GSON.toJson(example), StandardCharsets.UTF_8);
            }

            JsonObject root = JsonParser.parseString(Files.readString(config)).getAsJsonObject();
            Map<InputUtil.Key, List<KeyBinding>> keyMap = KeyBindingAccessor.doublebindings$getKeyToBindings();

            for (String bindingId : root.keySet()) {
                KeyBinding target = KeyBinding.byId(bindingId);
                if (target == null) {
                    System.err.println("[DoubleBindings] Unknown key binding ID: " + bindingId);
                    continue;
                }

                for (var element : root.getAsJsonArray(bindingId)) {
                    InputUtil.Key alias = parseKey(element.getAsString());
                    if (alias == null) {
                        System.err.println("[DoubleBindings] Invalid alias for " + bindingId + ": " + element);
                        continue;
                    }

                    List<KeyBinding> existing = keyMap.computeIfAbsent(alias, ignored -> new ArrayList<>());
                    if (!existing.isEmpty() && !existing.contains(target)) {
                        System.err.println("[DoubleBindings] Skipped " + element + " for " + bindingId
                                + " because that key is already assigned to another action. Unbind it first.");
                        continue;
                    }
                    if (!existing.contains(target)) existing.add(target);
                }
            }
            System.out.println("[DoubleBindings] Loaded extra key assignments from " + config);
        } catch (IOException | RuntimeException exception) {
            System.err.println("[DoubleBindings] Could not load config: " + exception.getMessage());
        }
    }

    private static InputUtil.Key parseKey(String encoded) {
        try {
            String[] parts = encoded.split(":", 2);
            if (parts.length != 2) return null;
            int code = Integer.parseInt(parts[1]);
            return switch (parts[0].toUpperCase()) {
                case "KEYSYM" -> InputUtil.Type.KEYSYM.createFromCode(code);
                case "MOUSE" -> InputUtil.Type.MOUSE.createFromCode(code);
                default -> null;
            };
        } catch (RuntimeException ignored) {
            return null;
        }
    }
}
