package com.android.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.util.LruCache;

import com.android.volley.disklrucache.DiskLruImageCache;
import com.android.volley.toolbox.ImageLoader;

public class MGImageCache implements ImageLoader.ImageCache {
	private static MemoryCache memoryCache;
	private static FileCache fileCache;

	private static class MemoryCache {
		private LruCache<String, Bitmap> cache;

		public MemoryCache() {
			final long maxMemory = Runtime.getRuntime().maxMemory();

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = (int) (maxMemory / 8);

			cache = new LruCache<String, Bitmap>(cacheSize) {
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}

		public Bitmap get(String id) {
			return cache.get(id);
		}

		public void put(String id, Bitmap bitmap) {
			if (id != null && bitmap != null)
				cache.put(id, bitmap);
		}
	}

	private static class FileCache {
		private DiskLruImageCache cache;

		public FileCache(Context context) {
			cache = new DiskLruImageCache(context, "image_cache",
					50 * 1024 * 1024, CompressFormat.PNG, 100);
		}

		public Bitmap get(String id) {
			return cache.getBitmap(id);
		}

		public void put(String id, Bitmap bitmap) {
			if (id != null && bitmap != null)
				cache.put(id, bitmap);
		}
	}

	public MGImageCache(Context context) {
		memoryCache = new MemoryCache();

		fileCache = new FileCache(context);
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null) {
			return bitmap;
		}

		bitmap = fileCache.get(Integer.toHexString(url.hashCode()));

		if (bitmap != null) {
			memoryCache.put(url, bitmap);

			return bitmap;
		}

		return null;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		memoryCache.put(url, bitmap);

		fileCache.put(Integer.toHexString(url.hashCode()), bitmap);
	}
}
