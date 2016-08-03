package com.example.will.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.will.rxjavademo.R;

public class RetroalmbdaActivity extends AppCompatActivity {

    //参考网址: http://blog.csdn.net/cai_iac/article/details/50846139
    //记得把jdk版本调整成java8

    Button bt_retroalmbda;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retroalmbda);

        bt_retroalmbda = (Button) findViewById(R.id.bt_retroalmbda);
        bt_retroalmbda.setOnClickListener(view -> Log.d("-------onClick", view.getId() + ""));

        //-------------->>>>>>>>>>>>>>>>测试input参数<<<<<<<<<<<<<<<<--------------------
        //method1: 没有参数
        test_method1(() -> Log.e("-----", "this is test_method1"));
        //method2: 单个参数
        test_method2(name -> Log.e("------name", name));

        //method3: 2个或多个参数
        test_method3((name, sex, parent_name) -> Log.e("--------", "-----name--" + name + "----sex--" + sex + "-----name--" + name));

        //method4: 不省略参数的类型
        test_method4((int age) -> Log.e("-------age", String.valueOf(age)));

        //-------------->>>>>>>>>>>>>>>>测试方法体<<<<<<<<<<<<<<<<--------------------

        //method5 方法体为空
        test_method3((String name, String sex, String parent_name) -> {
        });

        test_method3((String name, String sex, String parent_namee) -> Log.e("--------", "单行方法体,可以省略{}"));

        test_method3((name, sex, parent_name) -> {
            Log.e("-----", "多行方法体,并且没有返回值");
            Log.e("-----", "多行方法体,并且没有返回值");
        });

        test_method5(() -> {
            Log.e("-----", "多行方法体,有返回值");
            return 520;
        });
    }

    //测试的方法
    private void test_method1(CallBack1 callBack) {
        callBack.method1();
    }

    private void test_method2(CallBack2 callBack) {
        callBack.method2("weixinjie");
    }

    private void test_method3(CallBack3 callBack) {
        callBack.method3("zhangrui", "women", "weixinjie");
    }

    private void test_method4(CallBack4 callBack) {
        callBack.method4(10);
    }

    private void test_method5(CallBack5 callBack) {
        int age = callBack.get_age();
        Log.e("-------test method5中的打印", String.valueOf(age));
    }

    //测试的回调接口
    interface CallBack1 {
        //method1 没有参数
        void method1();
    }

    interface CallBack2 {
        void method2(String name);
    }

    interface CallBack3 {
        void method3(String name, String sex, String parent_name);
    }

    interface CallBack4 {
        void method4(int age);
    }

    interface CallBack5 {
        int get_age();
    }

}
