package com.tp.samples.akkacrawler.crawler.rm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.select.Elements;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RmPage {

  private final Elements elements;
  private final int index;
}
