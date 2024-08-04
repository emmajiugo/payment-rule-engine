package com.emmajiugo.ruleImpl.mapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class RuleMapper<C, A> {
    protected C condition;
    protected A action;
}
