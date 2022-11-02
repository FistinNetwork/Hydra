package fr.fistin.hydra.api.protocol.data;

import fr.fistin.hydra.api.HydraAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 01/11/2022 at 14:16
 */
public class HydraData {

    /** The dictionary that contains data linked to a key.  */
    private final Map<String, String> data = new HashMap<>();

    /**
     * Add a data into the dictionary.
     *
     * @param key The key of the data to add
     * @param value The value of the data to add
     * @return This {@link HydraData} object
     */
    public HydraData add(String key, String value) {
        this.data.put(key, value);
        return this;
    }

    /**
     * Get a data from the dictionary.
     *
     * @param key The key of the data to get
     * @return <code>null</code> if the data was not found or a {@link String}
     */
    public String get(String key) {
        return this.data.get(key);
    }

    /**
     * Add a data into the dictionary as an object.<br>
     * The object will be serialized in JSON format.
     *
     * @param key The key of the data to add
     * @param value The value of the data to add
     * @return This {@link HydraData} object
     */
    public HydraData addObject(String key, Object value) {
        this.data.put(key, HydraAPI.GSON.toJson(value));
        return this;
    }

    /**
     * Get a data of the dictionary as an object.<br>
     * The object will be deserialized from a JSON.
     *
     * @param key The key of the data to get
     * @param outputClass The class of the output object
     * @return The data deserialized as an object
     * @param <T> The type of the object to return
     */
    public <T> T getObject(String key, Class<T> outputClass) {
        final String json = this.data.get(key);

        return json == null ? null : HydraAPI.GSON.fromJson(json, outputClass);
    }

    /**
     * Add {@linkplain RedisData redis data} to the dictionary.
     *
     * @param data The data to add
     * @return This {@link HydraData} object
     */
    public HydraData addRedisData(RedisData data) {
        return this.addObject("redis", data);
    }

    /**
     * Get {@linkplain RedisData redis data} from the dictionary (only if present).
     *
     * @return The {@link RedisData} object; or <code>null</code> if nothing was found
     */
    public RedisData getRedisData() {
        return this.getObject("redis", RedisData.class);
    }

}
