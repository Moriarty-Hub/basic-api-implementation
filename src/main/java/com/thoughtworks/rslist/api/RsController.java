package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import org.springframework.web.bind.annotation.*;

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
  public List<RsEvent> getEventListOfSpecifiedRange(@RequestParam (required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start != null && end != null) {
      return rsList.subList(start - 1, end);
    }
    return rsList;
  }

  @PostMapping("/rs/addEvent")
  public void addEvent(@RequestBody RsEvent rsEvent) {
    rsList.add(rsEvent);
  }

  @GetMapping("/rs/updateEvent")
  public void updateEvent(@RequestParam int id, @RequestParam (required = false) String name,
                          @RequestParam (required = false) String keyword) {
    if (name != null) {
      rsList.get(id - 1).setName(name);
    }
    if (keyword != null) {
      rsList.get(id - 1).setKeyword(keyword);
    }
  }

  @GetMapping("/rs/deleteEvent")
  public void deleteEvent(@RequestParam int id) {
    rsList.remove(id - 1);
  }
}
