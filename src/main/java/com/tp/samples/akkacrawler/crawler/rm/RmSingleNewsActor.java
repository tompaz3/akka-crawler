package com.tp.samples.akkacrawler.crawler.rm;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import com.tp.samples.akkacrawler.News;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.Option;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RmSingleNewsActor extends AbstractActor {

  static Props props(RmSiteNewsReader rmSiteNewsReader, RmNewsDateParser rmNewsDateParser) {
    return Props
        .create(RmSingleNewsActor.class,
            () -> new RmSingleNewsActor(rmSiteNewsReader, rmNewsDateParser));
  }

  private final RmSiteNewsReader rmSiteNewsReader;
  private final RmNewsDateParser rmNewsDateParser;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(RmSingleNewsParam.class, this::readSingleNews)
        .build();
  }

  @SneakyThrows
  private void readSingleNews(RmSingleNewsParam param) {
    try {
      Option<RmPage> page = rmSiteNewsReader.findPage(param.getIndex(), 10_000L);
      if (page.isEmpty()) {
        getSender().tell(Option.empty(), getSelf());
      }
      getSender().tell(Option.apply(createNews(page.get())), getSelf());
    } catch (Exception e) {
      getSender().tell(new Status.Failure(e), getSelf());
      throw e;
    }
  }

  private News createNews(RmPage page) {
    return News.builder()
        .published(published(page))
        .permalink(permalink(page))
        .title(title(page))
        .content(content(page))
        .build();
  }

  private LocalDateTime published(RmPage page) {
    return rmNewsDateParser.parse(page.getElements()
        .select(".newsinfo")
        .select("span")
        .get(page.getIndex())
        .text()
    );
  }

  private String permalink(RmPage page) {
    return rmSiteNewsReader.getSiteUrl() + page.getElements()
        .select(".comments")
        .select("a")
        .get(page.getIndex())
        .attr("href");
  }

  private String title(RmPage page) {
    return page.getElements().select("h2").get(page.getIndex()).text();
  }

  private String content(RmPage page) {
    Element newsInfo = page.getElements()
        .select(".newsinfo").select("span").get(page.getIndex());
    StringBuilder sb = new StringBuilder();
    readContent(newsInfo, sb);
    return sb.toString();
  }

  private void readContent(Element current, StringBuilder sb) {
    sb.append(current.html());
    Elements comments = current.select(".comments");
    Element next = current.nextElementSibling();
    if (next != null && (comments == null || comments.isEmpty())) {
      readContent(next, sb);
    }
  }
}
