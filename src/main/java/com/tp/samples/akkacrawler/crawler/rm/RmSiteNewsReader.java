package com.tp.samples.akkacrawler.crawler.rm;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import scala.Option;

public class RmSiteNewsReader {

  private static final String SITE_NEWS_URL =
      "http://www.realmadryt.pl/index.php?co=aktualnosci&strona=%d";
  private static final String SITE_URL = "http://www.realmadryt.pl/";

  /**
   * Returns site's URL.
   *
   * @return site's URL.
   */
  String getSiteUrl() {
    return SITE_URL;
  }

  /**
   * Finds news' page of realmadryt.pl for the given news' absolute index. If search for the given
   * index is greater than given timeout, search is stopped and empty {@link Option} is returned.
   *
   * @param index news' index.
   * @param timeoutMillis timeout in millis.
   * @return news' page containing page found for the given absolute index with relative index
   * (index of the news on the returned page).
   */
  @SneakyThrows
  Option<RmPage> findPage(int index, long timeoutMillis) {
    if (index < 0) {
      return Option.empty();
    }
    return findPage(index, 1, timeoutMillis, System.currentTimeMillis());
  }

  private Option<RmPage> findPage(final int index, final int page,
      final long timeoutMillis, final long startTime) {
    if (System.currentTimeMillis() - startTime >= timeoutMillis) {
      return Option.empty();
    }
    Document document = readPage(page);
    Elements newsElements = document.select("#wrapper").select("#col-right")
        .select("#sub-left");
    int size = newsElements.select(".newsinfo").size();
    if (size < index + 1) {
      return findPage(index - size, page + 1, timeoutMillis, startTime);
    } else {
      return Option.apply(RmPage.builder()
          .elements(newsElements)
          .index(index)
          .build());
    }
  }

  @SneakyThrows
  private Document readPage(int page) {
    return Jsoup.connect(String.format(SITE_NEWS_URL, page)).get();
  }
}
