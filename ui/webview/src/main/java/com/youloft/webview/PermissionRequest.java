package com.youloft.webview;

public abstract class PermissionRequest {
    public static final int TYPE_RECORD = 1;
    public static final int TYPE_CAMERA = 2;
    public static final int TYPE_STORGE = 3;
    public String[] permission;
    public String tipText;
    public int type;
    public String denyText;

    public abstract void grant();
    public abstract void deny();
}
