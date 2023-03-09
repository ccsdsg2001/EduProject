package com.example.guliMall.ware;

import lombok.Data;

import java.util.List;

/**
 * @author cc
 * @date 2023年03月09日 21:42
 */
@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;
}
