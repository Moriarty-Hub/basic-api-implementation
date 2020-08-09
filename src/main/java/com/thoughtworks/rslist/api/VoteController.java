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

    @GetMapping({"/rs/voteRecord/userId/{userId}/rsEventId/{rsEventId}", "/rs/voteRecord/userId/{userId}",
            "/rs/voteRecord/rsEventId/{rsEventId}", "/rs/voteRecord"})
    public ResponseEntity<List<Vote>> getVoteRecordList(@PathVariable (required = false) Integer userId,
                                                        @PathVariable (required = false) Integer rsEventId) {
        return ResponseEntity.ok(voteService.getVoteRecordList(userId, rsEventId));
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity<ResponseEntity.BodyBuilder> voteEvent(@PathVariable int rsEventId, @RequestParam int voteNum,
                                                                @RequestParam int userId, @RequestParam String voteTime) {
        voteService.voteEvent(rsEventId, voteNum, userId, voteTime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rs/voteRecord/startYear/{startYear}/startMonth/{startMonth}/startDay/{startDay}/endYear/{endYear}/endMonth/{endMonth}/endDay/{endDay}")
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@PathVariable Integer startYear, @PathVariable Integer startMonth,
                                                          @PathVariable Integer startDay, @PathVariable Integer endYear,
                                                          @PathVariable Integer endMonth, @PathVariable Integer endDay) {
        return ResponseEntity.ok(voteService.getVoteRecordListByTime(startYear, startMonth, startDay, endYear, endMonth, endDay));
    }
}
