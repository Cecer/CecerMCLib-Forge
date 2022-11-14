package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.nslice;

import com.cecer1.projects.mc.cecermclib.forge.utils.JsonHelper;
import com.google.gson.*;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;

import java.lang.reflect.Type;

public class NSliceResourceMetadataReader extends BaseMetadataSectionSerializer<NSliceResourceMetadata> {
    private static final int FORMAT_VERSION = 1;
    
    private static final NSliceResourceMetadataReader instance = new NSliceResourceMetadataReader();
    public static NSliceResourceMetadataReader getInstance() {
        return instance;
    }

    @Override
    public String getSectionName() {
        return "cecermclib_nslice";
    }

    @Override
    public NSliceResourceMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        
        int formatVersion = JsonHelper.getInt(obj, "formatVersion", -1);
        if (formatVersion != FORMAT_VERSION) {
            throw new JsonParseException(String.format("Unsupported smarttexture tag metadata version! supports %d but found %d", FORMAT_VERSION, formatVersion));
        }

        JsonArray rowsJson = obj.getAsJsonArray("rows");
        JsonArray columnsJson = obj.getAsJsonArray("columns");
        
        if (rowsJson == null || rowsJson.size() == 0) {
            throw new JsonParseException("Invalid n-slice metadata! rows must be a JSON array of at least 1 slice specification");
        }
        if (columnsJson == null || columnsJson.size() == 0) {
            throw new JsonParseException("Invalid n-slice metadata! columns must be a JSON array of at least 1 slice specification");
        }

        NSliceSlice[] rows = new NSliceSlice[rowsJson.size()];
        NSliceSlice[] columns = new NSliceSlice[columnsJson.size()];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = this.deserializeSlice(rowsJson.get(i), i);
        }
        for (int i = 0; i < columns.length; i++) {
            columns[i] = this.deserializeSlice(columnsJson.get(i), i);
        }

        return new NSliceResourceMetadata(rows, columns);
    }

    private NSliceSlice deserializeSlice(JsonElement rowElement, int index) throws JsonParseException {
        if (!rowElement.isJsonObject()) {
            throw new JsonParseException("Invalid n-slice metadata! rows[" + index + "] is not a valid slice specification");
        }
        JsonObject rowObject = rowElement.getAsJsonObject();
        
        int size = JsonHelper.getInt(rowObject, "size");
        String growBehaviourStr = JsonHelper.getString(rowObject, "growBehaviour");
        int growWeight = JsonHelper.getInt(rowObject, "growWeight");

        NSliceGrowBehaviour growBehaviour = NSliceGrowBehaviour.valueOf(growBehaviourStr);
        return new NSliceSlice(size, growBehaviour, growWeight);
    }
}
