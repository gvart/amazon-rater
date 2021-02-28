package com.gvart.amazonrater.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Application specific configurations */
@Data
@ConfigurationProperties(prefix = "amazon.rater")
public class AmazonRateProperties {

  /** Accuracy calculation. The more precision is set, the more computing will take time */
  private SearchAccuracy accuracy = SearchAccuracy.HIGH;

  /** Depending on search strategy final scores could be different. */
  private SearchStrategyType searchStrategyType = SearchStrategyType.FULL_PARALLEL;

  /** Amazon computing web service url template */
  private String urlTemplate;

  public enum SearchAccuracy {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private final int step;

    SearchAccuracy(int step) {
      this.step = step;
    }

    public int getStep() {
      return step;
    }
  }

  public enum SearchStrategyType {
    FULL_PARALLEL,
    SEQUENTIAL_BREAK,
    REVERSE_SEQUENTIAL_BREAK;
  }
}
