package com.braunster.chatsdk.activities;

import android.util.Log;

/**
 * Created by DELL on 8/5/2017.
 */

public class ThreadIdStore {
    public static int id ;
  ThreadIdStore()
  {
      id=-1;
  }
  public void setid(int id)
  {
      this.id=id;
      Log.e("value thread",""+getid());
  }
  public static int getid(){
      return  id;
  }
}
