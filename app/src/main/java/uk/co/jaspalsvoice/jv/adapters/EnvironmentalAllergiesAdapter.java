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
import uk.co.jaspalsvoice.jv.models.EnvironmentalAllergies;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class EnvironmentalAllergiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EnvironmentalAllergies> environmentalAllergies;
    private Context context;

    public EnvironmentalAllergiesAdapter(Context context, ArrayList<EnvironmentalAllergies> environmentalAllergies) {
        this.environmentalAllergies = environmentalAllergies;
        this.context = context;
    }

    private class EnvironmentalAllergiesViewHolder extends RecyclerView.ViewHolder {

        EditText environmentalAllergiesEdittext;
        EditText typeText;
        TextView environmentalAllergiesTextView, typeTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public EnvironmentalAllergiesViewHolder(View itemView) {
            super(itemView);
            environmentalAllergiesEdittext = (EditText) itemView.findViewById(R.id.editFirstField);
            typeText = (EditText) itemView.findViewById(R.id.editSecondField);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            environmentalAllergiesTextView = (TextView) itemView.findViewById(R.id.dataFirstField);
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
        return new EnvironmentalAllergiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final EnvironmentalAllergies environmentalAllergy = environmentalAllergies.get(position);
        if (environmentalAllergy != null) {
            ((EnvironmentalAllergiesViewHolder) holder).environmentalAllergiesTextView.setText(environmentalAllergy.environmentalAllergies);
            ((EnvironmentalAllergiesViewHolder) holder).typeTextView.setText(environmentalAllergy.type);
            ((EnvironmentalAllergiesViewHolder) holder).environmentalAllergiesEdittext.setText(environmentalAllergy.environmentalAllergies);
            ((EnvironmentalAllergiesViewHolder) holder).typeText.setText(environmentalAllergy.type);
        } else {
            ((EnvironmentalAllergiesViewHolder) holder).environmentalAllergiesTextView.setText("");
            ((EnvironmentalAllergiesViewHolder) holder).typeTextView.setText("");
            ((EnvironmentalAllergiesViewHolder) holder).environmentalAllergiesEdittext.setText("");
            ((EnvironmentalAllergiesViewHolder) holder).typeText.setText("");
        }
        ((EnvironmentalAllergiesViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((EnvironmentalAllergiesViewHolder) holder));
            }
        });

        ((EnvironmentalAllergiesViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllergiesActivity)context).setEnvironmentalAllergiesId(environmentalAllergy.getId());
                uptypeData(((EnvironmentalAllergiesViewHolder)holder).environmentalAllergiesEdittext.getText().toString(),
                        ((EnvironmentalAllergiesViewHolder)holder).typeText.getText().toString());
                ((EnvironmentalAllergiesViewHolder) holder).environmentalAllergiesTextView.setText(
                        ((EnvironmentalAllergiesViewHolder)holder).environmentalAllergiesEdittext.getText().toString()
                );
                ((EnvironmentalAllergiesViewHolder) holder).typeTextView.setText(
                        ((EnvironmentalAllergiesViewHolder)holder).typeText.getText().toString()
                );
                hideEditMode(((EnvironmentalAllergiesViewHolder)holder));
            }
        });

        ((EnvironmentalAllergiesViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((EnvironmentalAllergiesViewHolder) holder));
            }
        });
        hideEditMode(((EnvironmentalAllergiesViewHolder)holder));
    }

    private void uptypeData(String environmentalAllergies, String type) {
        new Save().execute(environmentalAllergies, type);
    }

    private void showEditMode(EnvironmentalAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.environmentalAllergiesTextView.setVisibility(View.GONE);
        holder.typeTextView.setVisibility(View.GONE);
        holder.environmentalAllergiesEdittext.setVisibility(View.VISIBLE);
        holder.typeText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(EnvironmentalAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.environmentalAllergiesTextView.setVisibility(View.VISIBLE);
        holder.typeTextView.setVisibility(View.VISIBLE);
        holder.environmentalAllergiesEdittext.setVisibility(View.GONE);
        holder.typeText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return environmentalAllergies.size();
    }

    private class Save extends AsyncTask<String, Void, EnvironmentalAllergies> {
        @Override
        protected EnvironmentalAllergies doInBackground(String... params) {
            List<EnvironmentalAllergies> environmentalAllergies = new ArrayList<>();
            EnvironmentalAllergies environmentalAllergy = new EnvironmentalAllergies();
            environmentalAllergy.setEnvironmentalAllergies(params[0]);
            environmentalAllergy.setType(params[1]);
            environmentalAllergies.add(environmentalAllergy);
            ((JvApplication) context.getApplicationContext()).getDbHelper().
                    insertOrReplaceEnvironmentalAllergies(environmentalAllergies,true,
                    ((AllergiesActivity)context).getEnvironmentalAllergiesId());
            return environmentalAllergy;
        }

        @Override
        protected void onPostExecute(EnvironmentalAllergies environmentalAllergies) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new EnvironmentalAllergiesRetrieve().execute();
    }

    private class EnvironmentalAllergiesRetrieve extends AsyncTask<Void, Void, List<EnvironmentalAllergies>> {
        @Override
        protected List<EnvironmentalAllergies> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().
                    readAllEnvironmentalAllergies();
        }

        @Override
        protected void onPostExecute(List<EnvironmentalAllergies> environmentalAllergiesList) {
            environmentalAllergies.clear();
            environmentalAllergies.addAll(environmentalAllergiesList);
            notifyDataSetChanged();
        }
    }
}
