package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bean.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.UnmatchedUserIdException;
import com.thoughtworks.rslist.exception.UserNotExistException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RsService {

    private final UserService userService;
    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;

    public RsService(UserService userService, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.userService = userService;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    public RsEvent getEvent(int id) {
        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(id);
        if (optionalRsEventDto.isPresent()) {
            RsEventDto rsEventDto = optionalRsEventDto.get();
            return new RsEvent(rsEventDto.getName(), rsEventDto.getKeyword(), rsEventDto.getUserDto().getId(),
                    rsEventDto.getVoteNum());
        }
        return null;
    }

    public List<RsEvent> getEventList(Integer start, Integer end) {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        List<RsEvent> rsEventList = new LinkedList<>();
        rsEventDtoList.forEach(rsEventDto -> rsEventList.add(new RsEvent(rsEventDto.getName(), rsEventDto.getKeyword(),
                rsEventDto.getUserDto().getId(), rsEventDto.getVoteNum())));
        if (start == null || end == null) {
            return rsEventList;
        }
        if (start < 1 || end > rsEventList.size()) {
            return null;
        }
        return rsEventList.subList(start - 1, end);
    }

    public int addEvent(RsEvent rsEvent) {
/*        if (rsEvent.getName() == null || rsEvent.getKeyword() == null || rsEvent.getUserId() == null) {
            throw new NullParamException("invalid param");
        }*/
        int userId = rsEvent.getUserId();
        Optional<UserDto> optionalUserDto = userRepository.findById(userId);
        if (!optionalUserDto.isPresent()) {
            throw new UserNotExistException("user not exist");
        }
        UserDto userDto = optionalUserDto.get();
        return rsEventRepository.save(new RsEventDto(rsEvent.getName(), rsEvent.getKeyword(), userDto)).getId();
    }

    public void updateEvent(int id, RsEvent rsEvent) {
        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(id);
        if (optionalRsEventDto.isPresent()) {
            RsEventDto rsEventDto = optionalRsEventDto.get();
            if (rsEvent.getUserId() != rsEventDto.getUserDto().getId()) {
                throw new UnmatchedUserIdException("unmatched user id");
            }
            String name = rsEvent.getName();
            String keyword = rsEvent.getKeyword();
            if (name != null && keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, keyword);
            } else if (name != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, rsEventDto.getKeyword());
            } else if (keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, rsEventDto.getName(), keyword);
            }
        }
        /*Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(id);
        if (optionalRsEventDto.isPresent()) {
            RsEventDto rsEventDto = optionalRsEventDto.get();
            if (name != null && keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, keyword);
            } else if (name != null) {
                rsEventRepository.updateNameAndKeywordById(id, name, rsEventDto.getKeyword());
            } else if (keyword != null) {
                rsEventRepository.updateNameAndKeywordById(id, rsEventDto.getName(), keyword);
            }
        }*/
    }

    public void deleteEventById(int id) {
        rsEventRepository.deleteById(id);
    }

}
