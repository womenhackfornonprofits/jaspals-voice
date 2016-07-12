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
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.models.VitalsWeight;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class VitalsWeightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VitalsWeight> weights;
    private Context context;

    public VitalsWeightAdapter(Context context, ArrayList<VitalsWeight> weights) {
        this.weights = weights;
        this.context = context;
    }

    private class WeightViewHolder extends RecyclerView.ViewHolder {

        EditText weightEdittext;
        EditText dateText;
        TextView weightTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public WeightViewHolder(View itemView) {
            super(itemView);
            weightEdittext = (EditText) itemView.findViewById(R.id.weightEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            weightTextView = (TextView) itemView.findViewById(R.id.weightTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_vitals_weight, parent, false);
        return new WeightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        VitalsWeight weight = weights.get(position);
        if (weight != null) {
            ((VitalsActivity)context).setWeightId(weight.getId());
            ((WeightViewHolder) holder).weightTextView.setText(weight.weight);
            ((WeightViewHolder) holder).dateTextView.setText(weight.date);
            ((WeightViewHolder) holder).weightEdittext.setText(weight.weight);
            ((WeightViewHolder) holder).dateText.setText(weight.date);
        } else {
            ((WeightViewHolder) holder).weightTextView.setText("");
            ((WeightViewHolder) holder).dateTextView.setText("");
            ((WeightViewHolder) holder).weightEdittext.setText("");
            ((WeightViewHolder) holder).dateText.setText("");
        }
        ((WeightViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((WeightViewHolder) holder));
            }
        });

        ((WeightViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(((WeightViewHolder)holder).weightEdittext.getText().toString(),
                        ((WeightViewHolder)holder).dateText.getText().toString());
                ((WeightViewHolder) holder).weightTextView.setText(
                        ((WeightViewHolder)holder).weightEdittext.getText().toString()
                );
                ((WeightViewHolder) holder).dateTextView.setText(
                        ((WeightViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((WeightViewHolder)holder));
            }
        });

        ((WeightViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((WeightViewHolder) holder));
            }
        });
    }

    private void updateData(String weight, String date) {
        new Save().execute(weight, date);
    }

    private void showEditMode(WeightViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.weightTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.weightEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(WeightViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.weightTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.weightEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return weights.size();
    }

    private class Save extends AsyncTask<String, Void, VitalsWeight> {
        @Override
        protected VitalsWeight doInBackground(String... params) {
            List<VitalsWeight> weights = new ArrayList<>();
            VitalsWeight weight = new VitalsWeight();
            weight.setWeight(params[0]);
            weight.setDate(params[1]);
            weights.add(weight);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceWeight(weights,true,
                    ((VitalsActivity)context).getWeightId());
            return weight;
        }

        @Override
        protected void onPostExecute(VitalsWeight weight) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new Weight().execute();
    }

    private class Weight extends AsyncTask<Void, Void, List<VitalsWeight>> {
        @Override
        protected List<VitalsWeight> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllWeights();
        }

        @Override
        protected void onPostExecute(List<VitalsWeight> weightList) {
            weights.clear();
            weights.addAll(weightList);
            notifyDataSetChanged();
        }
    }
}
