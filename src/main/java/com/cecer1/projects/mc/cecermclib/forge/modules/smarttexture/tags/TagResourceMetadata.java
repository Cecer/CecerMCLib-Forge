package com.cecer1.projects.mc.cecermclib.forge.modules.smarttexture.tags;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.client.resources.data.IMetadataSection;

import java.util.*;

public class TagResourceMetadata implements IMetadataSection {
    
    private final Map<String, int[]> tags;
    private final TIntObjectMap<Set<String>> tagsLookup;

    public Map<String, int[]> getTags() {
        return this.tags;
    }
    public int[] getTag(String tagName) {
        return this.tags.get(tagName);
    }
    public Set<String> getTags(int color) {
        Set<String> tags = this.tagsLookup.get(color & 0xffffff); // We don't care about the alpha channel
        if (tags == null) {
            return Collections.emptySet();
        }
        return tags;
    }

    public TagResourceMetadata(Map<String, int[]> tags) {
        this.tags = tags;

        // Build the tags lookup map
        this.tagsLookup = new TIntObjectHashMap<>();

        for (Map.Entry<String, int[]> tag : this.tags.entrySet()) {
            for (int color : tag.getValue()) {
                color &= 0xffffff;  // We don't care about the alpha channel
                Set<String> tagsForColor = this.tagsLookup.get(color);
                if (tagsForColor == null) {
                    tagsForColor = new HashSet<>();
                    this.tagsLookup.put(color, tagsForColor);
                }
                tagsForColor.add(tag.getKey());
            }
        }

        // Prevent modification of tags
        for (int color : this.tagsLookup.keys()) {
            this.tagsLookup.put(color, Collections.unmodifiableSet(this.tagsLookup.get(color)));
        }
    }
    
    // <editor-fold desc="[Equality]">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagResourceMetadata that = (TagResourceMetadata) o;
        return Objects.equals(tags, that.tags) &&
                Objects.equals(tagsLookup, that.tagsLookup);
    }
    @Override
    public int hashCode() {
        return Objects.hash(tags, tagsLookup);
    }

    // </editor-fold>
}
