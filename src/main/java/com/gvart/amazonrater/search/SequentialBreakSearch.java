package com.gvart.amazonrater.search;

import com.gvart.amazonrater.config.AmazonRateProperties;
import com.gvart.amazonrater.util.Splitter;
import com.gvart.amazonrater.web.client.CompletionWebClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Computation based on sequential search from left to right by appending chunks, search is finished
 * when result contains searched keyword.
 */
@RequiredArgsConstructor
public final class SequentialBreakSearch implements SearchStrategy {

  private static final Logger log = LoggerFactory.getLogger(SequentialBreakSearch.class);

  private final CompletionWebClient webClient;
  private final AmazonRateProperties.SearchAccuracy accuracy;

  @Override
  public int computeScore(String keyword) {
    StringBuilder searchString = new StringBuilder();
    List<String> chunks = Splitter.fixedLength(keyword, accuracy.getStep());
    int notFoundCounter = 0;

    for (String s : chunks) {
      String queryString = searchString.append(s).toString();

      List<String> bestMatch = webClient.findBestMatch(queryString);
      if (bestMatch.contains(keyword)) {
        log.debug("Found searched item on: {}", queryString);
        break;
      }
      notFoundCounter++;
    }
    return 100 * (chunks.size() - notFoundCounter) / chunks.size();
  }
}
