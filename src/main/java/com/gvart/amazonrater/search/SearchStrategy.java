package com.gvart.amazonrater.search;

/** Base abstraction for search strategies in amazon completion web api */
public interface SearchStrategy {

  int computeScore(String keyword);
}
