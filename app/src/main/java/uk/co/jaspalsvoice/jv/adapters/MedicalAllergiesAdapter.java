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
import uk.co.jaspalsvoice.jv.models.MedicalAllergies;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class MedicalAllergiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MedicalAllergies> medicalAllergies;
    private Context context;

    public MedicalAllergiesAdapter(Context context, ArrayList<MedicalAllergies> medicalAllergies) {
        this.medicalAllergies = medicalAllergies;
        this.context = context;
    }

    private class MedicalAllergiesViewHolder extends RecyclerView.ViewHolder {

        EditText medicalAllergiesEdittext;
        EditText typeText;
        TextView medicalAllergiesTextView, typeTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public MedicalAllergiesViewHolder(View itemView) {
            super(itemView);
            medicalAllergiesEdittext = (EditText) itemView.findViewById(R.id.editFirstField);
            typeText = (EditText) itemView.findViewById(R.id.editSecondField);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            medicalAllergiesTextView = (TextView) itemView.findViewById(R.id.dataFirstField);
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
        return new MedicalAllergiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MedicalAllergies medicalAllergy = medicalAllergies.get(position);
        if (medicalAllergy != null) {
            ((MedicalAllergiesViewHolder) holder).medicalAllergiesTextView.setText(medicalAllergy.medicalAllergies);
            ((MedicalAllergiesViewHolder) holder).typeTextView.setText(medicalAllergy.type);
            ((MedicalAllergiesViewHolder) holder).medicalAllergiesEdittext.setText(medicalAllergy.medicalAllergies);
            ((MedicalAllergiesViewHolder) holder).typeText.setText(medicalAllergy.type);
        } else {
            ((MedicalAllergiesViewHolder) holder).medicalAllergiesTextView.setText("");
            ((MedicalAllergiesViewHolder) holder).typeTextView.setText("");
            ((MedicalAllergiesViewHolder) holder).medicalAllergiesEdittext.setText("");
            ((MedicalAllergiesViewHolder) holder).typeText.setText("");
        }
        ((MedicalAllergiesViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((MedicalAllergiesViewHolder) holder));
            }
        });

        ((MedicalAllergiesViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllergiesActivity)context).setMedicalAllergiesId(medicalAllergy.getId());
                uptypeData(((MedicalAllergiesViewHolder)holder).medicalAllergiesEdittext.getText().toString(),
                        ((MedicalAllergiesViewHolder)holder).typeText.getText().toString());
                ((MedicalAllergiesViewHolder) holder).medicalAllergiesTextView.setText(
                        ((MedicalAllergiesViewHolder)holder).medicalAllergiesEdittext.getText().toString()
                );
                ((MedicalAllergiesViewHolder) holder).typeTextView.setText(
                        ((MedicalAllergiesViewHolder)holder).typeText.getText().toString()
                );
                hideEditMode(((MedicalAllergiesViewHolder)holder));
            }
        });

        ((MedicalAllergiesViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((MedicalAllergiesViewHolder) holder));
            }
        });
        hideEditMode((MedicalAllergiesViewHolder)holder);
    }

    private void uptypeData(String medicalAllergies, String type) {
        new Save().execute(medicalAllergies, type);
    }

    private void showEditMode(MedicalAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.medicalAllergiesTextView.setVisibility(View.GONE);
        holder.typeTextView.setVisibility(View.GONE);
        holder.medicalAllergiesEdittext.setVisibility(View.VISIBLE);
        holder.typeText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(MedicalAllergiesViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.medicalAllergiesTextView.setVisibility(View.VISIBLE);
        holder.typeTextView.setVisibility(View.VISIBLE);
        holder.medicalAllergiesEdittext.setVisibility(View.GONE);
        holder.typeText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return medicalAllergies.size();
    }

    private class Save extends AsyncTask<String, Void, MedicalAllergies> {
        @Override
        protected MedicalAllergies doInBackground(String... params) {
            List<MedicalAllergies> medicalAllergies = new ArrayList<>();
            MedicalAllergies medicalAllergy = new MedicalAllergies();
            medicalAllergy.setMedicalAllergies(params[0]);
            medicalAllergy.setType(params[1]);
            medicalAllergies.add(medicalAllergy);
            ((JvApplication) context.getApplicationContext()).getDbHelper().
                    insertOrReplaceMedicalAllergies(medicalAllergies,true,
                    ((AllergiesActivity)context).getMedicalAllergiesId());
            return medicalAllergy;
        }

        @Override
        protected void onPostExecute(MedicalAllergies medicalAllergies) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new MedicalAllergiesRetrieve().execute();
    }

    private class MedicalAllergiesRetrieve extends AsyncTask<Void, Void, List<MedicalAllergies>> {
        @Override
        protected List<MedicalAllergies> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().
                    readAllMedicalAllergies();
        }

        @Override
        protected void onPostExecute(List<MedicalAllergies> medicalAllergiesList) {
            medicalAllergies.clear();
            medicalAllergies.addAll(medicalAllergiesList);
            notifyDataSetChanged();
        }
    }
}
