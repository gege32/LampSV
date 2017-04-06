package hu.gehorvath.lampsv.core.helpers.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import hu.gehorvath.lampsv.core.Preset;

public class PresetMapAdapter extends XmlAdapter<PresetMapAdapter.AdaptedMap, Map<Integer, Preset>>{

	
    public static class AdaptedMap {

        public List<Entry> preset = new ArrayList<>();

        public static class Entry {

            public Integer presetID;
            public Preset preset;

        }

    }

    @Override
    public AdaptedMap marshal(final Map<Integer, Preset> map) throws Exception {
        final AdaptedMap adaptedMap = new AdaptedMap();
        for (final Map.Entry<Integer, Preset> mapEntry : map.entrySet()) {
            final AdaptedMap.Entry entry = new AdaptedMap.Entry();
            entry.presetID = mapEntry.getKey();
            entry.preset = mapEntry.getValue();
            adaptedMap.preset.add(entry);
        }
        return adaptedMap;
    }

    @Override
    public Map<Integer, Preset> unmarshal(final AdaptedMap adaptedMap) throws Exception {
        final Map<Integer, Preset> map = new HashMap<>();
        for (final AdaptedMap.Entry entry : adaptedMap.preset) {
            map.put(entry.presetID, entry.preset);
        }
        return map;
    }
	
}
