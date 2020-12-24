package com.jingkai.asset.utils.selectpicture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.jingkai.asset.utils.SpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageSelectUtils {
    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTOZOOM = 2; // 缩放
    private static final int PHOTORESOULT = 3;// 结果
    private static final int ROUNDRESOULT = 4;// 圆形
    public static final int REQUEST_IMAGE = 5;// 打开ImageSelect
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String IMG_PATH_KEY = "IMG_PATH_KEY";

    private static String path = "";
    private Activity activity;
    private boolean isRound = false;
    private Intent intentUtils;
    private OnImageSelectCallback imageSelectCallback;

    public ImageSelectUtils(Activity activity) {
        this(activity, false, null);
    }

    public ImageSelectUtils(Activity activity, Intent intentUtils) {
        this(activity, false, intentUtils);
    }

    public ImageSelectUtils(Activity activity, boolean isRound, Intent intentUtils) {
        super();
        this.activity = activity;
        this.isRound = isRound;
        this.intentUtils = intentUtils;
        // intent.putExtra("aspectX", 1);
        // intent.putExtra("aspectY", 1);
        // // outputX outputY 是裁剪图片宽高
        // intent.putExtra("outputX", 150);
        // intent.putExtra("outputY", 150);
    }

    public static void setPath(String path) {
        ImageSelectUtils.path = path + "ico" + File.separator;
        File file = new File(ImageSelectUtils.path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void trimPath() {
        if (TextUtils.isEmpty(path)) {
            setPath(SpUtil.getShared("rootPath"));
        }
        SpUtil.setShared(IMG_PATH_KEY, path + System.currentTimeMillis() + ".png");

    }

    private String getPath() {
        return SpUtil.getShared(IMG_PATH_KEY);
    }

    public void setImageSelectCallBack(OnImageSelectCallback imageSelectCallback) {
        this.imageSelectCallback = imageSelectCallback;
    }

    /**
     * 回调接口
     * 1,通过自定义相册或者相机回调的图片
     * 2,通过系统打开相机或者打开相册或者去处理选择回来的图片
     */
    public interface OnImageSelectCallback {
        /**
         * 1,通过自定义相册或者相机回调的图片
         *
         * @param paths
         */
        void onImageSelected(ArrayList<String> paths);

        /**
         * 2,通过系统打开相机或者打开相册或者去处理选择回来的图片
         *
         * @param imageFilePath
         */
        void onSystemImageSelect(String imageFilePath);
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        trimPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPath())));
        activity.startActivityForResult(intent, PHOTOHRAPH);
    }

    /**
     * 打开本地文件
     */
    public void openPhotoAlbum() {
        // 在Android4.4上 以下方法失效
        // Intent intent = new Intent();
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType(IMAGE_UNSPECIFIED);
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        // activity.startActivityForResult(intent, PHOTOZOOM);

        // 在Android4.4上有效的方法应
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PHOTOZOOM);
    }

    /**
     * 打开相册,选择图片或者拍照
     * {@link }
     */
    public void openImageSelect(int selectMode, boolean isShowCamera) {
        openImageSelect(selectMode, 1, isShowCamera);
    }

    public void openImageSelect(int selectMode, int maxSelectCount, boolean isShowCamera) {
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectCount);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectMode);
        // 跳转
        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * fragment专用选择图片
     * @param fragment
     * @param selectMode
     * @param maxSelectCount
     */
    public void openFragmentImageSelect(Fragment fragment,int selectMode, int maxSelectCount) {
        Intent intent = new Intent(fragment.getContext(), MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectCount);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectMode);
        // 跳转
        fragment.startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE) {
            return;
        }

        // 调用系统相机拍照
        if (requestCode == PHOTOHRAPH) {
            String imgPath = autoRotation(getPath());
            if (intentUtils == null) {
                if (imageSelectCallback != null) {
                    imageSelectCallback.onSystemImageSelect(imgPath);
                }
            } else {
                // 设置文件保存路径这里放在跟目录下
                startPhotoZoom(Uri.parse("file:" + File.separator + File.separator + File.separator + imgPath));
            }
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            if (imageSelectCallback != null) {
                imageSelectCallback.onSystemImageSelect(getPath());
            }
        }

        // 处理圆结果
        if (requestCode == ROUNDRESOULT) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Config.RGB_565;
            Bitmap photo = toRoundCorner(BitmapFactory.decodeFile(getPath(), opts));
            SaveBitmap(photo);
            if (imageSelectCallback != null) {
                imageSelectCallback.onSystemImageSelect(getPath());

            }
        }

        if (data == null) {
            return;
        }

        // 打开ImageSelect
        if (requestCode == REQUEST_IMAGE) {
            ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

            if (mSelectPath != null && mSelectPath.size() > 0) {

//                String imgPath = autoRotation(mSelectPath.get(0));
                if (intentUtils == null) {
                    if (imageSelectCallback != null) {
                        imageSelectCallback.onImageSelected(mSelectPath);
                    }
                } else {
                    // 设置文件保存路径这里放在跟目录下
//                    startPhotoZoom(Uri.parse("file:" + File.separator + File.separator + File.separator + imgPath));
                }
            }
        }
        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            String requestPath = selectImage(activity, data);
            String imgPath = autoRotation(requestPath);
            if (intentUtils == null) {
                if (imageSelectCallback != null) {
                    imageSelectCallback.onSystemImageSelect(imgPath);

                }
            } else {
                // 最后根据索引值获取图片路径
                startPhotoZoom(Uri.parse("file:" + File.separator + File.separator + File.separator + imgPath));
            }
        }
    }

    private Bitmap toRoundCorner(Bitmap bitmap) {
        int pixels = bitmap.getWidth() / 2;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();

        return output;
    }

    /**
     * 设置缩放
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(activity, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), IMAGE_UNSPECIFIED);
        } else {
            intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        }

        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", intentUtils.getIntExtra("aspectX", 1));
        intent.putExtra("aspectY", intentUtils.getIntExtra("aspectY", 1));
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", intentUtils.getIntExtra("outputX", 150));
        intent.putExtra("outputY", intentUtils.getIntExtra("outputY", 150));
        intent.putExtra("scale", true);// 黑边
        intent.putExtra("scaleUpIfNeeded", true);// 黑边

        trimPath();

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPath())));
        activity.startActivityForResult(intent, isRound ? ROUNDRESOULT : PHOTORESOULT);
    }

    private static final String TAG = "ImageSelectUtils";

    /**
     * 保存头像
     *
     * @param bitmap
     * @throws IOException
     */
    private void SaveBitmap(Bitmap bitmap) {
        trimPath();
        String path = getPath();

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            // 将Bitmap对象写入本地路径中，Unity在去相同的路径来读取这个文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception r) {

        }

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String selectImage(Context context, Intent data) {
        Uri selectedImage = data.getData();

        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {

                return null;
            }
        }

        // String[] proj = { MediaStore.Images.Media.DATA };
        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        // Cursor cursor = activity.getContentResolver().query(data.getData(), proj, null, null, null);
        // 按我个人理解 这个是获得用户选择的图片的索引值
        // int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        // cursor.moveToFirst();
        // String requestPath = cursor.getString(column_index);

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    // 以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String autoRotation(String rotPath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(rotPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int angle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                default:
                    angle = 0;
                    break;
            }
            if (angle != 0) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(rotPath, opts);

                opts.inJustDecodeBounds = false;
                opts.inPreferredConfig = Config.RGB_565;
                opts.inSampleSize = Math.max(opts.outWidth / 600, 1);

                Bitmap photo = BitmapFactory.decodeFile(rotPath, opts);

                Matrix matrix = new Matrix();
                matrix.postRotate(angle);

                Bitmap result = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);

                photo.recycle();


//                photo = null;
                SaveBitmap(result);
                return getPath();
            }
        }
        return rotPath;
    }
}
