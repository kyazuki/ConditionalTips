package kyazuki.conditionaltips;

import com.mojang.logging.LogUtils;
import kyazuki.conditionaltips.impl.ConditionalTip;
import net.darkhax.tipsmod.api.TipsAPI;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ConditionalTips.MODID)
public class ConditionalTips {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "conditional_tips";
    // Define mod name in a common place for everything to reference
    public static final String MOD_NAME = "Conditional Tips";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ConditionalTips() {
        LOGGER.info(MOD_NAME + " Loaded.");
        TipsAPI.registerTipSerializer(ConditionalTip.SERIALIZER_ID, ConditionalTip.SERIALIZER);
    }
}
