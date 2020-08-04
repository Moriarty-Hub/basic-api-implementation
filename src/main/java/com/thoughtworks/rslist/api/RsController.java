package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RsController {
  private final List<RsEvent> rsList = new LinkedList<>();

  public RsController() {
    rsList.add(new RsEvent("美股熔断", "经济"));
    rsList.add(new RsEvent("边境冲突", "军事"));
    rsList.add(new RsEvent("示威活动", "自由"));
  }

  @GetMapping("/rs/getEvent")
  public RsEvent getEvent(@RequestParam int id) {
    return rsList.get(id - 1);
  }

  @GetMapping("/rs/getEventList")
  public List<RsEvent> getEventListOfSpecifiedRange(@RequestParam int start, @RequestParam int end) {
    return rsList.subList(start - 1, end);
  }
}
