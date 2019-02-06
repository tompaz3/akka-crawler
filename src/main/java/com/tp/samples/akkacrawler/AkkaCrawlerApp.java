package com.tp.samples.akkacrawler;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.tp.samples.akkacrawler.crawler.rm.RmCrawlParam;
import com.tp.samples.akkacrawler.crawler.rm.RmNewsActor;
import com.tp.samples.akkacrawler.crawler.rm.RmNewsDateParser;
import com.tp.samples.akkacrawler.crawler.rm.RmSiteNewsReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Akka Cralwer App Reads news using Akka Actors.
 */
public class AkkaCrawlerApp {

  /**
   * Reads and prints news read by Akka Actors.
   *
   * @param args unused.
   */
  public static void main(String[] args) {
    final ActorSystem system = ActorSystem.create("rmNewsReaderSystem");
    try {
      final RmSiteNewsReader newsReader = new RmSiteNewsReader();
      final RmNewsDateParser newsDateParser = new RmNewsDateParser();
      final ActorRef rmNewsActor = system
          .actorOf(RmNewsActor.props(newsReader, newsDateParser), "rmNewsActor");
      final FiniteDuration timeout = Duration.create(25, TimeUnit.SECONDS);
      @SuppressWarnings("unchecked") final Future<Object> futureResult = (Future<Object>) Await
          .result(ask(rmNewsActor, RmCrawlParam.builder().count(10).build(), 20_000L),
              timeout);
      System.out.println("AkkaCrawlerApp Retrieved futureResult: " + futureResult);

      @SuppressWarnings("unchecked") final List<News> result = (List<News>) Await
          .result(futureResult, timeout);
      System.out
          .println("AkkaCrawlerApp Retrieved result: \n\t" + result.stream().map(News::toString)
              .collect(Collectors.joining("\n\t")));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      system.terminate();
    }
  }
}

