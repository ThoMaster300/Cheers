package at.technikum_wien.cheers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DominikRossmanith on 03.01.2018.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>{
    ArrayList<String> data;

    public PlayerAdapter(ArrayList<String> data){
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvShowName;
        Button buttonDelete;
        public ViewHolder(View itemView) {
            super(itemView);

            tvShowName = (TextView)itemView.findViewById(R.id.showPlayerName_tv);
            buttonDelete = (Button)itemView.findViewById(R.id.deletePlayer_button);
            buttonDelete.setOnClickListener(this);
        }

        private int getPositionOfItem(String pName){
            for(String item : data)
            {
                if(item.equals(pName)) {
                    return data.indexOf(item);
                }
            }
            return -1;
        }

        @Override
        public void onClick(View view) {
            int pos = getPositionOfItem(tvShowName.getText().toString());
            data.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,data.size());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_entry_layout, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvShowName.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
