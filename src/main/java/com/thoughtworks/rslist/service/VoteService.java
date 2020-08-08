package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bean.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.VoteNumInsufficientException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final RsEventRepository rsEventRepository;

    public VoteService(UserRepository userRepository, VoteRepository voteRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
    }

    public List<Vote> getVoteRecordList(Integer userId, Integer rsEventId) {
        List<VoteDto> voteDtoList = voteRepository.findAll();
        List<Vote> voteList = new LinkedList<>();
        voteDtoList.forEach(voteDto -> {
            Vote vote = new Vote(voteDto.getUserDto().getId(), voteDto.getRsEventDto().getId(), voteDto.getNum(),
                    voteDto.getLocalDateTime());
            voteList.add(vote);
        });
        if (userId == null && rsEventId == null) {
            return voteList;
        }
        if (userId != null && rsEventId == null) {
            return voteList.stream().filter(vote -> vote.getUserId() == userId).collect(Collectors.toList());
        }
        if (userId == null) {
            return voteList.stream().filter(vote -> vote.getRsEventId() == rsEventId).collect(Collectors.toList());
        }
        return voteList.stream().filter(vote -> vote.getUserId() == userId && vote.getRsEventId() == rsEventId)
                .collect(Collectors.toList());
    }

    public void voteEvent(int rsEventId, int voteNum, int userId, String voteTime) {
        Optional<UserDto> optionalUserDto = userRepository.findById(userId);
        Optional<RsEventDto> optionalRsEventDto = rsEventRepository.findById(rsEventId);
        if (!optionalRsEventDto.isPresent() || !optionalUserDto.isPresent()) {
            return;
        }
        UserDto userDto = optionalUserDto.get();
        RsEventDto rsEventDto = optionalRsEventDto.get();
        if (userDto.getNumberOfVotes() < voteNum) {
            throw new VoteNumInsufficientException("vote num is insufficient");
        }
        userRepository.updateVoteNumById(userId, userDto.getNumberOfVotes() - voteNum);
        rsEventRepository.updateVoteNumById(rsEventId, rsEventDto.getVoteNum() + voteNum);
        voteRepository.save(new VoteDto(LocalDateTime.parse(voteTime), voteNum, rsEventDto, userDto));
    }
}
