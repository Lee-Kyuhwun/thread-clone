package com.fastcampus.thread.model.user;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public record UserPatchRequestBody(String description) {


}
