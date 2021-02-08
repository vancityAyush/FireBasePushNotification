package com.senpai.firebasepushnotification;

import androidx.annotation.NonNull;

public class UserID {
    public String userId;
    public <T extends UserID> T withID(@NonNull final String id){
        this.userId = id;
        return (T)this;
    }
}
