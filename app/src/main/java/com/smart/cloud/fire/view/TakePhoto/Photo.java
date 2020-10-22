package com.smart.cloud.fire.view.TakePhoto;

public class Photo {
    String name;
    String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Photo(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
