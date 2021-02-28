package com.gvart.amazonrater.search.service;

import com.gvart.amazonrater.search.SearchStrategy;
import com.gvart.amazonrater.search.domain.RateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RateCalculatorService {

  private final SearchStrategy searchStrategy;

  public RateResult calculate(String keyword) {
    RateResult result = new RateResult(keyword.trim().toLowerCase(), 0);

    if (!StringUtils.hasLength(keyword)) {
      return result;
    }

    int score = searchStrategy.computeScore(result.getKeyword());
    result.setScore(score);

    return result;
  }
}
