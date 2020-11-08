package com.feilong.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/10/7 19:50
 */
@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TableData<T> implements Serializable {
    private int code;
    private String msg = "";
    private long count;
    private List<T> data;


    public TableData(long count, List<T> data) {
        this.count = count;
        this.data = data;
    }


}
