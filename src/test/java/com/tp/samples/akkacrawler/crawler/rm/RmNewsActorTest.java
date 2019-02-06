package com.tp.samples.akkacrawler.crawler.rm;

import static akka.pattern.Patterns.ask;
import static org.assertj.core.api.Assertions.assertThat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.tp.samples.akkacrawler.News;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

class RmNewsActorTest {

  private static ActorSystem system;
  private static RmSiteNewsReader newsReader;
  private static RmNewsDateParser dateParser;
  private static FiniteDuration timeout;


  @BeforeAll
  static void setup() {
    system = ActorSystem.create("rmSingleNewsActorTestSystem");
    newsReader = new RmSiteNewsReader();
    dateParser = new RmNewsDateParser();
    timeout = Duration.create(10L, TimeUnit.SECONDS);
  }

  @AfterAll
  static void teardown() {
    TestKit.shutdownActorSystem(system, timeout, true);
    system = null;
  }

  @Test
  void testNewsActor() throws Throwable {
    final ActorRef newsActor = system.actorOf(RmNewsActor.props(newsReader, dateParser));
    @SuppressWarnings("unchecked") final Future<Object> futureResult = (Future<Object>) Await
        .result(ask(newsActor, RmCrawlParam.builder().count(2).build(), timeout.toMillis()),
            timeout);
    @SuppressWarnings("unchecked") final List<News> result = (List<News>) Await
        .result(futureResult, timeout);

    assertThat(result).isNotEmpty();
  }
}