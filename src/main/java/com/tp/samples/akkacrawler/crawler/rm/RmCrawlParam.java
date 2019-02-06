package com.tp.samples.akkacrawler.crawler.rm;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RmCrawlParam implements Serializable {

  private static final long serialVersionUID = 7220406544341680573L;
  private final int count;
}
