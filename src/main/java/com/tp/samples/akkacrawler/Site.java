package com.tp.samples.akkacrawler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Site {
  RM_PL("http://www.realmadryt.pl/");
  @Getter
  private final String url;
}
