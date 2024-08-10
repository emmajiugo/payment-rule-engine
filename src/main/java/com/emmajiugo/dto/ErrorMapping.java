package com.emmajiugo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMapping {
    private String type;
    private String message;
    private Object errors;
}