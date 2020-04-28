package xyz.vedant.diagnal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.vedant.diagnal.R;
import xyz.vedant.diagnal.prototype.ItemPrototype;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    Context context;
    public ArrayList<ItemPrototype> itemPrototypes; //api data original
    public ArrayList<ItemPrototype> filteredList; // filtered after search query

    public ItemAdapter(Context context, ArrayList<ItemPrototype> itemPrototypes) {
        this.context = context;
        this.filteredList = itemPrototypes;  //initialized to original data
        this.itemPrototypes = itemPrototypes;
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        //filtered list to be passed on the holder
        ItemPrototype itemPrototype = filteredList.get(position);

        holder.tv_title.setText(itemPrototype.getTitle());
        holder.iv_image.setImageResource(itemPrototype.getRes());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                //search only, query is 3 or more chars long
                if (charString.isEmpty() || charString.length() < 3) {
                    filteredList = itemPrototypes;
                } else {
                    filteredList = itemPrototypes;
                    ArrayList<ItemPrototype> curr_result = new ArrayList<>();
                    curr_result.clear();
                    for (ItemPrototype itemPrototype : itemPrototypes) {
                        //checking the pattern is contained in the title of movies
                        if (itemPrototype.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            curr_result.add(itemPrototype);
                        }
                    }


                    filteredList = curr_result;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (charSequence.length() == 0 || charSequence.length() < 3) {
                    filteredList = itemPrototypes;
                } else {
                    //pass the data to filter list
                    filteredList = (ArrayList<ItemPrototype>) filterResults.values;
                    Toast.makeText(context, "" + filteredList.size() + " movies found.", Toast.LENGTH_SHORT).show();
                }

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        //view holder layout declaration
        TextView tv_title;
        ImageView iv_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }


}
