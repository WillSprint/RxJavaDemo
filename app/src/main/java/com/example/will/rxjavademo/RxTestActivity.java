package com.example.will.rxjavademo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.will.rxjavademo.pojo.Course;
import com.example.will.rxjavademo.pojo.Student;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxTestActivity extends AppCompatActivity {

    ImageView iv_test;
    ImageView iv_test2;

    Button bt_test;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test);


        iv_test = (ImageView) findViewById(R.id.iv_test);
        iv_test2 = (ImageView) findViewById(R.id.iv_test2);
        bt_test = (Button) findViewById(R.id.bt_test);

        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RxTestActivity.this, "test", Toast.LENGTH_LONG).show();
            }
        });

        //method 1 创建一个observer(观察者)
        final String tag1 = "----observer";
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(tag1, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag1, "onError--------->>>>>>>>" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(tag1, "onNext-------->>>>>>>>" + s);
            }
        };

        //method2:Subscriber是对Observer的一个扩展,跟method1的用法是一样的
        final String tag2 = "------subscriber";
        Subscriber<String> subscriber = new Subscriber<String>() {
            //是Subscriber的一个扩展方法,可以做一些初始化的操作,比如数据的清零,
            // 但是不适用于对线程有要求的操作,因为改方法的作用函数是当前线程,不一定是ui线程
            public void onStart() {
                super.onStart();
                Log.e(tag2, "Onstart");
            }

            @Override
            public void onCompleted() {
                Log.e(tag2, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag2, "onError------->>>>>>>>" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(tag2, "onNext-------->>>>>>>>" + s);
            }
        };
        //Subscriber特有的方法用于取消订阅,防止内存泄漏
        // subscriber.unsubscribe();

        //method3创建一个Observable:被观察者 create方法是创建事件队列最基本的方法
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onNext("weixinjie");
                subscriber.onNext("恭喜你学会了rxjava");
                subscriber.onCompleted();
            }
        });

        //method4创建一个Observable:被观察者 通过just将参数一个一个的发送出来
        Observable observable1 = Observable.just("hello", "weixinjie", "这是通过just方法来输出数据");

        //method5创建一个Observable:被观察者 通过from方法将传入的数组拆分成具体的对象依次发送出来
        String[] test_strings = new String[]{"hello", "weixinjie", "这是通过from方法来输出数据"};
        Observable observable2 = Observable.from(test_strings);

        //method6将被观察者与观察者联系起来(这里不大符合思维习惯,订阅的时候是被观察者订阅观察者)
        observable.subscribe(observer);
        observable.subscribe(subscriber);

        observable1.subscribe(observer);
        observable1.subscribe(subscriber);

        observable2.subscribe(observer);
        observable2.subscribe(subscriber);

        //method6 Rxjava支持不完整定义的回调,Action0 Action1都是rxjava的一个接口,
        // Action0没有输入参数,所以用来模拟oncomplete;Action1有一个输入参数,所以用来模拟onNext与onError
        final String tag3 = "------不完整定义的回调";
        Action0 onCompleteAction = new Action0() {
            public void call() {
                Log.e(tag3, "onCompleteAction");
            }
        };

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(tag3, "OnNextAction");
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(tag3, "onErrorAction");
            }
        };

        observable2.subscribe(onNextAction);
        observable2.subscribe(onNextAction, onErrorAction);
        observable2.subscribe(onNextAction, onErrorAction, onCompleteAction);

        //method7 开始使用Scheduler来调度线程
        final String tag4 = "-------指定Scheduler来指定运行的线程";
        Observable.just(1, 2, 3, 4, 5, 6, 6).
                subscribeOn(Schedulers.io()). //操作发生在io线程
                observeOn(AndroidSchedulers.mainThread()). //指定Subscriber的回调发生在ui线程
                subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                //可以操作一下主线程的东西
                Log.e(tag4, integer.toString());
            }
        });

        //method8一个简单的小例子 通过scheduler来切换线程来加载图片
        final int drawable_id = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawable_id);
                subscriber.onNext(drawable);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Drawable>() {
            public void call(Drawable drawable) {
                iv_test.setImageDrawable(drawable);
            }
        });

        //开始学习牛逼的地方(变换)

        //method9:使用map进行变换,这里出现了一个交Func1的函数,这个东西也是Rxjava里面的一个接口,
        // 类似于Action1,与Action1不同的是Func1是有返回值的(这里的例子将int类型的输入参数转换成了Drawable类型的输出参数)
        //需要注意的是map是一对一的转化
        Observable.just(R.mipmap.ic_launcher).map(new Func1<Integer, Drawable>() {
            public Drawable call(Integer integer) {
                Drawable drawable = getResources().getDrawable(integer);
                return drawable;
            }
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Drawable>() {
                    public void call(Drawable drawable) {
                        iv_test2.setImageDrawable(drawable);
                    }
                });

        //method10 进行一对多的转化,事实证明使用from方法的时候使用数组跟list都可以的
        final String tag5 = "---->>>>method10";
//        Student[] students = new Student[3];
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Student student = new Student();
            student.setName("student" + i);

            Course course = new Course();
            course.setName("英文" + i);
            Course course1 = new Course();
            course1.setName("数学" + i);
            Course[] courses = new Course[2];
            courses[0] = course;
            courses[1] = course1;

            student.setCourses(courses);
//            students[i] = student;
            students.add(student);
        }
        Observable.from(students).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.getCourses());
            }
        }).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {
                Log.e(tag5, "onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag5, e.getMessage());
            }

            @Override
            public void onNext(Course course) {
                Log.e(tag5, course.getName()); //实现遍历
            }
        });

        //method11 使用lift对Observable进行转换'
        final String tag6 = "-------lift进行变换";

        final Observable.Operator lift1 = new Observable.Operator<String, Integer>() {
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    public void onNext(Integer integer) {
                        subscriber.onNext(integer + "");
                    }
                };
            }
        };
        Observable.just(1111, 2222, 3333).lift(lift1).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                Log.e(tag6, o);
            }
        });

        //method 12:使用compose对Observable的自身进行变换,可以对一组Observable进行相同的lift变换

        final String tag7 = "--------使用compose";
        final Observable.Operator lift2 = new Observable.Operator<Integer, String>() {

            public Subscriber<? super String> call(final Subscriber<? super Integer> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        subscriber.onNext(Integer.parseInt(s));
                    }
                };
            }
        };

        class MyLiftClass implements Observable.Transformer<Integer, String> {

            @Override
            public Observable<String> call(Observable<Integer> integerObservable) {
                return integerObservable.lift(lift1).lift(lift2);
            }
        }
        Observable.Transformer transformer = new MyLiftClass();

        Observable.just(111, 222, 333).compose(transformer).subscribe(new Subscriber<Integer>() {
            public void onCompleted() {
                Log.e(tag7, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag7, "onError" + e.getMessage());
            }

            @Override
            public void onNext(Integer o) {
                Log.e(tag7, "onNext" + o + "------>>>>>>" + (o + 1));
            }
        });


        //method 13:使用schedule进行多次切换线程--->>>>多次切换线程的需求只需要在每次想切换的地方加上
        // observeOn就行了(回忆一下 subscribeOn是指的生产线程,observeOn代表的是消费线程)不过subscribeOn这句话放到哪里都可以但是只能调用一次
        final String tag8 = "-------多次切换线程";
        Observable.just("韦新杰", "love", "张睿").
                subscribeOn(Schedulers.newThread()).
                map(new Func1<String, String>() {
                    public String call(String s) {
                        return s + "2333";
                    }
                }).observeOn(AndroidSchedulers.mainThread()).
                map(new Func1<String, String>() {
                    public String call(String s) {
                        return s.replace("2333", "12333");
                    }
                }).observeOn(Schedulers.io()).
                map(new Func1<String, String>() {
                    public String call(String s) {
                        return s.replace("12333", "521");
                    }
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<String>() {
                    @Override
                    public void onStart() { //这里不能指定线程,只可以做一些数据的清理等操作
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        Log.e(tag8, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag8, "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(RxTestActivity.this, s, Toast.LENGTH_SHORT).show();
                        Log.e(tag8, "onNext" + s);
                    }
                });

        //method14:使用doOnSubscribe来进行初始化的操作,由于Subsscriber中存在
        // onStart的方法中不可以指定运行的线程,所以可以使用Observable的doOnSubscribe来进行初始化(可以指定线程)的操作
        //WARNING:使用doOnSubscribe之后如果有subscribeOn的话就会挑最近的一个来确定donOnSubscribe的线程

        final String tag9 = "-------使用doOnSubscribe";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("韦新杰");
                subscriber.onNext("love");
                subscriber.onNext("张睿");
            }
        }).subscribeOn(Schedulers.newThread()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Toast.makeText(RxTestActivity.this, "doOnSubscribe", Toast.LENGTH_SHORT).show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(tag9, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(tag9, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(tag9, s);
                Toast.makeText(RxTestActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
