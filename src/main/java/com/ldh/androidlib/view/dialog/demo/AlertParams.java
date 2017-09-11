package com.ldh.androidlib.view.dialog.demo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ldh on 2017/8/22.
 */

public class AlertParams implements Parcelable {

    public int mIconId = 0;
    public int mIconAttrId = 0;
    public String mTitle;
    public String mMessage;
    public String mPositiveButtonText;
    public String mNegativeButtonText;
    public String mNeutralButtonText;
    public boolean mCancelable;
    public String[] mItems;
    public int mViewLayoutResId;
    public int mViewSpacingLeft;
    public int mViewSpacingTop;
    public int mViewSpacingRight;
    public int mViewSpacingBottom;
    public boolean mViewSpacingSpecified = false;
    public boolean[] mCheckedItems;
    public boolean mIsMultiChoice;
    public boolean mIsSingleChoice;
    public int mCheckedItem = -1;
    public String mLabelColumn;
    public String mIsCheckedColumn;
    public boolean mForceInverseBackground;
    public boolean mRecycleOnMeasure = true;

    public AlertParams() {

    }

    protected AlertParams(Parcel in) {
        mIconId = in.readInt();
        mIconAttrId = in.readInt();
        mTitle = in.readString();
        mMessage = in.readString();
        mPositiveButtonText = in.readString();
        mNegativeButtonText = in.readString();
        mNeutralButtonText = in.readString();
        mCancelable = in.readByte() != 0;
        mItems = in.createStringArray();
        mViewLayoutResId = in.readInt();
        mViewSpacingLeft = in.readInt();
        mViewSpacingTop = in.readInt();
        mViewSpacingRight = in.readInt();
        mViewSpacingBottom = in.readInt();
        mViewSpacingSpecified = in.readByte() != 0;
        mCheckedItems = in.createBooleanArray();
        mIsMultiChoice = in.readByte() != 0;
        mIsSingleChoice = in.readByte() != 0;
        mCheckedItem = in.readInt();
        mLabelColumn = in.readString();
        mIsCheckedColumn = in.readString();
        mForceInverseBackground = in.readByte() != 0;
        mRecycleOnMeasure = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIconId);
        dest.writeInt(mIconAttrId);
        dest.writeString(mTitle);
        dest.writeString(mMessage);
        dest.writeString(mPositiveButtonText);
        dest.writeString(mNegativeButtonText);
        dest.writeString(mNeutralButtonText);
        dest.writeByte((byte) (mCancelable ? 1 : 0));
        dest.writeStringArray(mItems);
        dest.writeInt(mViewLayoutResId);
        dest.writeInt(mViewSpacingLeft);
        dest.writeInt(mViewSpacingTop);
        dest.writeInt(mViewSpacingRight);
        dest.writeInt(mViewSpacingBottom);
        dest.writeByte((byte) (mViewSpacingSpecified ? 1 : 0));
        dest.writeBooleanArray(mCheckedItems);
        dest.writeByte((byte) (mIsMultiChoice ? 1 : 0));
        dest.writeByte((byte) (mIsSingleChoice ? 1 : 0));
        dest.writeInt(mCheckedItem);
        dest.writeString(mLabelColumn);
        dest.writeString(mIsCheckedColumn);
        dest.writeByte((byte) (mForceInverseBackground ? 1 : 0));
        dest.writeByte((byte) (mRecycleOnMeasure ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlertParams> CREATOR = new Creator<AlertParams>() {
        @Override
        public AlertParams createFromParcel(Parcel in) {
            return new AlertParams(in);
        }

        @Override
        public AlertParams[] newArray(int size) {
            return new AlertParams[size];
        }
    };
}
