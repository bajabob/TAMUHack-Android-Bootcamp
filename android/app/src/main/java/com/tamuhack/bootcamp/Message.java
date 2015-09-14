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

import com.parse.ParseObject;

/**
 * Created by bob on 9/13/15.
 */
public class Message {

    private String mUser;
    private String mMessage;
    private String mUserProfileUrl;
    private long mDate;


    public Message(ParseObject po){

        mMessage = po.getString("message");
        mDate = po.getCreatedAt().getTime();

        ParseObject user = po.getParseObject("poster");

        mUser = user.getString("name");
        mUserProfileUrl = user.getString("profilePictureUrl");
    }

    public long getDate(){
        return mDate;
    }

    public String getUsername(){
        return mUser;
    }

    public String getMessage(){
        return mMessage;
    }

    public String getUserProfilePictureUrl(){
        return mUserProfileUrl;
    }
}
