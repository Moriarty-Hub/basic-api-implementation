package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.UserNotExistException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RsService {

    private final UserService userService;
    private final RsEventRepository rsEventRepository;

    public RsService(UserService userService, RsEventRepository rsEventRepository) {
        this.userService = userService;
        this.rsEventRepository = rsEventRepository;
    }

    public RsEvent getEvent(int id) {
        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(id);
        if (optionalRsEventDto.isPresent()) {
            RsEventDto rsEventDto = optionalRsEventDto.get();
            return new RsEvent(rsEventDto.getName(), rsEventDto.getKeyword(), rsEventDto.getUserId());
        }
        return null;
    }

    public List<RsEvent> getEventList(Integer start, Integer end) {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        List<RsEvent> rsEventList = new LinkedList<>();
        rsEventDtoList.forEach(rsEventDto -> rsEventList.add(new RsEvent(rsEventDto.getName(), rsEventDto.getKeyword(), rsEventDto.getUserId())));
        if (start == null || end == null) {
            return rsEventList;
        }
        if (start < 1 || end > rsEventList.size()) {
            return null;
        }
        return rsEventList.subList(start - 1, end);
    }

    public int addEvent(RsEvent rsEvent) {
        int userId = rsEvent.getUserId();
        if (userService.getUserByUserId(userId) != null) {
            RsEventDto rsEventDto = rsEventRepository.save(new RsEventDto(rsEvent.getName(), rsEvent.getKeyword(),
                    rsEvent.getUserId()));
            return rsEventDto.getId();
        } else {
            throw new UserNotExistException("user not exist");
        }
    }

    public void updateEvent(int id, String name, String keyword) {
        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(id);
        if (optionalRsEventDto.isPresent()) {
            RsEventDto rsEventDto = optionalRsEventDto.get();
            if (name != null && keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, keyword);
            } else if (name != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, rsEventDto.getKeyword());
            } else if (keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, rsEventDto.getName(), keyword);
            }
        }
    }

    public void deleteEventById(int id) {
        rsEventRepository.deleteById(id);
    }

}
