package com.example.finalexam.config.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import static net.minidev.json.JSONValue.toJSONString;

@Slf4j
@Data
@AllArgsConstructor
public class ResponseAuthentication {
    private Instant timestamp;
    private int code;
    private String message;
    private String path;

    public static void responseJson(HttpServletResponse response, ResponseAuthentication data) {

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(data.getCode());
            response.getWriter().print(toJSONString(data));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public static void responseSuccess(HttpServletResponse response, Object data) {

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().print(toJSONString(data));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public ResponseAuthentication(int code, String message, String path) {
        this.timestamp = Instant.now();
        this.code = code;
        this.message = message;
        this.path = path;
    }

//    public static ResponseAuthentication success(Object data) {
//        return ResponseAuthentication.response(200, "Success", data);
//    }
//
//    public static ResponseAuthentication fail(Object data) {
//        return ResponseAuthentication.response(500, "Fail", data);
//    }

}
