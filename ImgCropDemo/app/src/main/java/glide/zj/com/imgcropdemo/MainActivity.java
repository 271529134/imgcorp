package glide.zj.com.imgcropdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_photo_graph)
    Button mImgPhotoGraph;
    @BindView(R.id.img_choose_album)
    Button mImgChooseAlbum;
    @BindView(R.id.img_show)
    ImageView mImgShow;

    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.img_photo_graph, R.id.img_choose_album})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo_graph:
                takePhone();
                break;
            case R.id.img_choose_album:
                choosePhone();
                break;
        }
    }

    /**
     * 6.0 申请拍照权限
     */
    public void takePhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        } else {
            ImageUtils.takePicture(this);
        }
    }

    /**
     * 6.0 申请拍照读取sd卡权限
     */
    public void choosePhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
        } else {
            ImageUtils.choosePicture(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtils.REQUEST_TAKE_CODE && resultCode == RESULT_OK) {
            ImageUtils.cropImageUri(this, ImageUtils.imageUri, 100, 100);
            imgFile = new File(ImageUtils.IMAGE_FILE_LOCATION);
            Log.i("tag", "=============>" + imgFile.getPath());
        } else if (requestCode == ImageUtils.REQUEST_CHOOSE_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String imgPah = ImageUtils.getPath(this, uri);
            imgFile = new File(imgPah);
            Log.i("tag", "=============>" + imgFile.getPath());
            ImageUtils.cropImageUri(this, uri, 100, 100);
        } else if (requestCode == ImageUtils.REQUEST_CORP_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = ImageUtils.decodeUriAsBitmap(this);
            mImgShow.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageUtils.takePicture(this);
            } else {
                Toast.makeText(MainActivity.this, "未授权,请设置权限...", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageUtils.choosePicture(this);
            } else {
                Toast.makeText(MainActivity.this, "未授权,请设置权限...", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
