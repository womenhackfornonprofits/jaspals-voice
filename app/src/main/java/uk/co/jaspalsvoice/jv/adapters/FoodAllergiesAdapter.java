package uk.co.jaspalsvoice.jv.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.AllergiesActivity;
import uk.co.jaspalsvoice.jv.models.FoodAllergies;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class FoodAllergiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FoodAllergies> foodAllergies;
    private Context context;

    public FoodAllergiesAdapter(Context context, ArrayList<FoodAllergies> foodAllergies) {
        this.foodAllergies = foodAllergies;
        this.context = context;
    }

    private class FoodAllergiesViewHolder extends RecyclerView.ViewHolder {

        EditText foodAllergiesEdittext;
        EditText typeText;
        TextView foodAllergiesTextView, typeTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public FoodAllergiesViewHolder(View itemView) {
            super(itemView);
            foodAllergiesEdittext = (EditText) itemView.findViewById(R.id.editFirstField);
            typeText = (EditText) itemView.findViewById(R.id.editSecondField);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            foodAllergiesTextView = (TextView) itemView.findViewById(R.id.dataFirstField);
            typeTextView = (TextView) itemView.findViewById(R.id.dataSecondField);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.common_list_item, parent, false);
        return new FoodAllergiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final FoodAllergies foodAllergy = foodAllergies.get(position);
        if (foodAllergy != null) {
            ((FoodAllergiesViewHolder) holder).foodAllergiesTextView.setText(foodAllergy.foodAllergies);
            ((FoodAllergiesViewHolder) holder).typeTextView.setText(foodAllergy.type);
            ((FoodAllergiesViewHolder) holder).foodAllergiesEdittext.setText(foodAllergy.foodAllergies);
            ((FoodAllergiesViewHolder) holder).typeText.setText(foodAllergy.type);
        } else {
            ((FoodAllergiesViewHolder) holder).foodAllergiesTextView.setText("");
            ((FoodAllergiesViewHolder) holder).typeTextView.setText("");
            ((FoodAllergiesViewHolder) holder).foodAllergiesEdittext.setText("");
            ((FoodAllergiesViewHolder) holder).typeText.setText("");
        }
        ((FoodAllergiesViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((FoodAllergiesViewHolder) holder));
            }
        });

        ((FoodAllergiesViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllergiesActivity)context).setFoodAllergiesId(foodAllergy.getId());
                uptypeData(((FoodAllergiesViewHolder)holder).foodAllergiesEdittext.getText().toString(),
                        ((FoodAllergiesViewHolder)holder).typeText.getText().toString());
                ((FoodAllergiesViewHolder) holder).foodAllergiesTextView.setText(
                        ((FoodAllergiesViewHolder)holder).foodAllergiesEdittext.getText().toString()
                );
                ((FoodAllergiesViewHolder) holder).typeTextView.setText(
                        ((FoodAllergiesViewHolder)holder).typeText.getText().toString()
                );
                hideEditMode(((FoodAllergiesViewHolder)holder));
            }
        });

        ((FoodAllergiesViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode((FoodAllergiesViewHolder) holder);
            }
        });
        hideEditMode((FoodAllergiesViewHolder)holder);
    }

    private void uptypeData(String foodAllergies, String type) {
        new Save().execute(foodAllergies, type);
    }

    private void showEditMode(FoodAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.foodAllergiesTextView.setVisibility(View.GONE);
        holder.typeTextView.setVisibility(View.GONE);
        holder.foodAllergiesEdittext.setVisibility(View.VISIBLE);
        holder.typeText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(FoodAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.foodAllergiesTextView.setVisibility(View.VISIBLE);
        holder.typeTextView.setVisibility(View.VISIBLE);
        holder.foodAllergiesEdittext.setVisibility(View.GONE);
        holder.typeText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return foodAllergies.size();
    }

    private class Save extends AsyncTask<String, Void, FoodAllergies> {
        @Override
        protected FoodAllergies doInBackground(String... params) {
            List<FoodAllergies> foodAllergies = new ArrayList<>();
            FoodAllergies foodAllergy = new FoodAllergies();
            foodAllergy.setFoodAllergies(params[0]);
            foodAllergy.setType(params[1]);
            foodAllergies.add(foodAllergy);
            ((JvApplication) context.getApplicationContext()).getDbHelper().
                    insertOrReplaceFoodAllergies(foodAllergies,true,
                    ((AllergiesActivity)context).getFoodAllergiesId());
            return foodAllergy;
        }

        @Override
        protected void onPostExecute(FoodAllergies foodAllergies) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new FoodAllergiesRetrieve().execute();
    }

    private class FoodAllergiesRetrieve extends AsyncTask<Void, Void, List<FoodAllergies>> {
        @Override
        protected List<FoodAllergies> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().
                    readAllFoodAllergies();
        }

        @Override
        protected void onPostExecute(List<FoodAllergies> foodAllergiesList) {
            foodAllergies.clear();
            foodAllergies.addAll(foodAllergiesList);
            notifyDataSetChanged();
        }
    }
}
