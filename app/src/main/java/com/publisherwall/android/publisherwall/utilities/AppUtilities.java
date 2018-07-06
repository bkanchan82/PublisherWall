package com.publisherwall.android.publisherwall.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

public class AppUtilities {

    public static final void openWebPage(Context context,String webUrl){
        Uri webpage = Uri.parse(webUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void shareNetwork(Activity context, String networkUrl) {
        String mimeType = "text/plain";
        String title = "Network";

        Intent shareIntent =   ShareCompat.IntentBuilder.from(context)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(networkUrl)
                .getIntent();
        if (shareIntent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(shareIntent);
        }
    }

}
