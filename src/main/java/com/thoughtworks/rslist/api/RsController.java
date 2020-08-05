package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

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
    UserController userController = appContext.getBean(UserController.class);
    if (userController.getUserByUsername(rsEvent.getUser().getUserName()) == null) {
      userController.addNewUser(rsEvent.getUser());
    }
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
