package com.ldh.androidlib.base;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.ldh.androidlib.view.ProgressDialogFragment;

/**
 * desc:
 * Created by ldh on 2018/7/2.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialogFragment progressDialog;


    /**
     * 展示progressDialog
     */
    public void showProgressDialog(@NonNull String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialogFragment();
            progressDialog.setMessage(message);
        }
        progressDialog.show(this);
    }

    /**
     * 取消progressDialog
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && !isDestroyedOrFinishing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * activity是否被销毁
     */
    public boolean isDestroyedOrFinishing() {
        return getSupportFragmentManager().isDestroyed() || isFinishing();
    }

    /**
     * gotoPersonCenter
     * gotoMovieDetail:movieId = 100
     * gotoNewList:cityId=1&cityName = 上海
     * gotoUrl:http://www.sina.com
     */
    public void gotoAnyWhere(String url) {
        if (url != null) {
            if (url.startsWith("gotoMovieDetail:")) {
                String strMovieId = url.substring(24);
                int movieId = Integer.valueOf(strMovieId);

            }
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
