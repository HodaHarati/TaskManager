package com.example.taskmanager.model;

import com.example.taskmanager.greendao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity (nameInDb = "Task")
public class Task {

    @Id (autoincrement = true)
    private Long _id;

    @Property (nameInDb = "uuid")
    @Index (unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mId;

    @Property(nameInDb = "user_uuid")
    private String user_uuid;

    @Property (nameInDb = "title")
    private String mTitle;

    @Property (nameInDb = "description")
    private String mDetail;

    @Property (nameInDb = "date")
    private String mDate;

    @Property (nameInDb = "time")
    private String mTime;

    @Property (nameInDb = "state")
    private String mState;

      @Property (nameInDb = "pathPic")
      private String mPathPic;

      public String getmPathPic() {
          return mPathPic;
      }

    public void setmPathPic(String mPathPic) {
        this.mPathPic = mPathPic;
    }
    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mStateViewpager) {
        this.mState = mStateViewpager;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public UUID getMId() {
        return this.mId;
    }

    public void setMId(UUID mId) {
        this.mId = mId;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMDetail() {
        return this.mDetail;
    }

    public void setMDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public String getMDate() {
        return this.mDate;
    }

    public void setMDate(String mDate) {
        this.mDate = mDate;
    }

    public String getMTime() {
        return this.mTime;
    }

    public void setMTime(String mTime) {
        this.mTime = mTime;
    }

    public String getMState() {
        return this.mState;
    }

    public void setMState(String mState) {
        this.mState = mState;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }


    public Task(String mTitle, String mDetail, String mDate, String mTime, String state, String user_id, String path) {
        this.mId = UUID.randomUUID();
        this.mTitle = mTitle;
        this.mDetail = mDetail;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mState = state;
        this.user_uuid = user_id;
        this.mPathPic = path;
    }

    public Task(){
        this(UUID.randomUUID());
    }
    public Task(UUID id){
        mId =id;
    }

    @Generated(hash = 1784532348)
    public Task(Long _id, UUID mId, String user_uuid, String mTitle, String mDetail, String mDate, String mTime,
            String mState, String mPathPic) {
        this._id = _id;
        this.mId = mId;
        this.user_uuid = user_uuid;
        this.mTitle = mTitle;
        this.mDetail = mDetail;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mState = mState;
        this.mPathPic = mPathPic;
    }

    public String getPhotoName (){
        return "IMG_" + mId + ".jpg";
    }

    public String getMPathPic() {
        return this.mPathPic;
    }

    public void setMPathPic(String mPathPic) {
        this.mPathPic = mPathPic;
    }

  /*  public String getMPathPic() {
        return this.mPathPic;
    }

    public void setMPathPic(String mPathPic) {
        this.mPathPic = mPathPic;
    }
*/}

