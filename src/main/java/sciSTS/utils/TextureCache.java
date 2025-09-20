package sciSTS.utils;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    public static Map<String, Texture> cache = new HashMap<>();

    public static void cacheTexture(String key, Texture texture) {
        if (!cache.containsKey(key)) {
            cache.put(key, new Texture(texture.getTextureData()));
        }
    }

    public static Texture getCachedTexture(String key) {
        return cache.get(key);
    }

    public static void clearCache() {
        for (Texture texture : cache.values()) {
            texture.dispose();
        }
        cache.clear();
    }
}