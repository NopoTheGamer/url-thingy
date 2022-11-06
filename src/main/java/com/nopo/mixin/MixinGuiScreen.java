package com.nopo.mixin;

import net.minecraft.client.gui.GuiScreen;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URI;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "openWebLink", at = @At("HEAD"), cancellable = true)
    public void onInitGui(URI url, CallbackInfo ci) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object) null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        } catch (Throwable var4) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Couldn't open link", var4);
            }
        }
        ci.cancel();
    }
}
