package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.exception.IndexOutOfBoundaryException;
import com.thoughtworks.rslist.exception.StartOrEndParamOutOfBoundaryException;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RsController {

  private final RsService rsService;

  public RsController(RsService rsService) {
    this.rsService = rsService;
  }

  @GetMapping("/rs/getEvent")
  public ResponseEntity<RsEvent> getEvent(@RequestParam int id) {
    RsEvent event = rsService.getEvent(id);
    if (event == null) {
      throw new IndexOutOfBoundaryException("invalid index");
    } else {
      return ResponseEntity.ok(event);
    }
  }

  @GetMapping("/rs/getEventList")
  public ResponseEntity<List<RsEvent>> getEventListOfSpecifiedRange(@RequestParam (required = false) Integer start, @RequestParam (required = false) Integer end) {
    List<RsEvent> rsEventList = rsService.getEventList(start, end);
    if (rsEventList == null) {
      throw new StartOrEndParamOutOfBoundaryException("invalid request param");
    } else {
      return ResponseEntity.ok(rsEventList);
    }
  }

  @PostMapping("/rs/addEvent")
  public ResponseEntity<String> addEvent(@RequestBody @Valid RsEvent rsEvent) {
    int index = rsService.addEvent(rsEvent);
    return ResponseEntity.created(null).header("index", String.valueOf(index)).build();
  }

  @GetMapping("/rs/updateEvent")
  public ResponseEntity<String> updateEvent(@RequestParam int id, @RequestParam (required = false) String name,
                          @RequestParam (required = false) String keyword) {
    rsService.updateEvent(id, name, keyword);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/rs/deleteEvent")
  public void deleteEvent(@RequestParam int id) {
    rsService.deleteEventById(id);
  }
}
