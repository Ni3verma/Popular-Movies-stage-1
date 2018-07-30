package com.importio.nitin.popularmovies.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.importio.nitin.popularmovies.ModalClasses.Video;
import com.importio.nitin.popularmovies.R;

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
                String key = v.getKey();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
                try {
                    if (appIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(appIntent);
                    }
                } catch (ActivityNotFoundException e) {
                    context.startActivity(webIntent);
                }
            }
        });

        return convertView;
    }
}
