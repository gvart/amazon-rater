package com.gvart.amazonrater.config;

import com.gvart.amazonrater.search.FullParallelSearch;
import com.gvart.amazonrater.search.ReverseSequentialBreakSearch;
import com.gvart.amazonrater.search.SearchStrategy;
import com.gvart.amazonrater.search.SequentialBreakSearch;
import com.gvart.amazonrater.web.client.CompletionWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SearchStrategyConfiguration {

  private final AmazonRateProperties amazonRateProperties;
  private final CompletionWebClient webClient;

  @Bean
  @ConditionalOnProperty(value = "amazon.rater.search-strategy-type", havingValue = "FULL_PARALLEL")
  public SearchStrategy fullSearchStrategy() {
    return new FullParallelSearch(webClient, amazonRateProperties.getAccuracy());
  }

  @Bean
  @ConditionalOnProperty(
      value = "amazon.rater.search-strategy-type",
      havingValue = "SEQUENTIAL_BREAK")
  public SearchStrategy sequentialBreakSearch() {
    return new SequentialBreakSearch(webClient, amazonRateProperties.getAccuracy());
  }

  @Bean
  @ConditionalOnProperty(
      value = "amazon.rater.search-strategy-type",
      havingValue = "REVERSE_SEQUENTIAL_BREAK")
  public SearchStrategy reverseSequentialBreakSearch() {
    return new ReverseSequentialBreakSearch(webClient, amazonRateProperties.getAccuracy());
  }
}
