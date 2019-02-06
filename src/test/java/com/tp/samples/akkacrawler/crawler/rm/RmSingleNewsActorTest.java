package com.tp.samples.akkacrawler.crawler.rm;

import static akka.pattern.Patterns.ask;
import static org.assertj.core.api.Assertions.assertThat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.tp.samples.akkacrawler.News;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scala.Some;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

class RmSingleNewsActorTest {

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
  void testSingleNewsActor() throws Throwable {
    final ActorRef singleNewsActor = system
        .actorOf(RmSingleNewsActor.props(newsReader, dateParser));
    final News result = (News) ((Some) Await
        .result(ask(singleNewsActor, RmSingleNewsParam.builder().index(0).build(), 1_000L),
            timeout)).getOrElse(() -> News.builder().build());

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isNotEmpty();
  }

}