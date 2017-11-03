package com.articles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.braunster.chatsdk.R;
import com.braunster.chatsdk.dao.BUser;
import com.braunster.chatsdk.network.BNetworkManager;
import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    private ArrayList<ArticleModel> list;
    private Context mContext;




//    public MyAdapter(ArrayList<WonderModel> Data) {
//        list = Data;
//    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articles_recycler_view_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ArticleModel articleModel = list.get(position);
        holder.titleTextView.setText(String.valueOf(list.get(position).getTitle()));
        holder.descriptionTextView.setText(list.get(position).getDescription());
        //holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
        //holder.coverImageView.setTag(list.get(position).getImageResourceId());
        if(String.valueOf(list.get(position).getLiked()).equals("true")) {
            holder.likeImageView.setTag(R.drawable.ic_liked);
            holder.likeImageView.setImageResource(R.drawable.ic_liked);
        }else{
            holder.likeImageView.setTag(R.drawable.ic_like);
        }
        if(String.valueOf(list.get(position).getBookmarked()).equals("true")) {
            holder.bookmarkImageView.setTag(R.drawable.ic_bookmarked);
            holder.bookmarkImageView.setImageResource(R.drawable.ic_bookmarked);
        }else{
            holder.bookmarkImageView.setTag(R.drawable.ic_bookmark);
        }
//        holder.likeImageView.setTag(R.drawable.ic_like);
//        holder.bookmarkImageView.setTag(R.drawable.ic_bookmark);
        holder.likeCountTextView.setText(list.get(position).getLikes());

        Glide.with(mContext).load(articleModel.getImageUrl()).into(holder.coverImageView);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArticleAdapter(Context mContext, ArrayList<ArticleModel> list) {
        this.mContext = mContext;
        this.list = list;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public ImageView bookmarkImageView;
        public TextView likeCountTextView;
        public TextView descriptionTextView;


        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            bookmarkImageView = (ImageView)v.findViewById(R.id.bookmarkImageView);
            likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TextView tv = (TextView) v.findViewById(R.id.titleTextView);
//                    String id = tv.getText().toString();
                    String articleUrl = list.get(getAdapterPosition()).getArticleURL();

                    Intent i = new Intent(mContext, WebViewActivity.class);
                    i.putExtra("ARTICLE_URL", articleUrl);

                    mContext.startActivity(i);
                }
            });

            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){

                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);

                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        String userString = user.getEntityID();
                        String articleId = list.get(getAdapterPosition()).getArticleId();
                        Firebase refUserLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/likes/" + articleId + "/id/");
                        refUserLikes.setValue(String.valueOf(articleId));
                        Firebase refArticleLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/articles/" + articleId + "/likes/");
                        refArticleLikes.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                int valueConvertInt = 0;
                                if(mutableData.getValue() != null){
                                    String value = (String) mutableData.getValue();
                                    valueConvertInt = Integer.parseInt(value);
                                }
                                valueConvertInt++;
                                mutableData.setValue(String.valueOf(valueConvertInt));

                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

                                likeCountTextView.setText(String.valueOf(dataSnapshot.getValue()));

                            }
                        });






                    }else{

                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);

                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        String userString = user.getEntityID();
                        String articleId = list.get(getAdapterPosition()).getArticleId();
                        Firebase refUserLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/likes/" + articleId + "/id/");
                        refUserLikes.setValue(null);
                        Firebase refArticleLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/articles/" + articleId + "/likes/");
                        refArticleLikes.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                int valueConvertInt = 0;
                                if(mutableData.getValue() != null){
                                    String value = (String) mutableData.getValue();
                                    valueConvertInt = Integer.parseInt(value);
                                }
                                valueConvertInt--;
                                mutableData.setValue(String.valueOf(valueConvertInt));
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                likeCountTextView.setText(String.valueOf(dataSnapshot.getValue()));
                            }
                        });
                    }







                }
            });
            bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)bookmarkImageView.getTag();
                    if( id == R.drawable.ic_bookmark){

                        bookmarkImageView.setTag(R.drawable.ic_bookmarked);
                        bookmarkImageView.setImageResource(R.drawable.ic_bookmarked);

                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        String userString = user.getEntityID();
                        String articleId = list.get(getAdapterPosition()).getArticleId();
                        Firebase refUserLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/bookmarks/" + articleId + "/id/");
                        refUserLikes.setValue(String.valueOf(articleId));

                    }else{

                        bookmarkImageView.setTag(R.drawable.ic_bookmark);
                        bookmarkImageView.setImageResource(R.drawable.ic_bookmark);

                        BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                        String userString = user.getEntityID();
                        String articleId = list.get(getAdapterPosition()).getArticleId();
                        Firebase refUserLikes = new Firebase("https://revsmarttech.firebaseio.com/testRoot/users/" + userString + "/bookmarks/" + articleId + "/id/");
                        refUserLikes.setValue(null);
                    }
                }
            });


            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                    String articleUrl = list.get(getAdapterPosition()).getArticleURL();
                    String description = titleTextView.getText().toString();


                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,articleUrl);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, description);
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share"));



                }
            });



        }
    }
}