package xyz.vedant.diagnal.data;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import xyz.vedant.diagnal.R;
import xyz.vedant.diagnal.adapter.ItemAdapter;
import xyz.vedant.diagnal.lib.EndlessRecyclerViewScrollListener;
import xyz.vedant.diagnal.prototype.ItemPrototype;

public class RecyclerData {
    Context context;
    RecyclerView recyclerView;

    public static ArrayList<ItemPrototype> itemPrototypes = new ArrayList<>();

    ProgressBar progressBar;
    private EndlessRecyclerViewScrollListener scrollListener;

    // max data from api
    int max_data;
    //track to see the api
    public static HashMap<Integer, Boolean> map = new HashMap<>();


    public int getMax_data() {
        return max_data;
    }

    public void setMax_data(int max_data) {
        this.max_data = max_data;
    }

    public RecyclerData(final Context context, RecyclerView recyclerView, ProgressBar progressBar) {

        //set the values
        this.context = context;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;

        itemPrototypes.clear();
        setMax_data(20);

        //make api1,api2,api3 to default false
        map.put(0, false);
        map.put(1, false);
        map.put(2, false);
        //load the data
        loadData(0);

        //3 for portrait, 5 for landscape grid columns
        if (itemPrototypes.size() > 0) {
            GridLayoutManager gridLayoutManager;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                gridLayoutManager = new GridLayoutManager(context, 3);
            } else {
                gridLayoutManager = new GridLayoutManager(context, 5);
            }


            recyclerView.setLayoutManager(gridLayoutManager);
            final ItemAdapter itemAdapter = new ItemAdapter(context, itemPrototypes);
            recyclerView.setAdapter(itemAdapter);
            //declare the listener, and loadData when maxCapping is not reached
            scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    final int curSize = itemAdapter.getItemCount();
                    if (curSize < getMax_data()) {
                        loadData(page);
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                itemAdapter.notifyItemRangeInserted(curSize, itemPrototypes.size());
                                //itemAdapter.notifyDataSetChanged();
                                Toast.makeText(context, "Inserted: " + itemPrototypes.size(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            };
            // Adds the scroll listener to RecyclerView
            recyclerView.addOnScrollListener(scrollListener);


            progressBar.setVisibility(View.GONE);

        } else {
            Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }


    }

    //api calls method
    public void loadData(int page) {
        String[] file_name = {"page1_data.json", "page2_data.json", "page3_data.json"};

        //check if the data is already loaded
        if (page > 2 || itemPrototypes.size() > 50 || map.get(page)) {
            return;
        }
        String str = loadJSONFromAsset(file_name[page]);
        map.put(page, true);
        try {
            JSONObject obj = new JSONObject(str);
            JSONObject page_obj = obj.getJSONObject("page");

            //updating the max amount val
            String max_count_str = page_obj.getString("total-content-items");
            setMax_data(Integer.parseInt(max_count_str));
            JSONObject content_obj = page_obj.getJSONObject("content-items");

            JSONArray item_arr = content_obj.getJSONArray("content");

            for (int i = 0; i < item_arr.length(); i++) {
                JSONObject item_obj = item_arr.getJSONObject(i);
                String name_item = item_obj.getString("name");
                String image_item = item_obj.getString("poster-image");
                ItemPrototype itemPrototype = new ItemPrototype(formatTitle(name_item), getResFromName(image_item));
                itemPrototypes.add(itemPrototype);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //load json file from asset folder

    public String loadJSONFromAsset(String json_file_name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(json_file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //resource image  file
    public int getResFromName(String resName) {
        int res;
        int[] arr = {R.drawable.poster1, R.drawable.poster2, R.drawable.poster3, R.drawable.poster4,
                R.drawable.poster5, R.drawable.poster6, R.drawable.poster7, R.drawable.poster8, R.drawable.poster9};
        switch (resName) {
            case "poster1.jpg":
                res = arr[0];
                break;

            case "poster2.jpg":
                res = arr[1];
                break;

            case "poster3.jpg":
                res = arr[2];
                break;

            case "poster4.jpg":
                res = arr[3];
                break;

            case "poster5.jpg":
                res = arr[4];
                break;

            case "poster6.jpg":
                res = arr[5];
                break;

            case "poster7.jpg":
                res = arr[6];
                break;

            case "poster8.jpg":
                res = arr[7];
                break;

            case "poster9.jpg":
                res = arr[8];
                break;

            default:
                //page 3 edge case - missing image
                res = R.drawable.placeholder_for_missing_posters;
        }

        return res;
    }


    public String formatTitle(String title) {
        //page 3 edge case -long title
        if (title.length() > 13) {
            return title.substring(0, 10) + "...";
        }
        return title;
    }


}
