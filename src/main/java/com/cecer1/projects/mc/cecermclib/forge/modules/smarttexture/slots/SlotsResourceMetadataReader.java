package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.slots;

import com.cecer1.projects.mc.cecermclib.forge.utils.JsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SlotsResourceMetadataReader extends BaseMetadataSectionSerializer<SlotsResourceMetadata> {
    private static final int FORMAT_VERSION = 1;
    
    private static final SlotsResourceMetadataReader instance = new SlotsResourceMetadataReader();
    public static SlotsResourceMetadataReader getInstance() {
        return instance;
    }

    @Override
    public String getSectionName() {
        return "cecermclib_slots";
    }

    @Override
    public SlotsResourceMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        int formatVersion = JsonHelper.getInt(obj, "formatVersion", -1);
        if (formatVersion != FORMAT_VERSION) {
            throw new JsonParseException(String.format("Unsupported slots smart texture metadata version! supports %d but found %d", FORMAT_VERSION, formatVersion));
        }

        JsonObject slotsJson = obj.getAsJsonObject("slots");
        Map<String, Slot> slots = new HashMap<>();
        
        for (Map.Entry<String, JsonElement> slotEntry : slotsJson.entrySet()) {
            JsonObject slotJson = slotEntry.getValue().getAsJsonObject();
            slots.put(slotEntry.getKey(), new Slot(
                    JsonHelper.getInt(slotJson, "x"),
                    JsonHelper.getInt(slotJson, "y"),
                    JsonHelper.getInt(slotJson, "width"),
                    JsonHelper.getInt(slotJson, "height")
            ));
        }
        
        return new SlotsResourceMetadata(slots);
    }
}
