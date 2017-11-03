package com.articles;

/**
 * Created by anonymous on 1/15/16.
 */
public class ArticleModel {

    String description;
    String imageUrl;
    String articleURL;
//    Double latitude;
//    Double longitude;
    String title;
    int likes;
    Boolean isLiked;
    Boolean isBookmarked;

    public Boolean getNearby() {
        return isNearby;
    }

    public void setNearby(Boolean nearby) {
        isNearby = nearby;
    }

    Boolean isNearby;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    String articleId;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }

    public String getLikes() {
        return (String.valueOf(likes));
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Boolean getBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}




