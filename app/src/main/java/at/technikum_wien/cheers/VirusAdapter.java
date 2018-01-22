package at.technikum_wien.cheers;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DominikRossmanith on 22.01.2018.
 */

public class VirusAdapter extends RecyclerView.Adapter<VirusAdapter.ViewHolder>{
    ArrayList<String> data;

    public VirusAdapter(String[] data){
        this.data = new ArrayList<String>();
        for(int i = 0; i < data.length; i++){
            this.data.add(data[i]);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvShowText, tvShowLength;
        public ViewHolder(View itemView) {
            super(itemView);

            tvShowText = (TextView)itemView.findViewById(R.id.showVirusText_tv);
            tvShowLength = (TextView)itemView.findViewById(R.id.showVirusLength_tv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.virus_show_layout, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int index = data.get(position).indexOf("|");
        String tempText1 = ""; //Text
        String tempText2 = ""; //Length
        if(index!=-1){
            tempText1 = data.get(position).substring(0,index);
            tempText2 = data.get(position).substring(index+1,data.get(position).length());
        }
        holder.tvShowText.setText(tempText1);
        holder.tvShowLength.setText(tempText2);
        if (position == 0){
            holder.tvShowText.setBackgroundColor(Color.parseColor("#65a08b"));
            holder.tvShowLength.setBackgroundColor(Color.parseColor("#65a08b"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
