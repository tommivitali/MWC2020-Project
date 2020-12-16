package com.toedro.fao.ui.home;

/**
 * This is the simple class that stores data in a suitable way to retrieve informations and show
 * them in the homepage's view pager. It contains a constructor, three fields and relative get and
 * set methods.
 */
public class HomePagerData {
    Integer image;
    String content;
    String description;

    public Integer getImage() { return image; }
    public String getContent() {
        return content;
    }
    public String getDescription() {
        return description;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public HomePagerData(Integer image, String content, String description) {
        this.image = image;
        this.content = content;
        this.description = description;
    }
}
