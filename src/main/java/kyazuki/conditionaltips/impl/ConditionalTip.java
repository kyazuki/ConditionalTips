package kyazuki.conditionaltips.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.api.serialization.ISerializer;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITipSerializer;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.resources.SimpleTip;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConditionalTip extends SimpleTip {
    public static final ResourceLocation SERIALIZER_ID = new ResourceLocation("conditional_tips", "conditional_tip");
    /**
     * The serializer for this type of conditional tip.
     */
    public static final ITipSerializer<ConditionalTip> SERIALIZER = new ConditionalTipSerializer();

    private final Optional<List<TipCondition>> conditions;

    public ConditionalTip(ResourceLocation id, Component title, Component text, Optional<Integer> cycleTime,
                          Optional<List<TipCondition>> conditions) {
        super(id, title, text, cycleTime);
        this.conditions = conditions;
    }

    public boolean canDisplay() {
        return conditions.map(tipConditions -> tipConditions.stream().anyMatch(TipCondition::canDisplay)).orElse(true);
    }

    protected static class TipCondition implements ISerializer<TipCondition> {
        private final Optional<List<ResourceLocation>> dimensions;
        private final Optional<List<ResourceLocation>> excludeDimensions;
        private final Optional<List<ResourceLocation>> advancements;
        private final Optional<List<ResourceLocation>> unarchivedAdvancements;

        public TipCondition(Optional<List<ResourceLocation>> dimensions,
                            Optional<List<ResourceLocation>> excludeDimensions,
                            Optional<List<ResourceLocation>> advancements,
                            Optional<List<ResourceLocation>> unarchivedAdvancements) {
            this.dimensions = dimensions;
            this.excludeDimensions = excludeDimensions;
            this.advancements = advancements;
            this.unarchivedAdvancements = unarchivedAdvancements;
        }

        private TipCondition() {
            this.dimensions = Optional.empty();
            this.excludeDimensions = Optional.empty();
            this.advancements = Optional.empty();
            this.unarchivedAdvancements = Optional.empty();
        }

        public boolean canDisplay() {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                return false;
            } else {
                // Check dimension
                ResourceLocation dimension = player.level().dimension().location();
                if (dimensions.isPresent() && !dimensions.get().contains(dimension)) {
                    return false;
                }
                if (excludeDimensions.isPresent() && excludeDimensions.get().contains(dimension)) {
                    return false;
                }
                // Check advancement
                AdvancementList advs = player.connection.getAdvancements().getAdvancements();
                Map<Advancement, AdvancementProgress> progressMap = player.connection.getAdvancements().progress;
                if (advancements.isPresent() && advancements.get().stream().anyMatch((ad) -> {
                    AdvancementProgress p = progressMap.get(advs.get(ad));
                    return p == null || !p.isDone();
                })) {
                    return false;
                }
                if (unarchivedAdvancements.isPresent() && unarchivedAdvancements.get().stream().anyMatch((ad) -> {
                    AdvancementProgress p = progressMap.get(advs.get(ad));
                    return p == null || p.isDone();
                })) {
                    return false;
                }
            }
            return true;
        }

        public static List<TipCondition> fromJSONListStatic(JsonArray array) {
            TipCondition tc = new TipCondition();
            return tc.fromJSONList(array);
        }

        public static JsonElement toJSONListStatic(List<TipCondition> conditions) {
            TipCondition tc = new TipCondition();
            return tc.toJSONList(conditions);
        }

        @Override
        public TipCondition fromJSON(JsonElement json) {
            JsonObject object = json.getAsJsonObject();
            final Optional<List<ResourceLocation>> dimensions;
            final Optional<List<ResourceLocation>> excludeDimensions;
            final Optional<List<ResourceLocation>> advancements;
            final Optional<List<ResourceLocation>> unarchivedAdvancements;
            if (object.has("dimensions")) {
                dimensions = Optional.ofNullable(Serializers.RESOURCE_LOCATION.fromJSONList(object.get("dimensions")));
            } else {
                dimensions = Optional.empty();
            }
            if (object.has("excludeDimensions")) {
                excludeDimensions = Optional.ofNullable(Serializers.RESOURCE_LOCATION.fromJSONList(object.get(
                    "excludeDimensions")));
            } else {
                excludeDimensions = Optional.empty();
            }
            if (object.has("advancements")) {
                advancements = Optional.ofNullable(Serializers.RESOURCE_LOCATION.fromJSONList(object.get(
                    "advancements")));
            } else {
                advancements = Optional.empty();
            }
            if (object.has("unarchivedAdvancements")) {
                unarchivedAdvancements = Optional.ofNullable(Serializers.RESOURCE_LOCATION.fromJSONList(object.get(
                    "unarchivedAdvancements")));
            } else {
                unarchivedAdvancements = Optional.empty();
            }
            return new TipCondition(dimensions, excludeDimensions, advancements, unarchivedAdvancements);
        }

        @Override
        public JsonElement toJSON(TipCondition c) {
            JsonObject object = new JsonObject();
            if (dimensions.isPresent()) {
                Serializers.RESOURCE_LOCATION.toJSONList(object, "dimensions", dimensions.get());
            }
            if (excludeDimensions.isPresent()) {
                Serializers.RESOURCE_LOCATION.toJSONList(object, "excludeDimensions", excludeDimensions.get());
            }
            if (advancements.isPresent()) {
                Serializers.RESOURCE_LOCATION.toJSONList(object, "advancements", advancements.get());
            }
            if (unarchivedAdvancements.isPresent()) {
                Serializers.RESOURCE_LOCATION.toJSONList(object, "unarchivedAdvancements",
                    unarchivedAdvancements.get());
            }
            return object;
        }

        @Override
        public TipCondition fromByteBuf(FriendlyByteBuf friendlyByteBuf) {
            throw new NotImplementedException();
        }

        @Override
        public void toByteBuf(FriendlyByteBuf friendlyByteBuf, TipCondition tipCondition) {
            throw new NotImplementedException();
        }

        @Override
        public Tag toNBT(TipCondition tipCondition) {
            throw new NotImplementedException();
        }

        @Override
        public TipCondition fromNBT(Tag tag) {
            throw new NotImplementedException();
        }
    }

    private static final class ConditionalTipSerializer implements ITipSerializer<ConditionalTip> {

        @Override
        public ConditionalTip fromJSON(ResourceLocation id, JsonObject json) {
            final Component title = Serializers.TEXT.fromJSON(json, "title", TipsAPI.DEFAULT_TITLE);
            final Component text = Serializers.TEXT.fromJSON(json, "tip");
            final Optional<Integer> cycleTime = Serializers.INT.fromJSONOptional(json, "cycleTime");
            final Optional<List<TipCondition>> conditions;
            if (json.has("tip_conditions") && json.get("tip_conditions").isJsonArray()) {
                conditions =
                    Optional.ofNullable(TipCondition.fromJSONListStatic(json.get("tip_conditions").getAsJsonArray()));
            } else {
                conditions = Optional.empty();
            }

            if (title == null) {
                throw new JsonParseException("Tip " + id.toString() + " does not have a title. This is required!");
            } else if (text == null) {
                throw new JsonParseException("Tip " + id.toString() + " does not have text. This is required.");
            }

            return new ConditionalTip(id, title, text, cycleTime, conditions);
        }

        @Override
        public JsonObject toJSON(ConditionalTip toWrite) {
            final JsonObject json = new JsonObject();
            Serializers.RESOURCE_LOCATION.toJSON(json, "type", SERIALIZER_ID);
            Serializers.TEXT.toJSON(json, "title", toWrite.getTitle());
            Serializers.TEXT.toJSON(json, "tip", toWrite.getText());
            Serializers.INT.toJSONOptional(json, "cycleTime",
                (toWrite.getCycleTime() != TipsModCommon.CONFIG.defaultCycleTime) ?
                    Optional.of(toWrite.getCycleTime()) : Optional.empty());
            if (toWrite.conditions.isPresent()) {
                json.add("tip_conditions", TipCondition.toJSONListStatic(toWrite.conditions.get()));
            }
            return json;
        }
    }
}
