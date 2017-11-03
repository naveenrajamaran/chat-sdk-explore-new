package com.articles.fragments;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.articles.ArticleAdapter;
import com.articles.ArticleModel;
import com.braunster.chatsdk.R;

import java.util.ArrayList;




import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.braunster.chatsdk.R;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.BNetworkManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DELL on 6/7/2017.
 */

public class BookmarkedArticles extends Fragment {

    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> url = new ArrayList<>();
    ArrayList<Integer> likes = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> userLikes = new ArrayList<>();
    ArrayList<String> userBookmarks = new ArrayList<>();
    private ArticleAdapter adapter;
    private RecyclerView MyRecyclerView;
    private ArrayList<ArticleModel> list = new ArrayList<>();
    private int totalArticles = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("onCreate()", "started bookmark");
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();


        getActivity().setTitle("Recent Articles");
        Log.i("onCreate()", "ended");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstantState) {
        Log.i("onCreateView()", "started");
        View v = inflater.inflate(R.layout.articles_tab_fragment_layout, container, false);
        Log.i("onCreateView()", "ended");
        return v;


    }

    @Override
    public void onStart() {
        Log.i("onStart()", "started");
        super.onStart();
        MyRecyclerView = (RecyclerView) getActivity().findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        performAsyncTask();
        Log.i("onStart()", "ended");


    }


    public void performAsyncTask() {

        Log.i("performAsyncTask()", "started");

        if (list.size() > 0) {
            settingAdapter();
        } else {
            Log.i("performAsyncTask()", "list is empty");
            boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext());
            if (isNetworkAvailable) {
                Log.i("performAsyncTask()", "DoRSSFeedTask started");
                new DoRssFeedTask().execute();
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DoRssFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("onPreExecute()", "started");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("doInBackground()", "started");

            BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
            String userString = user.getEntityID();
            Firebase refUserLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/likes/");
            Query userLikesQuery = refUserLikes.orderByKey().startAt("1");
            userLikesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot likes : dataSnapshot.getChildren()){
                        userLikes.add(String.valueOf(likes.child("id").getValue()));
                        Log.i("userLikes",String.valueOf(likes.child("id").getValue()));
                    }



                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebase refUserBookmarks = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/bookmarks/");
            Query userBookmarksQuery = refUserBookmarks.orderByKey().startAt("1");
            userBookmarksQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot likes : dataSnapshot.getChildren()){
                        userBookmarks.add(String.valueOf(likes.child("id").getValue()));
                        Log.i("userBookmarks",String.valueOf(likes.child("id").getValue()));
                    }



                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Firebase refArticles = new Firebase("https://revsmarttech.firebaseio.com/testRoot/articles/");
            Firebase refTotalArticles = new Firebase("https://revsmarttech.firebaseio.com/testRoot/articles/0/total/");
            refTotalArticles.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = String.valueOf(dataSnapshot.getValue());
                    totalArticles = Integer.parseInt(value);
                    Log.i("Total Articles", value);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Query query = refArticles.orderByKey().startAt("1");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot articles : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        description.add(String.valueOf(articles.child("des").getValue()));
                        images.add(String.valueOf(articles.child("img").getValue()));
                        likes.add(Integer.parseInt(String.valueOf(articles.child("likes").getValue())));
                        url.add(String.valueOf(articles.child("url").getValue()));
                        title.add(String.valueOf(articles.child("title").getValue()));
                        id.add(String.valueOf(articles.child("id").getValue()));

                        Log.i("ArticleID", String.valueOf(articles.child("id").getValue()));
//                        Log.i("image url", String.valueOf(articles.child("img").getValue()));
//                        Log.i("likes", String.valueOf(articles.child("likes").getValue()));
//                        Log.i("article url", String.valueOf(articles.child("url").getValue()));
                    }
                    initializeList();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });


            Log.i("doInBackground()", "ended");
            return "Exe";


        }

        @Override
        protected void onPostExecute(String result) {


            Log.i("onPostExecute()", "ended");
        }

    }

    public void initializeList() {
        Log.i("initialiseList()", "started");

        list.clear();
        Log.d("Totalarticlesinlist", String.valueOf(totalArticles));

        for (int i = totalArticles - 1; i >= 0; i--) {


            ArticleModel item = new ArticleModel();
            item.setDescription(description.get(i));
            item.setImageUrl(images.get(i));
            item.setArticleURL(url.get(i));
            item.setTitle(title.get(i));
            item.setLikes(likes.get(i));
            item.setArticleId(id.get(i));
            for (int j=0; j< userLikes.size(); j++){
                if(userLikes.get(j).equals(id.get(i))){
                    item.setLiked(true);
                    break;

                }else {
                    item.setLiked(false);
                }

            }
            for (int k=0; k< userBookmarks.size(); k++){
                if(userBookmarks.get(k).equals(id.get(i))){
                    item.setBookmarked(true);
                    list.add(item);
                    break;

                }else {
                    item.setBookmarked(false);
                }

            }
//            item.setIsturned(0);

        }
        Log.i("initialiseList()", "ended");
        Log.i("lengthlist", String.valueOf(list.size()));
        settingAdapter();


    }

    public void settingAdapter() {
        Log.i("settingAdapter()", "started");
        adapter = new ArticleAdapter(getActivity(), list);
        if (list.size() > 0 & MyRecyclerView != null) {
            Log.i("settingAdapter()", "set");
            MyRecyclerView.setAdapter(adapter);
        }
        Log.i("settingAdapter()", "ended");
    }
}