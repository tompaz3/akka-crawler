package com.tp.samples.akkacrawler;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "content")
public class News implements Serializable {

  private static final long serialVersionUID = -5666603119515048272L;
  private final String title;
  private final LocalDateTime published;
  private final String permalink;
  private final String content;
}
