package com.syztb_idea_gsf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Boolean success;

    private String error;

    private Object data;

    public static Result ok(){
        return new Result(true,null,null);
    }

    public static Result ok(Object data){
        return new Result(true,null,data);
    }

    public static Result fail(String error){
        return new Result(false,error,null);
    }
}
