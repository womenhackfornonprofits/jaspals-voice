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
import uk.co.jaspalsvoice.jv.activities.DiagnosesActivity;
import uk.co.jaspalsvoice.jv.models.SurgicalHistory;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class SurgicalHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SurgicalHistory> surgicalHistorys;
    private Context context;

    public SurgicalHistoryAdapter(Context context, ArrayList<SurgicalHistory> surgicalHistorys) {
        this.surgicalHistorys = surgicalHistorys;
        this.context = context;
    }

    private class SurgicalHistoryViewHolder extends RecyclerView.ViewHolder {

        EditText surgicalHistoryEdittext;
        EditText dateText;
        TextView surgicalHistoryTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public SurgicalHistoryViewHolder(View itemView) {
            super(itemView);
            surgicalHistoryEdittext = (EditText) itemView.findViewById(R.id.surgicalHistoryEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            surgicalHistoryTextView = (TextView) itemView.findViewById(R.id.surgicalHistoryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_surgical_history, parent, false);
        return new SurgicalHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SurgicalHistory surgicalHistory = surgicalHistorys.get(position);
        if (surgicalHistory != null) {
            ((SurgicalHistoryViewHolder) holder).surgicalHistoryTextView.setText(surgicalHistory.getSurgicalHistory());
            ((SurgicalHistoryViewHolder) holder).dateTextView.setText(surgicalHistory.getDate());
            ((SurgicalHistoryViewHolder) holder).surgicalHistoryEdittext.setText(surgicalHistory.getSurgicalHistory());
            ((SurgicalHistoryViewHolder) holder).dateText.setText(surgicalHistory.getDate());
        } else {
            ((SurgicalHistoryViewHolder) holder).surgicalHistoryTextView.setText("");
            ((SurgicalHistoryViewHolder) holder).dateTextView.setText("");
            ((SurgicalHistoryViewHolder) holder).surgicalHistoryEdittext.setText("");
            ((SurgicalHistoryViewHolder) holder).dateText.setText("");
        }
        ((SurgicalHistoryViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((SurgicalHistoryViewHolder) holder));
            }
        });

        ((SurgicalHistoryViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DiagnosesActivity)context).setSurgicalHistoryId(surgicalHistory.getId());
                updateData(((SurgicalHistoryViewHolder)holder).surgicalHistoryEdittext.getText().toString(),
                        ((SurgicalHistoryViewHolder)holder).dateText.getText().toString());
                ((SurgicalHistoryViewHolder) holder).surgicalHistoryTextView.setText(
                        ((SurgicalHistoryViewHolder)holder).surgicalHistoryEdittext.getText().toString()
                );
                ((SurgicalHistoryViewHolder) holder).dateTextView.setText(
                        ((SurgicalHistoryViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((SurgicalHistoryViewHolder)holder));
            }
        });

        ((SurgicalHistoryViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((SurgicalHistoryViewHolder) holder));
            }
        });


        ((SurgicalHistoryViewHolder)holder).dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePicker(((SurgicalHistoryViewHolder)holder).dateText, ((DiagnosesActivity)context));
            }
        });
    }

    private void updateData(String surgicalHistory, String date) {
        new Save().execute(surgicalHistory, date);
    }

    private void showEditMode(SurgicalHistoryViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.surgicalHistoryTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.surgicalHistoryEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(SurgicalHistoryViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.surgicalHistoryTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.surgicalHistoryEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return surgicalHistorys.size();
    }

    private class Save extends AsyncTask<String, Void, SurgicalHistory> {
        @Override
        protected SurgicalHistory doInBackground(String... params) {
            List<SurgicalHistory> surgicalHistorys = new ArrayList<>();
            SurgicalHistory surgicalHistory = new SurgicalHistory();
            surgicalHistory.setSurgicalHistory(params[0]);
            surgicalHistory.setDate(params[1]);
            surgicalHistorys.add(surgicalHistory);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceSurgicalHistory(surgicalHistorys,true,
                    ((DiagnosesActivity)context).getSurgicalHistoryId());
            return surgicalHistory;
        }

        @Override
        protected void onPostExecute(SurgicalHistory surgicalHistory) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new SurgicalHistoryRetrieve().execute();
    }

    private class SurgicalHistoryRetrieve extends AsyncTask<Void, Void, List<SurgicalHistory>> {
        @Override
        protected List<SurgicalHistory> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllSurgicalHistory();
        }

        @Override
        protected void onPostExecute(List<SurgicalHistory> surgicalHistoryList) {
            surgicalHistorys.clear();
            surgicalHistorys.addAll(surgicalHistoryList);
            notifyDataSetChanged();
        }
    }
}
