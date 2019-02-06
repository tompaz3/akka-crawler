package com.tp.samples.akkacrawler;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.tp.samples.akkacrawler.crawler.rm.RmCrawlParam;
import com.tp.samples.akkacrawler.crawler.rm.RmNewsActor;
import com.tp.samples.akkacrawler.crawler.rm.RmNewsDateParser;
import com.tp.samples.akkacrawler.crawler.rm.RmSiteNewsReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

  private static final int DEFAULT_NEWS_COUNT = 10;
  private static final long DEFAULT_TIMEOUT_SECONDS = 25L;

  /**
   * Reads and prints news read by Akka Actors.
   *
   * @param args cmd arguments - first argument supported only - count of latest news to be read
   * (integer).
   */
  public static void main(String[] args) {
    new AkkaCrawlerApp().run(args);
  }

  private void run(String[] args) {
    final ActorSystem system = ActorSystem.create("rmNewsReaderSystem");
    try {
      final RmSiteNewsReader newsReader = new RmSiteNewsReader();
      final RmNewsDateParser newsDateParser = new RmNewsDateParser();
      final ActorRef rmNewsActor = system
          .actorOf(RmNewsActor.props(newsReader, newsDateParser), "rmNewsActor");
      final FiniteDuration timeout = Duration.create(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      final int count = readCountArg(args).orElse(DEFAULT_NEWS_COUNT);
      @SuppressWarnings("unchecked") final Future<Object> futureResult = (Future<Object>) Await
          .result(ask(rmNewsActor, RmCrawlParam.builder().count(count).build(), timeout.toMillis()),
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

  private Optional<Integer> readCountArg(String[] args) {
    return Arrays.stream(args).limit(1L).map(Integer::valueOf).findAny();
  }
}

