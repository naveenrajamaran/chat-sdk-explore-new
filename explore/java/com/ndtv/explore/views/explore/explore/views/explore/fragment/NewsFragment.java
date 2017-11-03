package com.ndtv.explore.views.explore.explore.views.explore.fragment;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ndtv.explore.R;
import com.ndtv.explore.views.explore.explore.views.explore.adapters.NewsDataAdapter;
import com.ndtv.explore.views.explore.explore.views.explore.models.RSSFeed;
import com.ndtv.explore.views.explore.explore.views.explore.utils.ConnectionDetector;
import com.ndtv.explore.views.explore.explore.views.explore.utils.Constants;
import com.ndtv.explore.views.explore.explore.views.explore.utils.NewsFeedParser;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsFeedParser mNewsFeeder;
    private List<RSSFeed>  mRssFeedList = new ArrayList<RSSFeed>();


    public static NewsFragment newInstance(CharSequence title, int position) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("title", title);
        bundle.putInt("position", position);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i("Fragment", "Oncreate()");


    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Log.i("Fragment", "OncreateView()");
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();


        Log.i("Fragment", "onStart()");

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for vertical listview like items
        //  mLayoutManager = new LinearLayoutManager(getActivity());

        // StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2,1);
       // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.gridspan), 1));


        // Grid Layout

        //mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        performAsyncTask();





    }

    public void performAsyncTask() {


        if (mRssFeedList.size() > 0) {
            settingAdapter();
        } else {


            boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext());

            if (isNetworkAvailable) {

                Bundle args = getArguments();

                if (args != null)

                   {

                       new DoRssFeedTask().execute(Constants.TOPSTORIES[args.getInt("position")]);



                  //     RSSFeed rssFeed = new RSSFeed("title","http://www.google.com", "description","category", "pub date","guid","http://www.google.com");

                 //      mRssFeedList.add(rssFeed);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Oops!!! News Fragment", Toast.LENGTH_LONG).show();
                    }
                }

             else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }


    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        Log.i("Fragment", "onActivityCreated()");
    }*/

    public class DoRssFeedTask extends AsyncTask<String, Void, List<RSSFeed>> {
        ProgressDialog prog;
        String jsonStr = null;


        @Override
        protected void onPreExecute() {
           /* prog = new ProgressDialog(getActivity());
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();*/
            Log.i("Fragment ASyncTask", "onPreExecute()");
        }

        @Override
        protected List<RSSFeed> doInBackground(String... params) {
            for (String urlVal : params) {
                mNewsFeeder = new NewsFeedParser(urlVal);
            }
            mRssFeedList = mNewsFeeder.parseXmlData();

            Log.i("Fragment ASyncTask", "doInBackground()");

            return mRssFeedList;
        }

        @Override
        protected void onPostExecute(List<RSSFeed> result) {
           /* prog.dismiss();*/


            settingAdapter();


            Log.i("Fragment ASyncTask", "onPostExecute()");
        }

    }


    public void settingAdapter() {
        mAdapter = new NewsDataAdapter(getActivity(), mRssFeedList);
        int count = mRssFeedList.size();
        if (count != 0 && mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}