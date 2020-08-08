package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RsServiceTest {

    @Autowired
    private RsService rsService;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private UserRepository userRepository;

    private static final int INITIAL_GENERATED_VALUE = 0;

    @BeforeEach
    public void setUp() {
        UserDto userTestData1 = new UserDto("user1", 20, "male", "user1@gmail.com", "12345678901");
        UserDto userTestData2 = new UserDto("user2", 30, "female", "user2@gmail.com", "12345678902");
        UserDto userTestData3 = new UserDto("user3", 40, "male", "user3@gmail.com", "12345678903");
        userRepository.save(userTestData1);
        userRepository.save(userTestData2);
        userRepository.save(userTestData3);

        RsEventDto eventTestData1 = new RsEventDto("name1", "keyword1", userTestData1);
        RsEventDto eventTestData2 = new RsEventDto("name2", "keyword2", userTestData2);
        RsEventDto eventTestData3 = new RsEventDto("name3", "keyword3", userTestData3);
        rsEventRepository.save(eventTestData1);
        rsEventRepository.save(eventTestData2);
        rsEventRepository.save(eventTestData3);
    }

    @AfterEach
    public void tearDown() {
        rsEventRepository.deleteAll();
    }

    @Test
    void should_return_event_of_specified_id() {
        System.out.println(rsEventRepository.findAll().toString());
        RsEvent rsEvent1 = rsService.getEvent(INITIAL_GENERATED_VALUE + 1);
        assertEquals("name1", rsEvent1.getName());
        assertEquals("keyword1", rsEvent1.getKeyword());
        assertEquals(1, rsEvent1.getUserId());

        RsEvent rsEvent2 = rsService.getEvent(INITIAL_GENERATED_VALUE + 2);
        assertEquals("name2", rsEvent2.getName());
        assertEquals("keyword2", rsEvent2.getKeyword());
        assertEquals(2, rsEvent2.getUserId());

        RsEvent rsEvent3 = rsService.getEvent(INITIAL_GENERATED_VALUE + 3);
        assertEquals("name3", rsEvent3.getName());
        assertEquals("keyword3", rsEvent3.getKeyword());
        assertEquals(3, rsEvent3.getUserId());
    }

    @Test
    void should_return_whole_event_list_when_start_or_end_is_null() {
        System.out.println(rsEventRepository.findAll().toString());
        List<RsEvent> wholeRsEventList1 = rsService.getEventList(null, 1);
        List<RsEvent> wholeRsEventList2 = rsService.getEventList(1, null);
        List<RsEvent> wholeRsEventList3 = rsService.getEventList(null, null);
        assertEquals(3, wholeRsEventList1.size());
        assertEquals("name1", wholeRsEventList1.get(0).getName());
        assertEquals("keyword1", wholeRsEventList1.get(0).getKeyword());
        assertEquals(1, wholeRsEventList1.get(0).getUserId());
        assertEquals("name2", wholeRsEventList1.get(1).getName());
        assertEquals("keyword2", wholeRsEventList1.get(1).getKeyword());
        assertEquals(2, wholeRsEventList1.get(1).getUserId());
        assertEquals("name3", wholeRsEventList1.get(2).getName());
        assertEquals("keyword3", wholeRsEventList1.get(2).getKeyword());
        assertEquals(3, wholeRsEventList1.get(2).getUserId());
        assertEquals(wholeRsEventList1.toString(), wholeRsEventList2.toString());
        assertEquals(wholeRsEventList1.toString(), wholeRsEventList3.toString());
    }

    @Test
    void should_return_null_when_start_or_end_out_of_boundary() {
        System.out.println(rsEventRepository.findAll().toString());
        assertNull(rsService.getEventList(0, 1));
        assertNull(rsService.getEventList(1, 6));
        assertNull(rsService.getEventList(-1, 8));
    }

    @Test
    void should_return_sublist_of_event_list() {
        System.out.println(rsEventRepository.findAll().toString());
        List<RsEvent> subList1 = rsService.getEventList(1, 2);
        assertEquals(2, subList1.size());
        assertEquals("name1", subList1.get(0).getName());
        assertEquals("keyword1", subList1.get(0).getKeyword());
        assertEquals(1, subList1.get(0).getUserId());
        assertEquals("name2", subList1.get(1).getName());
        assertEquals("keyword2", subList1.get(1).getKeyword());
        assertEquals(2, subList1.get(1).getUserId());
    }

    @Test
    void should_add_new_rs_event() {
        System.out.println(rsEventRepository.findAll().toString());
        System.out.println(userRepository.findAll().toString());
        RsEvent newRsEvent = new RsEvent("name4", "keyword4", 3);
        int id = rsService.addEvent(newRsEvent);
        System.out.println(rsEventRepository.findAll().toString());
        RsEvent addedRsEvent = rsService.getEvent(id);
        assertEquals("name4", addedRsEvent.getName());
        assertEquals("keyword4", addedRsEvent.getKeyword());
        assertEquals(3, addedRsEvent.getUserId());
    }

    @Test
    void should_update_event() {
        System.out.println(rsEventRepository.findAll().toString());
        RsEvent rsEvent1 = new RsEvent("newName1", "newKeyword1", 1);
        rsService.updateEvent(INITIAL_GENERATED_VALUE + 1, rsEvent1);
        RsEvent updatedEvent1 = rsService.getEvent(INITIAL_GENERATED_VALUE + 1);
        assertEquals("newName1", updatedEvent1.getName());
        assertEquals("newKeyword1", updatedEvent1.getKeyword());

        RsEvent rsEvent2 = new RsEvent("newName2", null, 2);
        rsService.updateEvent(INITIAL_GENERATED_VALUE + 2, rsEvent2);
        RsEvent updatedEvent2 = rsService.getEvent(INITIAL_GENERATED_VALUE + 2);
        assertEquals("newName2", updatedEvent2.getName());
        assertEquals("keyword2", updatedEvent2.getKeyword());

        RsEvent rsEvent3 = new RsEvent(null, "newKeyword3", 3);
        rsService.updateEvent(INITIAL_GENERATED_VALUE + 3, rsEvent3);
        RsEvent updatedEvent3 = rsService.getEvent(INITIAL_GENERATED_VALUE + 3);
        assertEquals("name3", updatedEvent3.getName());
        assertEquals("newKeyword3", updatedEvent3.getKeyword());
    }

    @Test
    void should_delete_the_specified_event() {
        System.out.println(rsEventRepository.findAll().toString());
        assertEquals(3, rsService.getEventList(null, null).size());
        RsEvent eventAtIndex2 = rsService.getEvent(INITIAL_GENERATED_VALUE + 2);
        assertEquals("name2", eventAtIndex2.getName());
        assertEquals("keyword2", eventAtIndex2.getKeyword());
        assertEquals(2, eventAtIndex2.getUserId());

        rsService.deleteEventById(INITIAL_GENERATED_VALUE + 2);

        assertEquals(2, rsService.getEventList(null, null).size());
        assertEquals("name2", eventAtIndex2.getName());
        assertEquals("keyword2", eventAtIndex2.getKeyword());
        assertEquals(2, eventAtIndex2.getUserId());
    }

}
