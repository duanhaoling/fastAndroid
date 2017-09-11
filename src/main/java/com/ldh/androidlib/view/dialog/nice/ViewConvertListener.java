package com.ldh.androidlib.view.dialog.nice;

import java.io.Serializable;

/**
 * Created by ldh on 2017/9/8.
 */

public interface ViewConvertListener extends Serializable {
    long serialVersionUID = System.currentTimeMillis();

    void convertView(DialogViewHolder holder, BaseNiceDialog dialog);
}
