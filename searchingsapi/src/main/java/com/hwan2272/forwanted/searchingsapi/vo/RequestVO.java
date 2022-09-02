package com.hwan2272.forwanted.searchingsapi.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RequestVO {
    Map<String, String> company_name;
    List<Map<String, Map<String, String>>> tags;


}
