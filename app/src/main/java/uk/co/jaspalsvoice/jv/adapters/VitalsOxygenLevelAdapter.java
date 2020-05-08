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
import uk.co.jaspalsvoice.jv.models.VitalsOxygenLevel;

public class VitalsOxygenLevelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VitalsOxygenLevel> oxygenLevels;
    private Context context;

    public VitalsOxygenLevelAdapter(Context context, ArrayList<VitalsOxygenLevel> oxygenLevels) {
        this.oxygenLevels = oxygenLevels;
        this.context = context;
    }

    private class OxygenLevelViewHolder extends RecyclerView.ViewHolder {

        EditText oxygenLevelEdittext;
        EditText dateText;
        TextView oxygenLevelTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public OxygenLevelViewHolder(View itemView) {
            super(itemView);
            oxygenLevelEdittext = (EditText) itemView.findViewById(R.id.editFirstField);
            dateText = (EditText) itemView.findViewById(R.id.editSecondField);
            dateText.setFocusable(false);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            oxygenLevelTextView = (TextView) itemView.findViewById(R.id.dataFirstField);
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
        return new OxygenLevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final VitalsOxygenLevel oxygenLevel = oxygenLevels.get(position);
        if (oxygenLevel != null) {
            ((OxygenLevelViewHolder) holder).oxygenLevelTextView.setText(oxygenLevel.getOxygenLevel());
            ((OxygenLevelViewHolder) holder).dateTextView.setText(oxygenLevel.getDate());
            ((OxygenLevelViewHolder) holder).oxygenLevelEdittext.setText(oxygenLevel.getOxygenLevel());
            ((OxygenLevelViewHolder) holder).dateText.setText(oxygenLevel.getDate());
        } else {
            ((OxygenLevelViewHolder) holder).oxygenLevelTextView.setText("");
            ((OxygenLevelViewHolder) holder).dateTextView.setText("");
            ((OxygenLevelViewHolder) holder).oxygenLevelEdittext.setText("");
            ((OxygenLevelViewHolder) holder).dateText.setText("");
        }
        ((OxygenLevelViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((OxygenLevelViewHolder) holder));
            }
        });

        ((OxygenLevelViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VitalsActivity)context).setOxygenLevelId(oxygenLevel.getId());
                updateData(((OxygenLevelViewHolder)holder).oxygenLevelEdittext.getText().toString(),
                        ((OxygenLevelViewHolder)holder).dateText.getText().toString());
                ((OxygenLevelViewHolder) holder).oxygenLevelTextView.setText(
                        ((OxygenLevelViewHolder)holder).oxygenLevelEdittext.getText().toString()
                );
                ((OxygenLevelViewHolder) holder).dateTextView.setText(
                        ((OxygenLevelViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((OxygenLevelViewHolder)holder));
            }
        });

        ((OxygenLevelViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((OxygenLevelViewHolder) holder));
            }
        });

        ((OxygenLevelViewHolder)holder).dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateUtils.showDatePicker(((OxygenLevelViewHolder)holder).dateText, ((VitalsActivity)context));
            }
        });
        hideEditMode(((OxygenLevelViewHolder)holder));
    }

    private void updateData(String oxygenLevel, String date) {
        new Save().execute(oxygenLevel, date);
    }

    private void showEditMode(OxygenLevelViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.oxygenLevelTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.oxygenLevelEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(OxygenLevelViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.oxygenLevelTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.oxygenLevelEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return oxygenLevels.size();
    }

    private class Save extends AsyncTask<String, Void, VitalsOxygenLevel> {
        @Override
        protected VitalsOxygenLevel doInBackground(String... params) {
            List<VitalsOxygenLevel> oxygenLevels = new ArrayList<>();
            VitalsOxygenLevel oxygenLevel = new VitalsOxygenLevel();
            oxygenLevel.setOxygenLevel(params[0]);
            oxygenLevel.setDate(params[1]);
            oxygenLevels.add(oxygenLevel);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceOxygenLevel(oxygenLevels,true,
                    ((VitalsActivity)context).getOxygenLevelId());
            return oxygenLevel;
        }

        @Override
        protected void onPostExecute(VitalsOxygenLevel oxygenLevel) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new OxygenLevel().execute();
    }

    private class OxygenLevel extends AsyncTask<Void, Void, List<VitalsOxygenLevel>> {
        @Override
        protected List<VitalsOxygenLevel> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllOxygenLevels();
        }

        @Override
        protected void onPostExecute(List<VitalsOxygenLevel> oxygenLevelsList) {
            oxygenLevels.clear();
            oxygenLevels.addAll(oxygenLevelsList);
            notifyDataSetChanged();
        }
    }

}
