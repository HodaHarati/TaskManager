package com.example.taskmanager.model;

import com.example.taskmanager.greendao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "User")
public class User {

    @Id(autoincrement = true)
    private Long _id;

    @Unique
    @Property(nameInDb = "user_uuid")
    private String user_uuid;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mId;

    @Property(nameInDb = "username")
    private String mUsername;

    @Property(nameInDb = "password")
    private String mPassword;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public UUID getmUUID() {
        return mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public UUID getMId() {
        return this.mId;
    }

    public void setMId(UUID mId) {
        this.mId = mId;
    }

    public String getMUsername() {
        return this.mUsername;
    }

    public void setMUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getMPassword() {
        return this.mPassword;
    }

    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public User(String mUsename, String mPassword) {
        this();
        this.user_uuid = mId.toString();
        this.mUsername = mUsename;
        this.mPassword = mPassword;
    }

    public User() {
        mId = UUID.randomUUID();
    }

    public User (UUID id){
        this.mId = id;
    }

    @Generated(hash = 886209038)
    public User(Long _id, String user_uuid, UUID mId, String mUsername,
            String mPassword) {
        this._id = _id;
        this.user_uuid = user_uuid;
        this.mId = mId;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }
}
