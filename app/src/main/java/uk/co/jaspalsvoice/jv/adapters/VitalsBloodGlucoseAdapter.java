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

import uk.co.jaspalsvoice.jv.DateUtils;
import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.models.VitalsBloodGlucose;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class VitalsBloodGlucoseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VitalsBloodGlucose> bloodGlucoses;
    private Context context;

    public VitalsBloodGlucoseAdapter(Context context, ArrayList<VitalsBloodGlucose> bloodGlucoses) {
        this.bloodGlucoses = bloodGlucoses;
        this.context = context;
    }

    private class BloodGlucoseViewHolder extends RecyclerView.ViewHolder {

        EditText bloodGlucoseEdittext;
        EditText dateText;
        TextView bloodGlucoseTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public BloodGlucoseViewHolder(View itemView) {
            super(itemView);
            bloodGlucoseEdittext = (EditText) itemView.findViewById(R.id.editFirstField);
            dateText = (EditText) itemView.findViewById(R.id.editSecondField);
            dateText.setFocusable(false);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            bloodGlucoseTextView = (TextView) itemView.findViewById(R.id.dataFirstField);
            dateTextView = (TextView) itemView.findViewById(R.id.dataSecondField);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.common_list_item, parent, false);
        return new BloodGlucoseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final VitalsBloodGlucose bloodGlucose = bloodGlucoses.get(position);
        if (bloodGlucose != null) {
            ((BloodGlucoseViewHolder) holder).bloodGlucoseTextView.setText(bloodGlucose.bloodGlucose);
            ((BloodGlucoseViewHolder) holder).dateTextView.setText(bloodGlucose.date);
            ((BloodGlucoseViewHolder) holder).bloodGlucoseEdittext.setText(bloodGlucose.bloodGlucose);
            ((BloodGlucoseViewHolder) holder).dateText.setText(bloodGlucose.date);
        } else {
            ((BloodGlucoseViewHolder) holder).bloodGlucoseTextView.setText("");
            ((BloodGlucoseViewHolder) holder).dateTextView.setText("");
            ((BloodGlucoseViewHolder) holder).bloodGlucoseEdittext.setText("");
            ((BloodGlucoseViewHolder) holder).dateText.setText("");
        }
        ((BloodGlucoseViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((BloodGlucoseViewHolder) holder));
            }
        });

        ((BloodGlucoseViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VitalsActivity)context).setBloodGlucoseId(bloodGlucose.getId());
                updateData(((BloodGlucoseViewHolder)holder).bloodGlucoseEdittext.getText().toString(),
                        ((BloodGlucoseViewHolder)holder).dateText.getText().toString());
                ((BloodGlucoseViewHolder) holder).bloodGlucoseTextView.setText(
                        ((BloodGlucoseViewHolder)holder).bloodGlucoseEdittext.getText().toString()
                );
                ((BloodGlucoseViewHolder) holder).dateTextView.setText(
                        ((BloodGlucoseViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((BloodGlucoseViewHolder)holder));
            }
        });

        ((BloodGlucoseViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((BloodGlucoseViewHolder) holder));
            }
        });

        ((BloodGlucoseViewHolder)holder).dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePicker(((BloodGlucoseViewHolder)holder).dateText, ((VitalsActivity)context));
            }
        });
        hideEditMode(((BloodGlucoseViewHolder)holder));
    }

    private void updateData(String bloodGlucose, String date) {
        new Save().execute(bloodGlucose, date);
    }

    private void showEditMode(BloodGlucoseViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.bloodGlucoseTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.bloodGlucoseEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(BloodGlucoseViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.bloodGlucoseTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.bloodGlucoseEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return bloodGlucoses.size();
    }

    private class Save extends AsyncTask<String, Void, VitalsBloodGlucose> {
        @Override
        protected VitalsBloodGlucose doInBackground(String... params) {
            List<VitalsBloodGlucose> bloodGlucoses = new ArrayList<>();
            VitalsBloodGlucose bloodGlucose = new VitalsBloodGlucose();
            bloodGlucose.setBloodGlucose(params[0]);
            bloodGlucose.setDate(params[1]);
            bloodGlucoses.add(bloodGlucose);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceBloodGlucose(bloodGlucoses,true,
                    ((VitalsActivity)context).getBloodGlucoseId());
            return bloodGlucose;
        }

        @Override
        protected void onPostExecute(VitalsBloodGlucose bloodGlucose) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new BloodGlucose().execute();
    }

    private class BloodGlucose extends AsyncTask<Void, Void, List<VitalsBloodGlucose>> {
        @Override
        protected List<VitalsBloodGlucose> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllBloodGlucoses();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodGlucose> bloodGlucoseList) {
            bloodGlucoses.clear();
            bloodGlucoses.addAll(bloodGlucoseList);
            notifyDataSetChanged();
        }
    }
}
