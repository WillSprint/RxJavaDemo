package com.example.will.rxjavademo.http;


import com.example.will.rxjavademo.pojo.BaseBean;
import com.example.will.rxjavademo.pojo.Goods;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by will on 16/8/2.
 */

public interface TestService {

    @GET("1/good/goods?sort=new&page=1")
    Observable<BaseBean<List<Goods>>> getIpInfo();
}
