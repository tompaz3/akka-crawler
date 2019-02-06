package com.tp.samples.akkacrawler.crawler.rm;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class RmSingleNewsParam implements Serializable {

  private static final long serialVersionUID = 8406617396925993213L;
  private final int index;
}
