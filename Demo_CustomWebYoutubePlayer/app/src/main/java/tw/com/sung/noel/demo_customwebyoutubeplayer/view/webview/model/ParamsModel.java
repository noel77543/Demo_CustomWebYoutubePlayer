package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.model;

public class ParamsModel {
    private String youtubeId;
    private String bgColor;
    private String autoPlay;
    private String autoHide;
    private String rel;
    private String showInfo;
    private String enableJSApi;
    private String disableKB;
    private String ccLangPref;
    private String controls;
    private String fs;


    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean isAutoPlay) {
        this.autoPlay =  convertBooleanToString(isAutoPlay);
    }

    public String getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(boolean isAutoHide) {
        this.autoHide =  convertBooleanToString(isAutoHide);
    }

    public String getRel() {
        return rel;
    }

    public void setRel(boolean isRel) {
        this.rel =  convertBooleanToString(isRel);
    }

    public String getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean isShowInfo) {
        this.showInfo =  convertBooleanToString(isShowInfo);
    }

    public String getEnableJSApi() {
        return enableJSApi;
    }

    public void setEnableJSApi(boolean isEnableJSApi) {
        this.enableJSApi =  convertBooleanToString(isEnableJSApi);
    }

    public String getDisableKB() {
        return disableKB;
    }

    public void setDisableKB(boolean isDisableKB) {
        this.disableKB =  convertBooleanToString(isDisableKB);
    }

    public String getCcLangPref() {
        return ccLangPref;
    }

    public void setCcLangPref(String ccLangPref) {
        this.ccLangPref = ccLangPref;
    }

    public String getControls() {
        return controls;
    }

    public void setControls(boolean isControls) {
        this.controls =  convertBooleanToString(isControls);
    }

    public String getFs() {
        return fs;
    }

    public void setFs(boolean isFs) {
        this.fs = convertBooleanToString(isFs);
    }


    //------

    /***
     *  true / false = 1 / 0
     * @param isAble
     * @return
     */
    private String convertBooleanToString(boolean isAble) {
        return String.valueOf(isAble ? 1 : 0);
    }
}
