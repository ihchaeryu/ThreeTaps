package com.example.threetaps;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MusicModal implements Parcelable {

    // variables
    private Uri contentUri;
    private Uri albumArtUri;
    private String fileName;
    private String artistName;

    // constructor
    public MusicModal(Uri contentUri, Uri albumArtUri,String fileName, String artistName) {
        this.contentUri = contentUri;
        this.albumArtUri = albumArtUri;
        this.fileName = fileName;
        this.artistName = artistName;
    }

    // getter

    public Uri getContentUri() {
        return contentUri;
    }
    public Uri getAlbumArtUri() { return albumArtUri; }

    public String getFileName() {
        return fileName;
    }

    public String getArtistName() {
        return artistName;
    }

    // setter

    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
    }

    public void setAlbumArtUri(Uri albumArtUri) {
        this.albumArtUri = albumArtUri;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    // Parcelable implementation
    protected MusicModal(Parcel in) {
        contentUri = in.readParcelable(Uri.class.getClassLoader());
        albumArtUri = in.readParcelable(Uri.class.getClassLoader());
        fileName = in.readString();
        artistName = in.readString();
    }

    public static final Creator<MusicModal> CREATOR = new Creator<MusicModal>() {
        @Override
        public MusicModal createFromParcel(Parcel in) {
            return new MusicModal(in);
        }

        @Override
        public MusicModal[] newArray(int size) {
            return new MusicModal[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(contentUri, flags);
        dest.writeParcelable(albumArtUri, flags);
        dest.writeString(fileName);
        dest.writeString(artistName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
