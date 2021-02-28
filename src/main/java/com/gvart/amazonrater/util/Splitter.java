package com.gvart.amazonrater.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Splitter {

  private Splitter() {}

  /** Split a string in smaller chunks with length of chunkSize. */
  public static List<String> fixedLength(String string, int chunkSize) {
    int length = string.length();
    int ceil = (int) Math.ceil(length / (double) chunkSize);

    return IntStream.iterate(0, i -> i + chunkSize)
        .limit(ceil)
        .mapToObj(i -> string.substring(i, Math.min(i + chunkSize, length)))
        .collect(Collectors.toList());
  }

  /**
   * Split input string using {@link #fixedLength} and then append for each following element the
   * previous one.
   *
   * <p>Example: For input "ABC" and chunkSize 1, output will be ["A", "AB", "ABC]
   */
  public static List<String> fixedLengthAppended(String string, int chunkSize) {
    List<String> chunks = fixedLength(string, chunkSize);
    for (int i = 1; i < chunks.size(); i++) {
      chunks.set(i, chunks.get(i - 1) + chunks.get(i));
    }
    return chunks;
  }
}
