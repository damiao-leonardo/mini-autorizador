package com.damiao.miniautorizador.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utilities {

  private Utilities() {
  }

  public static String encryptPassword(String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(password);
  }

}
