package com.example.yface;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast toast;

    /** 
     * 解决Toast重复弹出 长时间不消失的问题 
     * @param context 
     * @param message 
     */  
    public static void showToast(Context context, String message){
        if (toast==null){  
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);


        }else {  
            toast.setText(message);  
        }  
        toast.show();//设置新的消息提示  
     }


} 