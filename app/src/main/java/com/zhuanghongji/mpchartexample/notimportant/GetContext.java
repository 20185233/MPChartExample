package com.zhuanghongji.mpchartexample.notimportant;

import android.content.Context;
import android.widget.Toast;

public class GetContext {

    public static Context toastNews(){

        Context context = MyApplication.getContext();
        Toast.makeText(context, "hello world", Toast.LENGTH_SHORT).show();
        return context;

    }
}