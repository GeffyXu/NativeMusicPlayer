package com.example.nativemusicplayer;

/**
 * Created by on 2017/6/2.
 */
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.ListView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*读取本地音乐文件到list*/
public class FindSongs {
    public static ArrayList<Mp3Info> getMp3Infos(Context Context) {

        Cursor cursor = Context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Mp3Info mp3Info = new Mp3Info();                               //新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            cursor.moveToNext();

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));	//文件大小

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));	//文件路径

            if (duration/(1000 * 60) >= 2) {		//只把1分钟以上的音乐添加到集合当中

                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Info.setAvatar(R.drawable.off);
                mp3Infos.add(mp3Info);
            }
        }
        cursor.close();
        return mp3Infos;
    }

    public void setListAdpter(Context context,List<Mp3Info> mp3Infos,ListView mMusicList) {

        List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String , String>>();
        MyListViewAdapter mAdapter = new MyListViewAdapter(context, mp3Infos);
        mMusicList.setAdapter(mAdapter);
    }
}
