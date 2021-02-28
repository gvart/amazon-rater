package com.gvart.amazonrater.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateResult {
  private String keyword;
  private int score;
}
