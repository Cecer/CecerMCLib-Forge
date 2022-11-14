package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags;

import com.cecer1.projects.mc.cecermclib.forge.utils.JsonHelper;
import com.google.gson.*;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TagResourceMetadataReader extends BaseMetadataSectionSerializer<TagResourceMetadata> {
    private static final int FORMAT_VERSION = 1;

    private static final TagResourceMetadataReader instance = new TagResourceMetadataReader();
    public static TagResourceMetadataReader getInstance() {
        return instance;
    }
    
    @Override
    public String getSectionName() {
        return "cecermclib_tags";
    }
    
    @Override
    public TagResourceMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        
        int formatVersion = JsonHelper.getInt(obj, "formatVersion", -1);
        if (formatVersion != FORMAT_VERSION) {
            throw new JsonParseException(String.format("Unsupported tags smart texture metadata version! supports %d but found %d", FORMAT_VERSION, formatVersion));
        }

        JsonObject tagsJson = obj.getAsJsonObject("tags");
        Map<String, int[]> tags = new HashMap<>();

        for (Map.Entry<String, JsonElement> tagEntry : tagsJson.entrySet()) {
            JsonArray tagJson = tagEntry.getValue().getAsJsonArray();
            
            int[] colors = new int[tagJson.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = tagJson.get(i).getAsInt();
            }
            
            tags.put(tagEntry.getKey(), colors);
        }

        return new TagResourceMetadata(tags);
    }
}
