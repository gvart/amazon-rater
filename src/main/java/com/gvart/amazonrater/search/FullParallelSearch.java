package com.gvart.amazonrater.search;

import com.gvart.amazonrater.config.AmazonRateProperties;
import com.gvart.amazonrater.util.Splitter;
import com.gvart.amazonrater.web.client.CompletionWebClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

/**
 * Computation based on full parallel search, in the end all results being merged and calculated.
 */
@RequiredArgsConstructor
public final class FullParallelSearch implements SearchStrategy {

  private final CompletionWebClient webClient;
  private final AmazonRateProperties.SearchAccuracy accuracy;

  @SneakyThrows
  @Override
  public int computeScore(String keyword) {
    List<String> chunks = Splitter.fixedLengthAppended(keyword, accuracy.getStep());

    int matches =
        (int)
            chunks.parallelStream()
                .map(it -> webClient.findBestMatch(it).contains(keyword))
                .filter(it -> it)
                .count();

    return 100 * matches / chunks.size();
  }
}
