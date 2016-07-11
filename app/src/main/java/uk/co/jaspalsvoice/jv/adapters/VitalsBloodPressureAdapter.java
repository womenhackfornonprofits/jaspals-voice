package uk.co.jaspalsvoice.jv.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.co.jaspalsvoice.jv.R;
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
        ImageView editButton;

        public BloodPressureViewHolder(View itemView) {
            super(itemView);
            bloodPressureEdittext = (EditText) itemView.findViewById(R.id.bloodPressureEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_vitals_blood_pressure, parent, false);
        return new BloodPressureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VitalsBloodPressure bloodPressure = bloodPressures.get(position);
        if (bloodPressure != null) {
            ((BloodPressureViewHolder) holder).bloodPressureEdittext.setText(bloodPressure.bloodPressure);
            ((BloodPressureViewHolder) holder).dateText.setText(bloodPressure.date);
        } else {
            ((BloodPressureViewHolder) holder).bloodPressureEdittext.setText("");
            ((BloodPressureViewHolder) holder).dateText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return bloodPressures.size();
    }
}
