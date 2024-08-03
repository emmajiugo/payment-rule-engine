package com.emmajiugo.dto;

import com.emmajiugo.utils.Utils;

public record RuleDto(
        Object condition,
        Object action
) {
}
