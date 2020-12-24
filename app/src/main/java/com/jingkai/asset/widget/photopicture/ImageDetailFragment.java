package com.jingkai.asset.widget.photopicture;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jingkai.asset.R;
import com.jingkai.asset.utils.LoadingDialogUtils;
import com.jingkai.asset.utils.ToastUtil;
import com.jingkai.asset.widget.photopicture.imageview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private static final String TAG = "ImageDetailFragment";
    private String mImageUrl;
    PhotoView imageView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        imageView = v.findViewById(R.id.subsamplingScaleImageView);

        LoadingDialogUtils.getUtils().showProgressDialog(getActivity());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.mipmap.image_default_holder)
                .error(R.mipmap.image_default_holder);
        Glide.with(getActivity()).load(mImageUrl).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                LoadingDialogUtils.getUtils().dismissDialog();
                ToastUtil.toastShortCenter("图片加载失败");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                LoadingDialogUtils.getUtils().dismissDialog();
                return false;
            }
        }).into(imageView);


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//				保存图片
                if (mImageUrl.startsWith("http://") || mImageUrl.startsWith("https://"))
                    savePic(mImageUrl);
                return true;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//		mImageView = (ImageView) v.findViewById(R.id.image);
//		mAttacher = new PhotoViewAttacher(mImageView);
        //长按保存图片
//		mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				//保存图片
//				if (mImageUrl.startsWith("http://")||mImageUrl.startsWith("https://"))
//					savePic(mImageUrl);
//				return true;
//			}
//		});
//		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//
//			@Override
//			public void onPhotoTap(View arg0, float arg1, float arg2) {
//				getActivity().finish();
//			}
//		});

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoadingDialogUtils.getUtils().dismissDialog();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//		ImageLoader.getInstance().displayImage(mImageUrl,mImageView,new SimpleImageLoadingListener(){
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setVisibility(View.VISIBLE);
//				iv_logo_wind.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				String message = null;
//				switch (failReason.getType()) {
//					case IO_ERROR:
//						message = "下载错误";
//						break;
//					case DECODING_ERROR:
//						message = "图片无法显示";
//						break;
//					case NETWORK_DENIED:
//						message = "网络有问题，无法下载";
//						break;
//					case OUT_OF_MEMORY:
//						message = "图片太大无法显示";
//						break;
//					case UNKNOWN:
//						message = "未知的错误";
//						break;
//				}
//				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//				progressBar.setVisibility(View.GONE);
//				iv_logo_wind.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				progressBar.setVisibility(View.GONE);
//				iv_logo_wind.setVisibility(View.GONE);
//				mAttacher.update();
//				ExifInterface exif = null;
//				try {
//					exif = new ExifInterface(imageUri);
//					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
//					UserUtil.Log("==》", imageUri+"，方向，"+orientation);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
    }

    /**
     * 保存图片到相册
     *
     * @param imageUrl
     */
    private void savePic(final String imageUrl) {

        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Light_Dialog_Theme_Light_Dialog_Cus);
        View view = View.inflate(getActivity(), R.layout.dialog_save_pic, null);
        view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定
                final Bitmap[] bitmap_toast = {null};

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bitmap_toast[0] = getNetBitmap(imageUrl);
                        saveBitmap(getActivity(), bitmap_toast[0]);
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = "图片保存成功！";
                        handler.sendMessage(message);
                    }
                }).start();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);

//        dialog.setCancelable(false);

        dialog.show();
    }


    //2017-2-13 根据网络图片url转换为bitmap
    private Bitmap getNetBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = android.graphics.BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void saveBitmap(Context context, Bitmap bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "ToastImage");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "ToastImage" + System.currentTimeMillis() + ".jpg";
        File f = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通知图库更新  
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

}
