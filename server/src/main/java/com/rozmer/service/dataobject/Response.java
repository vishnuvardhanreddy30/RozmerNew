package com.rozmer.service.dataobject;

import java.util.List;

public interface Response<T> {

    T getData();
    List<String> getMessage();
    void setData(T data);
    void setMessage (List<String> message);
    void setTimeStamp(long currentTimeMillis);

}
