package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private final List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");

  @GetMapping("/rs/getEvent")
  public String getEvent(@RequestParam int id) {
    return rsList.get(id - 1);
  }

  @GetMapping("/rs/getEventList")
  public List<String> getEventListOfSpecifiedRange(@RequestParam int start, @RequestParam int end) {
    return rsList.subList(start - 1, end);
  }
}
