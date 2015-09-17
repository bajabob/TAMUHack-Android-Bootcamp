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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bob on 9/13/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private WeakReference<Context> mContext;

    private ArrayList<Message> mMessages = new ArrayList<Message>();

    public MessagesAdapter(Context c){
        mContext = new WeakReference<Context>(c);
    }

    public void setMessages(ArrayList<Message> messages){
        mMessages = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_message, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder holder, int position) {
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public boolean areListsDifferent(ArrayList<Message> newMessages){
        if(mMessages.size() == 0){
            return true;
        }
        return newMessages.size() != mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImage;
        private TextView mMessage;
        private TextView mMeta;

        public ViewHolder(View v) {
            super(v);

            mImage = (CircleImageView) v.findViewById(R.id.image);
            mMessage = (TextView) v.findViewById(R.id.message);
            mMeta = (TextView) v.findViewById(R.id.meta);
        }

        public void setMessage(Message message){
            mMessage.setText(message.getMessage());

            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("UTC"));
            CharSequence time = DateUtils.getRelativeTimeSpanString(message.getDate(), c.getTimeInMillis(),
                    DateUtils.FORMAT_ABBREV_RELATIVE);

            mMeta.setText(message.getUsername() + ", " + time);

            if(!TextUtils.isEmpty(message.getUserProfilePictureUrl())) {
                if(mContext != null) {
                    Picasso.with(mContext.get()).load(message.getUserProfilePictureUrl()).into(mImage);
                }
            }
        }
    }
}
