package com.example.will.rxjavademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.widget.Toast;

import com.example.will.rxjavademo.http.TestService;
import com.example.will.rxjavademo.pojo.BaseBean;
import com.example.will.rxjavademo.pojo.Goods;

import java.util.List;

import javax.security.auth.login.LoginException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String baseUrl = "http://beijing6.appdao.com:8182/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = initRetrofit();

        TestService service = retrofit.create(TestService.class);
        service.getIpInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean<List<Goods>>>() {
            public void onCompleted() {
                Log.e("---------", "onComplete");
            }

            public void onError(Throwable e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

            public void onNext(BaseBean<List<Goods>> listBaseBean) {
                Log.e("------listBaseBean size", listBaseBean.getData().size() + "");
                List<Goods> goods = listBaseBean.getData();

                String summary = goods.get(0).getSummary();

                Log.e("-------listBaseBean", summary);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(MainActivity.this, RetroalmbdaActivity.class);
        startActivity(intent);
    }

    private Retrofit initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit;
    }
}
