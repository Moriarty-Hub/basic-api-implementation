package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.exception.IndexOutOfBoundaryException;
import com.thoughtworks.rslist.exception.StartOrEndParamOutOfBoundaryException;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RsController {

  private final RsService rsService;

  public RsController(RsService rsService) {
    this.rsService = rsService;
  }

  @GetMapping("/rs/event/{id}")
  public ResponseEntity<RsEvent> getEvent(@PathVariable int id) {
    RsEvent event = rsService.getEvent(id);
    if (event == null) {
      throw new IndexOutOfBoundaryException("invalid index");
    } else {
      return ResponseEntity.ok(event);
    }
  }

  @GetMapping({"/rs/eventList/start/{start}/end/{end}", "/rs/eventList/start/{start}", "/rs/eventList/end/{end}","/rs/eventList"})
  public ResponseEntity<List<RsEvent>> getEventListOfSpecifiedRange(@PathVariable (required = false) Integer start, @PathVariable (required = false) Integer end) {
    List<RsEvent> rsEventList = rsService.getEventList(start, end);
    if (rsEventList == null) {
      throw new StartOrEndParamOutOfBoundaryException("invalid request param");
    } else {
      return ResponseEntity.ok(rsEventList);
    }
  }

  @PostMapping("/rs/event")
  public ResponseEntity<String> addEvent(@RequestParam String name, @RequestParam String keyword, @RequestParam Integer userId) {
    RsEvent rsEvent = new RsEvent(name, keyword, userId);
    int index = rsService.addEvent(rsEvent);
    return ResponseEntity.created(null).header("index", String.valueOf(index)).build();
  }

  @PatchMapping("/rs/event/{rsEventId}")
  public ResponseEntity<String> updateEvent(@PathVariable int rsEventId, @RequestParam (required = false) String name,
                                            @RequestParam (required = false) String keyword, @RequestParam Integer userId) {
    RsEvent rsEvent = new RsEvent(name, keyword, userId);
    rsService.updateEvent(rsEventId, rsEvent);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/event/{id}")
  public void deleteEvent(@PathVariable int id) {
    rsService.deleteEventById(id);
  }
}
