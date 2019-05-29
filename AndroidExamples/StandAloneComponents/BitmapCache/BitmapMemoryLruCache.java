import android.graphics.Bitmap;
import android.util.LruCache;

import javax.inject.Inject;

/**
 * An implementation of IBitmapCache that uses a {@Link LruCache} cache with a fixed size to
 * temporarily store bitmaps in a HashMap.
 * <p>
 * If the maximum memory size is exceeded, the oldest bitmap(s) will be removed.
 */
public class BitmapMemoryLruCache implements IBitmapCache {
    private static final double MAX_PERCENT_OF_AVAILABLE_MEMORY_TO_USE = 0.2;
    private LruCache<String, Bitmap> _bitmapCache;

    @Inject
    public BitmapMemoryLruCache() {
        _bitmapCache = new LruCache<String, Bitmap>(calculateMaxMemory()) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private int calculateMaxMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        return (int) (maxMemory * MAX_PERCENT_OF_AVAILABLE_MEMORY_TO_USE);
    }

    @Override
    public boolean addBitmap(String key, Bitmap bitmap) {
        if (getBitmap(key) == null) {
            _bitmapCache.put(key, bitmap);
            return true;
        }
        return false;
    }

    @Override
    public Bitmap getBitmap(String key) {
        return _bitmapCache.get(key);
    }

    @Override
    public Bitmap removeBitmap(String key) {
        return _bitmapCache.remove(key);
    }
}
