import android.content.Context;
import android.graphics.Bitmap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * An implementation of IBitmapCache that uses an LRU disk cache with a fixed size to
 * temporarily store serialized bitmaps as files.
 * <p>
 * If the maximum cache size is exceeded, the oldest bitmap file(s) will be deleted.
 * Multiple {@Link BitmapDiskLruCache} instances may share the same directory.
 * Bitmaps are stored uncompressed for better performance.
 */
public class BitmapDiskLruCache implements IBitmapCache {
    private static final long DISK_CACHE_MAX_SIZE = 1024 * 1024 * 20; // 20MB
    private static final String DISK_CACHE_SUB_DIR = "BitmapDiskCache";

    private final File _cacheDir;
    private final Object _diskCacheLock = new Object();

    @Inject
    public BitmapDiskLruCache(Context context) {
        if (context == null) {
            throw new IllegalStateException("Context has not been set.");
        }

        String path = context.getCacheDir().getAbsolutePath() + File.separator + DISK_CACHE_SUB_DIR;

        _cacheDir = new File(path);
        if (!_cacheDir.exists()) {
            _cacheDir.mkdirs();
        }

        trimToSize();
    }

    @Override
    public boolean addBitmap(String key, Bitmap bitmap) {
        synchronized (_diskCacheLock) {
            File file = new File(_cacheDir, key);
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                writeBitmap(bitmap, out);

            } catch (IOException e) {
                if (file != null && file.exists()) {
                    file.delete();
                }
                return false;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        trimToSize();
        return true;
    }

    @Override
    public Bitmap getBitmap(String key) {
        Bitmap bitmap = null;

        synchronized (_diskCacheLock) {
            File file = new File(_cacheDir, key);
            if (file == null || !file.exists()) {
                return null;
            }

            InputStream in = null;
            try {
                in = new FileInputStream(file);
                bitmap = readBitmap(new FileInputStream(file));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Could not read bitmap file.", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            return bitmap;
        }
    }

    @Override
    public Bitmap removeBitmap(String key) {
        Bitmap bitmap = getBitmap(key);

        synchronized (_diskCacheLock) {
            deleteFile(new File(_cacheDir, key));
            return bitmap;
        }
    }

    private void deleteFile(File file) {
        synchronized (_diskCacheLock) {
            if (file != null && file.exists() && !file.delete()) {
                throw new RuntimeException("Could not delete bitmap file " + file);
            }
        }
    }

    private void trimToSize() {
        List<File> fileList = getFiles();
        while (getSize() > DISK_CACHE_MAX_SIZE) {
            synchronized (_diskCacheLock) {

                File toEvict = fileList.remove(0);
                if (toEvict == null) {
                    break;
                }

                deleteFile(toEvict);
            }
        }
    }

    private long getSize() {
        return FileUtils.sizeOfDirectory(_cacheDir);
    }

    private List<File> getFiles() {
        List<File> fileList = new ArrayList<>(Arrays.asList(_cacheDir.listFiles()));
        sortFilesByDateAscending(fileList);
        return fileList;
    }

    private void sortFilesByDateAscending(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {

                if (f1.lastModified() > f2.lastModified()) {
                    return 1;
                } else if (f1.lastModified() < f2.lastModified()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    private static class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 6879914159857742532L;
        byte[] bytes;
        int width;
        int height;
        Bitmap.Config config;
    }

    private void writeBitmap(Bitmap bitmap, OutputStream outputStream) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            BitmapDataObject data = new BitmapDataObject();
            data.bytes = BitmapSerializer.toByteArrayNoCompression(bitmap);
            data.config = bitmap.getConfig();
            data.width = bitmap.getWidth();
            data.height = bitmap.getHeight();

            out.writeObject(data);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private Bitmap readBitmap(InputStream inStream) throws IOException, ClassNotFoundException {
        Bitmap bitmap = null;

        if (inStream != null) {
            ObjectInputStream in = new ObjectInputStream(inStream);
            BitmapDataObject data = (BitmapDataObject) in.readObject();
            bitmap = BitmapSerializer.fromByteArrayNoCompression(data.bytes, data.width, data.height, data.config);
            in.close();
        }

        return bitmap;
    }
}