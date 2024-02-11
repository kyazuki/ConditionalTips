package kyazuki.conditionaltips.mixin;

import kyazuki.conditionaltips.impl.ConditionalTip;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TipsAPI.class)
public abstract class TipsAPIMixin {
    @Inject(method = "canDisplayTip", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onCanDisplayTip(ITip tip, CallbackInfoReturnable<Boolean> ci) {
        if (tip instanceof ConditionalTip) {
            ConditionalTip cTip = (ConditionalTip) tip;
            if (!cTip.canDisplay()) {
                ci.setReturnValue(false);
                ci.cancel();
            }
        }
    }
}
