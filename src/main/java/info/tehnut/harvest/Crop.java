package info.tehnut.harvest;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@JsonAdapter(Crop.Adapter.class)
public class Crop implements Predicate<BlockState> {

    private BlockState mature;
    private Block block;

    public Crop(Identifier name, JsonObject states) {
        RegistryWaiter.waitFor(
                blocks -> {
                    Block block = blocks.apply(name);
                    setMature(block, states);
                },
                (id, replacement) ->
                {
                    setMature(replacement, states);
                },
                Registry.BLOCK, name);
    }

    public Crop(BlockState state) {
        this.mature = state;
    }

    private void setMature(Block block, JsonObject states) {
        BlockState state = block.getDefaultState();
        for (Map.Entry<String, JsonElement> e : states.entrySet()) {
            Property property = block.getStateManager().getProperty(e.getKey());
            if (property != null) {
                String valueString = e.getValue().getAsString();
                Comparable value = (Comparable) property.parse(valueString).get();
                state = state.with(property, value);
            }
        }
        this.mature = state;
        Harvest.debug("Registered crop " + this + " from config");

    }

    public BlockState getMature() {
        return mature;
    }

    public Block getBlock() {
        return block == null ? block = mature.getBlock() : block;
    }

    @Override
    public boolean test(BlockState state) {
        return state == mature;
    }

    @Override
    public String toString() {
        if (mature != null) {
            return "Crop{" + mature + "}";
        } else {
            return "Crop{null}";
        }
    }

    public static class Adapter implements JsonSerializer<Crop>, JsonDeserializer<Crop> {
        @Override
        public Crop deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = element.getAsJsonObject();
            Identifier blockID = new Identifier(json.getAsJsonPrimitive("block").getAsString());
            JsonObject stateObject = json.getAsJsonObject("states");
            return new Crop(blockID, stateObject);
        }

        @Override
        public JsonElement serialize(Crop src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("block", Registry.BLOCK.getId(src.getBlock()).toString());

            String stateString = src.mature.toString();
            String[] properties = stateString.substring(stateString.indexOf("[") + 1, stateString.length() - 1).split(",");

            JsonObject states = new JsonObject();
            for (String property : properties) {
                String[] split = property.split("=");
                states.addProperty(split[0], split[1]);
            }
            object.add("states", states);

            return object;
        }
    }
}
