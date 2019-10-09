package com.ys.rkapi.product;

import android.os.Build;

import com.ys.rkapi.Utils.VersionUtils;

/**
 * Created by Administrator on 2018/4/13.
 */

public class YsFactory {

    public static YS getRK() {
        String product = VersionUtils.getAndroidModle();
        if (product.contains("rk3328")) {
            return YS3328.INSTANCE;
        }else if (product.contains("rk3399")) {
            return YS3399.INSTANCE;
        }else if (product.contains("rk3368") && ("22".equals(Build.VERSION.SDK))) {
            return YS3368_5.INSTANCE;
        } else if (product.contains("rk3288") && "22".equals(Build.VERSION.SDK)) {
            return YS3288_5.INSTANCE;
        } else if (product.contains("rk3128") && "19".equals(Build.VERSION.SDK)) {
            return YS3128.INSTANCE;
        } else if (product.contains("rk3288") && "25".equals(Build.VERSION.SDK))
            return YS3288_7.INSTANCE;
        else if (product.contains("rk3368") && "25".equals(Build.VERSION.SDK))
            return YS3368_7.INSTANCE;
        else if (product.contains("rk3128") && "25".equals(Build.VERSION.SDK))
            return YS3128_7.INSTANCE;
        else if (product.contains("msm895") && "27".equals(Build.VERSION.SDK))
            return GT8953_8.INSTANCE;
        return YS3368_5.INSTANCE;
    }
}
