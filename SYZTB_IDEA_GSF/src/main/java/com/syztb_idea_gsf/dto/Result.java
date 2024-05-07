package com.syztb_idea_gsf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Boolean isSuccess;

    private String message;

    private Object data;


    public static Result ok(String message){
        return new Result(true,message,null);
    }

    public static Result ok(String message,Object data){
        return new Result(true,message,data);
    }

    public static Result fail(String message){
        return new Result(false,message,null);
    }
}
