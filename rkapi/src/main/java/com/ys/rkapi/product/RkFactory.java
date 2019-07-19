package com.ys.rkapi.product;

import android.os.Build;

import com.ys.rkapi.Utils.VersionUtils;

/**
 * Created by Administrator on 2018/4/13.
 */

public class RkFactory {

    public static RK getRK() {
        String product = VersionUtils.getAndroidModle();
        if (product.contains("rk3328")) {
            return Rk3328.INSTANCE;
        }else if (product.contains("rk3399")) {
            return Rk3399.INSTANCE;
        }else if (product.contains("rk3368") && ("22".equals(Build.VERSION.SDK))) {
            return Rk3368_5.INSTANCE;
        } else if (product.contains("rk3288") && "22".equals(Build.VERSION.SDK)) {
            return Rk3288_5.INSTANCE;
        } else if (product.contains("rk3128") && "19".equals(Build.VERSION.SDK)) {
            return Rk3128.INSTANCE;
        } else if (product.contains("rk3288") && "25".equals(Build.VERSION.SDK))
            return Rk3288_7.INSTANCE;
        else if (product.contains("rk3368") && "25".equals(Build.VERSION.SDK))
            return Rk3368_7.INSTANCE;
        else if (product.contains("rk3128") && "25".equals(Build.VERSION.SDK))
            return Rk3128_7.INSTANCE;
        return Rk3368_5.INSTANCE;
    }
}
