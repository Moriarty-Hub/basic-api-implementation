package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import com.thoughtworks.rslist.exception.IndexOutOfBoundaryException;
import com.thoughtworks.rslist.exception.StartOrEndParamOutOfBoundaryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RsController {

  @Autowired
  private ApplicationContext appContext;

  private final List<RsEvent> rsList = new LinkedList<>();

  public RsController() {
    User root = new User("root", 20, "male", "root@thoughtworks.com", "12345678901");
    User user1 = new User("user1", 30, "female", "user1@thoughtworks.com", "12345678902");
    User user2 = new User("user2", 40, "male", "user2@thoughtworks.com", "12345678903");
    rsList.add(new RsEvent("美股熔断", "经济", user1));
    rsList.add(new RsEvent("边境冲突", "军事", root));
    rsList.add(new RsEvent("示威活动", "自由", user2));
  }

  @GetMapping("/rs/getEvent")
  public ResponseEntity<RsEvent> getEvent(@RequestParam int id) {
    if (id < 1 || id > rsList.size()) {
      throw new IndexOutOfBoundaryException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(id - 1));
  }

  @GetMapping("/rs/getEventList")
  public ResponseEntity<List<RsEvent>> getEventListOfSpecifiedRange(@RequestParam (required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start != null && end != null) {
      if (start < 1 || end > rsList.size()) {
        throw new StartOrEndParamOutOfBoundaryException("invalid request param");
      }
      return ResponseEntity.ok(rsList.subList(start - 1, end));
    }
    return ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/addEvent")
  public ResponseEntity<String> addEvent(@RequestBody @Valid RsEvent rsEvent) {
    UserController userController = appContext.getBean(UserController.class);
    if (userController.getUserByUsername(rsEvent.getUser().getUserName()) == null) {
      userController.addNewUser(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    return ResponseEntity.created(null).header("index", String.valueOf(rsList.size())).build();
  }

  @GetMapping("/rs/updateEvent")
  public ResponseEntity<String> updateEvent(@RequestParam int id, @RequestParam (required = false) String name,
                          @RequestParam (required = false) String keyword) {
    if (name != null) {
      rsList.get(id - 1).setName(name);
    }
    if (keyword != null) {
      rsList.get(id - 1).setKeyword(keyword);
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("/rs/deleteEvent")
  public void deleteEvent(@RequestParam int id) {
    rsList.remove(id - 1);
  }
}
