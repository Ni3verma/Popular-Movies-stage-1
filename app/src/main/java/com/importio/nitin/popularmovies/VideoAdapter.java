package com.importio.nitin.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoAdapter extends ArrayAdapter<Video> {
    private Context context;
    private Video videos[];

    public VideoAdapter(@NonNull Context context, @NonNull Video[] videos) {
        super(context, 0, videos);
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list_video, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView site = convertView.findViewById(R.id.site);

        final Video v = getItem(position);
        name.setText(v.getName());
        site.setText(v.getSite());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://www.youtube.com/watch?v=gFaYbSGjED0
                String key = v.getKey();

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.youtube.com")
                        .appendPath("watch")
                        .appendQueryParameter("v", key);

                URL url = null;
                try {
                    url = new URL(builder.build().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }
}
