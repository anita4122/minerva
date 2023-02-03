package io.ansan.minc.token;

public record Position(int offset_start, int offset_end, int line, int colum) {

  public int len() {
    return offset_end - offset_start;
  }
}
