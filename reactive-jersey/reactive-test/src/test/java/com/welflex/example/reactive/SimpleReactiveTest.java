package com.welflex.example.reactive;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SimpleReactiveTest {
  public static void main(String args[]) throws InterruptedException {
 

    Observable.concat(integerObservable(1), integerObservable(2), integerObservable(3), integerObservable(4)).observeOn(Schedulers.io())
   .flatMap(new Func1<Integer, Observable<Integer>>() {

      @Override
      public Observable<Integer> call(Integer t1) {
        System.out.println("Thread:" + Thread.currentThread());
        return Observable.just(t1);
      }
      
    }).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Integer>() {

        @Override
        public void onCompleted() {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void onError(Throwable e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void onNext(Integer t) {
          System.out.println("Got:" + t  + " on thread :" + Thread.currentThread());
        }});
   
    
    
     Thread.sleep(60);
  }
  
  private static Observable<Integer> integerObservable(final int i) {
    return Observable.create(new Observable.OnSubscribe<Integer>() {
 

      @Override
      public void call(Subscriber<? super Integer> t1) {
        System.out.println("Did the work on:" + Thread.currentThread());
        t1.onNext(i);
        t1.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }
}
