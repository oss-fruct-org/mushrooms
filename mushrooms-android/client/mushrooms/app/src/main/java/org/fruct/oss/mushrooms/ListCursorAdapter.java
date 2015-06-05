package org.fruct.oss.mushrooms;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Closeable;
import java.lang.ref.SoftReference;

public class ListCursorAdapter extends CursorAdapter implements Closeable {
	private final LayoutInflater lInflater;
	private final boolean small;

	private final int titleColumnIndex;
	private final int imageColumnIndex;
	private final int idColumnIndex;

	private final DisplayMetrics metrics;

	private LruCache<Integer, Bitmap> bitmapMemoryCache
			= new BitmapCache((int) Runtime.getRuntime().maxMemory() / 8);

	public ListCursorAdapter(Context context, Cursor cursor, String nameColumn, boolean small, DisplayMetrics metrics) {
		super(context, cursor, false);
		this.metrics = metrics;

		this.titleColumnIndex = cursor.getColumnIndex(nameColumn);
		this.imageColumnIndex = cursor.getColumnIndex("image");
		this.idColumnIndex = cursor.getColumnIndex("_id");

		this.lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.small = small;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		try {
			View view = lInflater.inflate(R.layout.catalog_list_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.textView = ((TextView) view.findViewById(R.id.catalogLabel));
			holder.imageView = ((ImageView) view.findViewById(R.id.catalogImages));
			view.setTag(holder);
			return view;
		} catch (Exception ex) {
			Log.d("TAG", "ExceptionE: ", ex);
			throw ex;
		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Item item = getItem(cursor);
		ViewHolder holder = (ViewHolder) view.getTag();

		if (holder.imageTask != null) {
			holder.imageTask.cancel(false);
		}

		if (small) {
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams((metrics.widthPixels / 3) / 2, (metrics.widthPixels / 3) / 2));
			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (metrics.widthPixels / 10) / 2);
			holder.imageView.setPadding(metrics.widthPixels / 100, 0, metrics.widthPixels / 100, 0);
		} else {
			holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels / 3, metrics.widthPixels / 3));
			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.widthPixels / 15);
			holder.imageView.setPadding(metrics.widthPixels / 50, 0, metrics.widthPixels / 50, 0);
		}

		holder.textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
		holder.textView.setText(item.title);
		holder.item = item;

		if (item.imageByteArray != null) {
			requestImage(holder, item, cursor);
		} else {
			holder.imageView.setImageDrawable(null);
		}
	}

	private void requestImage(ViewHolder holder, Item item, Cursor cursor) {
		Bitmap bitmap = bitmapMemoryCache.get(item.id);
		if (bitmap != null) {
			Log.d("TAG", "Image found in cache");
			holder.imageView.setImageBitmap(bitmap);
		} else {
			Log.d("TAG", "Image not found in cache, decoding. Cache size = " + bitmapMemoryCache.size());

			holder.imageTask = new ImageTask(holder, cursor, item, cursor.getPosition());
			holder.imageTask.execute(item);
			holder.imageView.setImageDrawable(null);
		}
	}

	public Item getItem(Cursor cursor) {
		String title = cursor.getString(titleColumnIndex);
		byte[] imageBytes = cursor.getBlob(imageColumnIndex);
		int id = cursor.getInt(idColumnIndex);

		return new Item(title, imageBytes, id);
	}

	@Override
	public void close() {
		if (getCursor() != null) {
			getCursor().close();
		}
	}

	private static class Item {
		public Item(String title, byte[] imageByteArray, int id) {
			this.title = title;
			this.imageByteArray = imageByteArray;
			this.id = id;
		}

		String title;
		byte[] imageByteArray;
		int id;
	}

	private static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
		public ImageTask imageTask;
		public Item item;
	}

	private class ImageTask extends AsyncTask<Item, Void, Bitmap> {
		private final Cursor cursor;
		private final ViewHolder holder;
		private final Item item;
		private final int position;


		public ImageTask(ViewHolder holder, Cursor cursor,  Item item, int position) {
			this.holder = holder;
			this.item = item;
			this.cursor = cursor;
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (cursor.isClosed()) {
				cancel(false);
				return;
			}

			if (holder.item != item) {
				cancel(false);
			}
		}

		@Override
		protected Bitmap doInBackground(Item... params) {
			Item item = params[0];
			byte[] imageBytes = item.imageByteArray;

			if (imageBytes == null) {
				return null;
			}

			BitmapFactory.Options opt = new BitmapFactory.Options();

			// Get image dimensions
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);

			opt.inSampleSize = calculateInSampleSize(opt,
					metrics.widthPixels / 3, metrics.widthPixels / 3);
			opt.inJustDecodeBounds = false;

			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}*/
			return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap == null) {
				return;
			}

			// Image view can be:
			// - Out of list. Then setting bitmap for it is safe
			// - Bound to new Item. Then this code must never be called due to cancel() call in bindView
			bitmapMemoryCache.put(item.id, bitmap);
			holder.imageView.setImageBitmap(bitmap);
		}

		// Copypasted from https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
		public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;

				// Calculate the largest inSampleSize value that is a power of 2 and keeps both
				// height and width larger than the requested height and width.
				while ((halfHeight / inSampleSize) > reqHeight
						&& (halfWidth / inSampleSize) > reqWidth) {
					inSampleSize *= 2;
				}
			}

			return inSampleSize;
		}
	}

	private class BitmapCache extends LruCache<Integer, Bitmap> {
		public BitmapCache(int maxSize) {
			super(maxSize);
		}

		@Override
		protected int sizeOf(Integer key, Bitmap value) {
			if (Build.VERSION.SDK_INT >= 19) {
				return value.getAllocationByteCount();
			} else {
				return value.getByteCount();
			}
		}
	}
}
