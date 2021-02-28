package com.gvart.amazonrater.web.controller;

import com.gvart.amazonrater.search.domain.RateResult;
import com.gvart.amazonrater.search.service.RateCalculatorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rate/amazon")
public class AmazonRateController {

  private static final Logger log = LoggerFactory.getLogger(AmazonRateController.class);

  private final RateCalculatorService rateCalculatorService;

  @GetMapping
  public RateResult searchRateByKeyword(@RequestParam(name = "keyword") String keyword) {
    long startTime = System.currentTimeMillis();

    RateResult result = rateCalculatorService.calculate(keyword);

    log.debug("Calculation was executed in {} milliseconds.", System.currentTimeMillis() - startTime);

    return result;
  }
}
