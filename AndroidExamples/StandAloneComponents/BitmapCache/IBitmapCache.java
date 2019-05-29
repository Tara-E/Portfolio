import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Allows caching of bitmaps
 */
public interface IBitmapCache {

    boolean addBitmap(String key, Bitmap bitmap);

    Bitmap getBitmap(String key);

    Bitmap removeBitmap(String key);

}
