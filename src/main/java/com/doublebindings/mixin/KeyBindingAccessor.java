package com.doublebindings.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("KEY_TO_BINDINGS")
    static Map<InputUtil.Key, List<KeyBinding>> doublebindings$getKeyToBindings() {
        throw new AssertionError("Mixin accessor was not applied");
    }
}
