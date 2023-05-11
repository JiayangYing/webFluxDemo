package com.example.webfluxdemo03;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

public class ReactorDemo {
    public static void main(String[] args) {
        //reactor: jdk8 stream + jdk9 reactive stream
        //Mono 0-1 element
        // Flux 0-N element

        String[] strings = {"1","2","3"};

          Subscriber<Integer> subscriber = new Subscriber<Integer>() {
          private Subscription subscription;
          @Override
          public void onSubscribe(Subscription subscription) {
              this.subscription = subscription;

              //request data
              this.subscription.request(1);


          }

          @Override
          public void onNext(Integer integer) {
              System.out.println("received data: "+ integer );
              try {
                  TimeUnit.SECONDS.sleep(3);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
              this.subscription.request(1);

          }

          @Override
          public void onError(Throwable t) {

              t.printStackTrace();

              // we tell publishers that we can't accept more data
              this.subscription.cancel();

          }

          @Override
          public void onComplete() {
              //finish completing data after publisher closes
              System.out.println("completed!");
          }
      };

        //stream in jdk8
        Flux.fromArray(strings).map(s->Integer.parseInt(s)) // it's stream and publisher
        //reactive stream in jdk9
        .subscribe(subscriber);




    }
}
