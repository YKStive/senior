package com.youloft.widgets;

import android.os.Parcel;
import android.widget.RemoteViews;

import com.youloft.trans.I18N;

/**
 * Created by javen on 14-8-18.
 */
public class I18NRemoteView extends RemoteViews {
    /**
     * Create a new RemoteViews object that will display the views contained
     * in the specified layout file.
     *
     * @param packageName Name of the package that contains the layout resource
     * @param layoutId    The id of the layout resource
     */
    public I18NRemoteView(String packageName, int layoutId) {
        super(packageName, layoutId);
    }

    /**
     * Reads a RemoteViews object from a parcel.
     *
     * @param parcel
     */
    public I18NRemoteView(Parcel parcel) {
        super(parcel);
    }

    @Override
    public void setTextViewText(int viewId, CharSequence text) {
        super.setTextViewText(viewId, I18N.convert(text));
    }
}
