package com.gvart.amazonrater.search;

import com.gvart.amazonrater.config.AmazonRateProperties;
import com.gvart.amazonrater.web.client.CompletionWebClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Computation based on sequential search from right to left by removing parts from keyword, search
 * is finished when results doesn't contains searched word.
 */
@RequiredArgsConstructor
public final class ReverseSequentialBreakSearch implements SearchStrategy {

  private static final Logger log = LoggerFactory.getLogger(ReverseSequentialBreakSearch.class);

  private final CompletionWebClient webClient;
  private final AmazonRateProperties.SearchAccuracy accuracy;

  @Override
  public int computeScore(String keyword) {
    int foundCounter = 0;

    for (int i = keyword.length(); i > 0; i -= accuracy.getStep()) {
      String queryString = keyword.substring(0, Math.max(1, i));
      List<String> bestMatch = webClient.findBestMatch(queryString);
      if (!bestMatch.contains(keyword)) {
        log.debug("Item is missing for searched item: {}", queryString);
        break;
      }

      foundCounter++;
    }

    return 100 * foundCounter / (keyword.length() / accuracy.getStep());
  }
}
