package com.tp.samples.akkacrawler.crawler.rm;

import static akka.pattern.Patterns.ask;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;
import com.tp.samples.akkacrawler.News;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import scala.Option;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RmNewsActor extends AbstractActor {

  public static Props props(RmSiteNewsReader rmSiteNewsReader, RmNewsDateParser rmNewsDateParser) {
    return Props
        .create(RmNewsActor.class, () -> new RmNewsActor(rmSiteNewsReader, rmNewsDateParser));
  }

  private final RmSiteNewsReader rmSiteNewsReader;
  private final RmNewsDateParser rmNewsDateParser;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(RmCrawlParam.class, p -> p.getCount() > 0, this::readNews)
        .build();
  }

  private void readNews(RmCrawlParam param) {
    try {
      final Timeout timeout = new Timeout(Duration.create(10L, TimeUnit.SECONDS));
      final List<Future<Object>> futures = new ArrayList<>();

      for (int i = 0; i < param.getCount(); i++) {
        ActorRef childActor = getContext()
            .actorOf(RmSingleNewsActor.props(rmSiteNewsReader, rmNewsDateParser),
                "rmSingleNewsActor" + i);
        futures
            .add(ask(childActor, RmSingleNewsParam.builder().index(i).build(), timeout));
      }

      final Future<List<News>> transformed = Futures.sequence(futures, getContext().dispatcher())
          .map(
              new Mapper<>() {
                @Override
                public List<News> apply(Iterable<Object> parameter) {
                  return StreamSupport.stream(parameter.spliterator(), false)
                      .map(Option.class::cast)
                      .filter(Option::nonEmpty)
                      .map(Option::get)
                      .map(News.class::cast)
                      .collect(Collectors.toList());
                }
              }, getContext().dispatcher()
          );

      getSender().tell(transformed, getSelf());
    } catch (Exception e) {
      getSender().tell(new Status.Failure(e), getSelf());
      throw e;
    }
  }
}
