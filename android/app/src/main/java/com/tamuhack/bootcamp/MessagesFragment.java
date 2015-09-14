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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MessagesFragment extends Fragment {

    private static final String LOG = "MessagesFragment";

    private RecyclerView mRecyclerView;
    private MessagesAdapter mAdapter;


    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fetchMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.messages);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MessagesAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

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
                    Log.d(LOG, "Retrieved " + parseObjectList.size() + " messages");

                    // create message objects so that we can use them later
                    //  to make the visible list in the adapter
                    ArrayList<Message> messages = new ArrayList<Message>();
                    for(ParseObject po : parseObjectList){
                        messages.add(new Message(po));
                    }

                    mAdapter.setMessages(messages);

                } else {
                    Log.d(LOG, "Error: " + e.getMessage());
                }
            }
        });

    }

}
