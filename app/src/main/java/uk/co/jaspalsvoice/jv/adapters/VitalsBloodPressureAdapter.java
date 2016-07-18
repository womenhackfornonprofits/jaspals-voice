package uk.co.jaspalsvoice.jv.adapters;

import android.content.Context;
import android.media.Image;
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

import uk.co.jaspalsvoice.jv.DateUtils;
import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class VitalsBloodPressureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VitalsBloodPressure> bloodPressures;
    private Context context;

    public VitalsBloodPressureAdapter(Context context, ArrayList<VitalsBloodPressure> bloodPressures) {
        this.bloodPressures = bloodPressures;
        this.context = context;
    }

    private class BloodPressureViewHolder extends RecyclerView.ViewHolder {

        EditText bloodPressureEdittext;
        EditText dateText;
        TextView bloodPressureTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public BloodPressureViewHolder(View itemView) {
            super(itemView);
            bloodPressureEdittext = (EditText) itemView.findViewById(R.id.bloodPressureEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            bloodPressureTextView = (TextView) itemView.findViewById(R.id.bloodPressureTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_vitals_blood_pressure, parent, false);
        return new BloodPressureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final VitalsBloodPressure bloodPressure = bloodPressures.get(position);
        if (bloodPressure != null) {
            ((BloodPressureViewHolder) holder).bloodPressureTextView.setText(bloodPressure.bloodPressure);
            ((BloodPressureViewHolder) holder).dateTextView.setText(bloodPressure.date);
            ((BloodPressureViewHolder) holder).bloodPressureEdittext.setText(bloodPressure.bloodPressure);
            ((BloodPressureViewHolder) holder).dateText.setText(bloodPressure.date);
        } else {
            ((BloodPressureViewHolder) holder).bloodPressureTextView.setText("");
            ((BloodPressureViewHolder) holder).dateTextView.setText("");
            ((BloodPressureViewHolder) holder).bloodPressureEdittext.setText("");
            ((BloodPressureViewHolder) holder).dateText.setText("");
        }
        ((BloodPressureViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((BloodPressureViewHolder) holder));
            }
        });

        ((BloodPressureViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VitalsActivity)context).setBloodPressureId(bloodPressure.getId());
                updateData(((BloodPressureViewHolder)holder).bloodPressureEdittext.getText().toString(),
                        ((BloodPressureViewHolder)holder).dateText.getText().toString());
                ((BloodPressureViewHolder) holder).bloodPressureTextView.setText(
                        ((BloodPressureViewHolder)holder).bloodPressureEdittext.getText().toString()
                );
                ((BloodPressureViewHolder) holder).dateTextView.setText(
                        ((BloodPressureViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((BloodPressureViewHolder)holder));
            }
        });

        ((BloodPressureViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((BloodPressureViewHolder) holder));
            }
        });

        ((BloodPressureViewHolder)holder).dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePicker(((BloodPressureViewHolder)holder).dateText, ((VitalsActivity)context));
            }
        });

    }

    private void updateData(String bloodPressure, String date) {
        new Save().execute(bloodPressure, date);
    }

    private void showEditMode(BloodPressureViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.bloodPressureTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.bloodPressureEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(BloodPressureViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.bloodPressureTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.bloodPressureEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return bloodPressures.size();
    }

    private class Save extends AsyncTask<String, Void, VitalsBloodPressure> {
        @Override
        protected VitalsBloodPressure doInBackground(String... params) {
            List<VitalsBloodPressure> bloodPressures = new ArrayList<>();
            VitalsBloodPressure bloodPressure = new VitalsBloodPressure();
            bloodPressure.setBloodPressure(params[0]);
            bloodPressure.setDate(params[1]);
            bloodPressures.add(bloodPressure);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceBloodPressure(bloodPressures,true,
                    ((VitalsActivity)context).getBloodPressureId());
            return bloodPressure;
        }

        @Override
        protected void onPostExecute(VitalsBloodPressure bloodPressure) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new BloodPressure().execute();
    }

    private class BloodPressure extends AsyncTask<Void, Void, List<VitalsBloodPressure>> {
        @Override
        protected List<VitalsBloodPressure> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllBloodPressures();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodPressure> bloodPressureList) {
            bloodPressures.clear();
            bloodPressures.addAll(bloodPressureList);
            notifyDataSetChanged();
        }
    }
}
