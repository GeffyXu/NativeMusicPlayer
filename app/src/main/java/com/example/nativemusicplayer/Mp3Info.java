package com.example.nativemusicplayer;

/**
 * Created by 许国辉 on 2017/6/2.
 */


/*歌曲信息类*/
public class Mp3Info{

    private String title;
    private String artist;
    private long duration;
    private long size;
    private String url;
    private int avatar; //图片ID

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){return this.title;}

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){return this.artist;}

    public void setDuration(long duration){this.duration = duration;}

    public long getDuration(){return this.duration;}

    public void setSize(long size){this.size = size;}

    public long getSize(){return this.size;}

    public void setUrl(String url){this.url = url;}

    public String getUrl(){return this.url;}

    //图片
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
    public int getAvatar() {
        return avatar;
    }

}
