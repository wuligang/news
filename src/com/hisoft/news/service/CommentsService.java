package com.hisoft.news.service;

import com.hisoft.news.javabean.Comments;

/**
 * @program: news
 * @description:
 * @author: wlg
 * @create: 2021-06-29 16:12:00
 **/
public interface CommentsService {
    /**
     * 发布评论
     * @param comments
     * @return
     */
    int publishComments(Comments comments);
}
