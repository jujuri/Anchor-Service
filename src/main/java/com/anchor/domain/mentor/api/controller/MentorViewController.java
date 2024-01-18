package com.anchor.domain.mentor.api.controller;

import com.anchor.domain.mentor.api.controller.request.MentorRegisterInfo;
import com.anchor.domain.mentor.api.service.MentorService;
import com.anchor.domain.mentor.api.service.response.AppliedMentoringSearchResult;
import com.anchor.global.auth.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mentors")
@RequiredArgsConstructor
@Controller
public class MentorViewController {

  private final MentorService mentorService;
  private final ViewResolver viewResolver;

  @GetMapping("/me/schedule")
  public String getUnavailableTimes(HttpSession httpSession, Model model) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    MentorOpenCloseTimes result = mentorService.getMentorSchedule(1L);
    model.addAttribute("openCloseTimes", result);
    return viewResolver.getViewPath("mentor", "schedule");
  }

  @GetMapping("/me/applied-mentorings")
  public String getMentoringApplications(
      @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable, HttpSession httpSession, Model model) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    Page<AppliedMentoringSearchResult> results = mentorService.getMentoringApplications(
        user.getMentorId(), pageable);
    model.addAttribute("mentoringApplications", results);
    return "mentoring-dashboard";
  }

  @GetMapping("/email-verification")
  public String verify(Model model) {
    MentorRegisterInfo mentorRegisterInfo = new MentorRegisterInfo("", Career.JUNIOR, "", "", "");
    model.addAttribute("mentorRegisterInfo", mentorRegisterInfo);
    return viewResolver.getViewPath("mentor", "email-verification");
  }

  @GetMapping("/register")
  public String register(@ModelAttribute MentorRegisterInfo mentorRegisterInfo, Model model) {
    log.info("email === " + mentorRegisterInfo.getCompanyEmail());
    model.addAttribute(mentorRegisterInfo);
    return viewResolver.getViewPath("mentor", "register");
  }

  @PostMapping
  public String registerProcess(@Valid @ModelAttribute MentorRegisterInfo mentorRegisterInfo,
      BindingResult bindingResult, Model model
  ) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("mentorRegisterInfo", mentorRegisterInfo);
      return viewResolver.getViewPath("mentor", "register");
    }
    mentorService.register(mentorRegisterInfo);
    return viewResolver.getViewPath("mentor", "register");
  }
}
