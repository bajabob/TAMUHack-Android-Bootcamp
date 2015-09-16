/*
 * Copyright (c) 2015. Bob Timm, https://github.com/bajabob
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom
 *   the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tamuhack.bootcamp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MessagesFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MessagesFragment";

    private RecyclerView mRecyclerView;
    private MessagesAdapter mAdapter;
    private ImageButton mSend;
    private EditText mMessage;
    private ProgressBar mSpinner;
    private TextView mError;

    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for an internet connection
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if((cm.getActiveNetworkInfo() != null)) {
            this.fetchMessages();
        }else{
            showError();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.messages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MessagesAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mSend = (ImageButton) view.findViewById(R.id.send);
        mSend.setOnClickListener(this);

        mMessage = (EditText) view.findViewById(R.id.message);

        mSpinner = (ProgressBar) view.findViewById(R.id.spinner);

        mError = (TextView) view.findViewById(R.id.error);
        
        return view;
    }

    /**
     * Call whenever you want the most recent list of messages
     */
    private void fetchMessages(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.addAscendingOrder("createdAt");
        query.include("poster");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjectList, ParseException e) {
                if (e == null) {

                    // found some messages, print how many
                    Log.d(TAG, "Retrieved " + parseObjectList.size() + " messages");

                    // create message objects so that we can use them later
                    //  to make the visible list in the adapter
                    ArrayList<Message> messages = new ArrayList<Message>();
                    for(ParseObject po : parseObjectList){
                        messages.add(new Message(po));
                    }

                    // did we get any new messages? if so we should scroll to the bottom
                    boolean shouldScroll = (messages.size() != mAdapter.getItemCount());
                    mAdapter.setMessages(messages);

                    if(shouldScroll) {
                        mRecyclerView.scrollToPosition(messages.size() - 1);
                    }

                    // fetch messages again in a couple seconds
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            fetchMessages();
                        }
                    }, 5000);

                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                    showError();
                }
            }
        });

    }

    public void showError(){
        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_out_top);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // need to hide the view when the animation is complete
                mError.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mError.startAnimation(slideDown);
        mError.setVisibility(View.VISIBLE);
    }
    
    
    @Override
    public void onClick(View v) {

        // send button has been clicked
        if(v.getId() == R.id.send){

            String message = mMessage.getText().toString();

            if( !TextUtils.isEmpty(message)) {

                mSpinner.setVisibility(View.VISIBLE);
                mSend.setVisibility(View.GONE);

                ParseObject po = new ParseObject("Messages");
                po.put("message", message);
                po.put("poster", ParseObject.createWithoutData("User", "D0QU3Il9Zh"));
                po.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // clear the message
                            mMessage.setText("");
                            fetchMessages();
                        } else {
                            Log.e(TAG, "Could not send message: " + e.getLocalizedMessage());
                            showError();
                        }
                        mSpinner.setVisibility(View.GONE);
                        mSend.setVisibility(View.VISIBLE);
                    }
                });

            }
        }
    }
}
