package com.rozmer.service.dataobject;

import java.util.List;

import lombok.Data;

@Data
public class SuccessResponse<T> implements Response<T> {

    protected T data;
    protected List<String> message = null;
    protected Long timestamp;
    String resourceName;


    public SuccessResponse(){

        this(null, null);
        
    }

    public SuccessResponse(T data){

        this(data, null);
        
    }
    
    
    public SuccessResponse(List<String> message) {
        this.message = message;
    }

    
	public SuccessResponse(String resourceName) {
		super();
		this.resourceName = resourceName;
	}

    public SuccessResponse(T data2, List<String> message2) {
        this.data = data2;
        this.message =message2;
        this.timestamp = System.currentTimeMillis();
    }


    @Override
    public T getData() {
        
        return data;
    }

    @Override
    public List<String> getMessage() {
        return message;
    }

    @Override
    public void setData(T data) {
        this.data = data;
        
    }

    @Override
    public void setMessage(List<String> message) {
        this.message = message;
        
    }

    @Override
    public void setTimeStamp(long timestamp) {
       this.timestamp = timestamp;
        
    }
    
}
