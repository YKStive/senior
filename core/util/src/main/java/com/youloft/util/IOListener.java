package com.youloft.util;

/**
 * Created by Jie Zhang on 2015/10/22.
 */
public interface IOListener {
    void copying(Long copySize);
    void copyOver(Long count);
}
