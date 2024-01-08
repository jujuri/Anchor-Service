package com.anchor.domain.mentor.api.controller;

import com.anchor.domain.mentor.api.controller.request.MentoringStatusInfo;
import com.anchor.domain.mentor.api.controller.request.MentoringUnavailableTimeInfo;
import com.anchor.domain.mentor.api.service.MentorService;
import com.anchor.domain.mentor.api.service.response.MentoringUnavailableTimes;
import com.anchor.global.auth.SessionUser;
import com.anchor.global.util.type.Link;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mentors/me")
@RequiredArgsConstructor
@RestController
public class MentorController {

  private final MentorService mentorService;

  @GetMapping("/schedule")
  public ResponseEntity<MentoringUnavailableTimes> getUnavailableTimes(HttpSession httpSession) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    MentoringUnavailableTimes result = mentorService.getUnavailableTimes(user.getMentorId());
    result.addLinks(Link.builder()
                        .setLink("self", "/me/schedule")
                        .build());
    return ResponseEntity.ok(result);
  }

  @PostMapping("/schedule")
  public ResponseEntity<String> registerUnavailableTimes(
      @Valid @RequestBody MentoringUnavailableTimeInfo mentoringUnavailableTimeInfo, HttpSession httpSession) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    mentorService.setUnavailableTimes(user.getMentorId(), mentoringUnavailableTimeInfo.getDateTimeRanges());
    log.info("IsEmpty: {}", mentoringUnavailableTimeInfo.getDateTimeRanges()
                                                        .isEmpty());
    return ResponseEntity.ok()
                         .build();
  }

  @PostMapping("/applied-mentorings")
  public ResponseEntity<String> changeMentoringStatus(
      @RequestBody MentoringStatusInfo mentoringStatusInfo,
      HttpSession httpSession) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    mentorService.changeMentoringStatus(user.getMentorId(), mentoringStatusInfo.getRequiredMentoringStatusInfos());
    return ResponseEntity.ok()
                         .build();
  }

}