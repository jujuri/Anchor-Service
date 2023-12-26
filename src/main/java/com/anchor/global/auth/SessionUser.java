package com.anchor.global.auth;

import com.anchor.domain.user.domain.User;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionUser implements Serializable {

  private String email;
  private String nickname;
  private String image;

  public SessionUser(User user) {
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.image = user.getImage();
  }

}