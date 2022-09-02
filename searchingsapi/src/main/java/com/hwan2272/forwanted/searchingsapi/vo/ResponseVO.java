package com.hwan2272.forwanted.searchingsapi.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseVO {
    String company_name;
    List<String> tags;
}
