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
import uk.co.jaspalsvoice.jv.activities.DiagnosesActivity;
import uk.co.jaspalsvoice.jv.models.Diagnosis;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class DiagnosisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Diagnosis> diagnosiss;
    private Context context;

    public DiagnosisAdapter(Context context, ArrayList<Diagnosis> diagnosiss) {
        this.diagnosiss = diagnosiss;
        this.context = context;
    }

    private class DiagnosisViewHolder extends RecyclerView.ViewHolder {

        EditText diagnosisEdittext;
        EditText dateText;
        TextView diagnosisTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public DiagnosisViewHolder(View itemView) {
            super(itemView);
            diagnosisEdittext = (EditText) itemView.findViewById(R.id.diagnosisEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            diagnosisTextView = (TextView) itemView.findViewById(R.id.diagnosisTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_diagnosis, parent, false);
        return new DiagnosisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Diagnosis diagnosis = diagnosiss.get(position);
        if (diagnosis != null) {
            ((DiagnosisViewHolder) holder).diagnosisTextView.setText(diagnosis.getDiagnosis());
            ((DiagnosisViewHolder) holder).dateTextView.setText(diagnosis.getDate());
            ((DiagnosisViewHolder) holder).diagnosisEdittext.setText(diagnosis.getDiagnosis());
            ((DiagnosisViewHolder) holder).dateText.setText(diagnosis.getDate());
        } else {
            ((DiagnosisViewHolder) holder).diagnosisTextView.setText("");
            ((DiagnosisViewHolder) holder).dateTextView.setText("");
            ((DiagnosisViewHolder) holder).diagnosisEdittext.setText("");
            ((DiagnosisViewHolder) holder).dateText.setText("");
        }
        ((DiagnosisViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((DiagnosisViewHolder) holder));
            }
        });

        ((DiagnosisViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DiagnosesActivity)context).setDiagnosisId(diagnosis.getId());
                updateData(((DiagnosisViewHolder)holder).diagnosisEdittext.getText().toString(),
                        ((DiagnosisViewHolder)holder).dateText.getText().toString());
                ((DiagnosisViewHolder) holder).diagnosisTextView.setText(
                        ((DiagnosisViewHolder)holder).diagnosisEdittext.getText().toString()
                );
                ((DiagnosisViewHolder) holder).dateTextView.setText(
                        ((DiagnosisViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((DiagnosisViewHolder)holder));
            }
        });

        ((DiagnosisViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((DiagnosisViewHolder) holder));
            }
        });
    }

    private void updateData(String diagnosis, String date) {
        new Save().execute(diagnosis, date);
    }

    private void showEditMode(DiagnosisViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.diagnosisTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.diagnosisEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(DiagnosisViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.diagnosisTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.diagnosisEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return diagnosiss.size();
    }

    private class Save extends AsyncTask<String, Void, Diagnosis> {
        @Override
        protected Diagnosis doInBackground(String... params) {
            List<Diagnosis> diagnosiss = new ArrayList<>();
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setDiagnosis(params[0]);
            diagnosis.setDate(params[1]);
            diagnosiss.add(diagnosis);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceDiagnosis(diagnosiss,true,
                    ((DiagnosesActivity)context).getDiagnosisId());
            return diagnosis;
        }

        @Override
        protected void onPostExecute(Diagnosis diagnosis) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new DiagnosisRetrieve().execute();
    }

    private class DiagnosisRetrieve extends AsyncTask<Void, Void, List<Diagnosis>> {
        @Override
        protected List<Diagnosis> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllDiagnosis();
        }

        @Override
        protected void onPostExecute(List<Diagnosis> diagnosisList) {
            diagnosiss.clear();
            diagnosiss.addAll(diagnosisList);
            notifyDataSetChanged();
        }
    }
}
