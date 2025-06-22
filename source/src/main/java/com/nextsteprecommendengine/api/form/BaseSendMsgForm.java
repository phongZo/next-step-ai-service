package com.nextsteprecommendengine.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSendMsgForm<T> {
    private String app;
    private String cmd;
    private String subCmd;
    private T data;
    private String responseCode;
    private String token;
}
