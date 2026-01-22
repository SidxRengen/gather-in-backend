package com.example.getherinjava.dto.request;

public class SettingRequest {
    public String blur;
    public int opacity;

    public String getBlur() {
        return blur;
    }

    public void setBlur(String blur) {
        this.blur = blur;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public SettingRequest(String blur, int opacity) {
        this.blur = blur;
        this.opacity = opacity;
    }
}
