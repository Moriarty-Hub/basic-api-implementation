package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bean.Vote;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecordList(@RequestParam (required = false) Integer userId, @RequestParam (required = false) Integer rsEventId) {
        return ResponseEntity.ok(voteService.getVoteRecordList(userId, rsEventId));
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity<ResponseEntity.BodyBuilder> voteEvent(@PathVariable int rsEventId, @RequestParam int voteNum,
                                                                @RequestParam int userId, @RequestParam String voteTime) {
        voteService.voteEvent(rsEventId, voteNum, userId, voteTime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voteRecordByTime")
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@RequestParam Integer startYear, @RequestParam Integer startMonth,
                                                          @RequestParam Integer startDay, @RequestParam Integer endYear,
                                                          @RequestParam Integer endMonth, @RequestParam Integer endDay) {
        return ResponseEntity.ok(voteService.getVoteRecordListByTime(startYear, startMonth, startDay, endYear, endMonth, endDay));
    }
}
