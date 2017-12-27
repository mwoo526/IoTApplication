package samplepush2.androidtown.org.iotapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import samplepush2.androidtown.org.iotapplication.R;
import samplepush2.androidtown.org.iotapplication.model.Trash;

/**
 * Created by MinWoo on 2017-11-23.
 */

public class TrashAdapter extends ArrayAdapter<Trash>{

    Activity activity;
    int resource;

    public TrashAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Trash> objects) {
        super(context, resource, objects);

        activity=(Activity) context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView==null){
            itemView=this.activity.getLayoutInflater().inflate(this.resource,null);
        }
        Trash trash = getItem(position);

        if(trash!=null){
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextView textView1=(TextView)itemView.findViewById(R.id.textView1);
            TextView textView2=(TextView)itemView.findViewById(R.id.textView2);

            imageView.setImageResource(trash.getImage());
            textView1.setText(trash.getLocation());
            textView2.setText(trash.getAmount());
        }
        return itemView;
    }
}
