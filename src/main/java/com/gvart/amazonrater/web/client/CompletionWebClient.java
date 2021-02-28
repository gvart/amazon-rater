package com.gvart.amazonrater.web.client;

import com.gvart.amazonrater.config.AmazonRateProperties;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompletionWebClient {

  private static final Logger log = LoggerFactory.getLogger(CompletionWebClient.class);

  private final RestTemplate restTemplate;
  private final AmazonRateProperties rateProperties;

  public List<String> findBestMatch(String keyword) {
    log.debug("Request to fetch completions for '{}' keyword", keyword);

    if (!StringUtils.hasLength(keyword)) {
      return Collections.emptyList();
    }

    String response =
        restTemplate.getForObject(rateProperties.getUrlTemplate(), String.class, keyword);

    return JsonPath.parse(response).read("$.[1][*]");
  }
}
