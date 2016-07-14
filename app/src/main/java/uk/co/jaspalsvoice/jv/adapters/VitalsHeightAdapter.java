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
import uk.co.jaspalsvoice.jv.models.VitalsHeight;

/**
 * Created by Srinivas Klayani on 11 Jul 16.
 */
public class VitalsHeightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VitalsHeight> heights;
    private Context context;

    public VitalsHeightAdapter(Context context, ArrayList<VitalsHeight> heights) {
        this.heights = heights;
        this.context = context;
    }

    private class HeightViewHolder extends RecyclerView.ViewHolder {

        EditText heightEdittext;
        EditText dateText;
        TextView heightTextView, dateTextView;
        ImageView editButton;
        LinearLayout editLayout;
        ImageView saveButton, cancelButton;

        public HeightViewHolder(View itemView) {
            super(itemView);
            heightEdittext = (EditText) itemView.findViewById(R.id.heightEdittext);
            dateText = (EditText) itemView.findViewById(R.id.dateEditText);
            editButton = (ImageView) itemView.findViewById(R.id.editButton);
            heightTextView = (TextView) itemView.findViewById(R.id.heightTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            editLayout = (LinearLayout) itemView.findViewById(R.id.saveLayout);
            saveButton = (ImageView) editLayout.findViewById(R.id.saveButton);
            cancelButton = (ImageView) editLayout.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.list_item_vitals_height, parent, false);
        return new HeightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final VitalsHeight height = heights.get(position);
        if (height != null) {
            ((HeightViewHolder) holder).heightTextView.setText(height.height);
            ((HeightViewHolder) holder).dateTextView.setText(height.date);
            ((HeightViewHolder) holder).heightEdittext.setText(height.height);
            ((HeightViewHolder) holder).dateText.setText(height.date);
        } else {
            ((HeightViewHolder) holder).heightTextView.setText("");
            ((HeightViewHolder) holder).dateTextView.setText("");
            ((HeightViewHolder) holder).heightEdittext.setText("");
            ((HeightViewHolder) holder).dateText.setText("");
        }
        ((HeightViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditMode(((HeightViewHolder) holder));
            }
        });

        ((HeightViewHolder) holder).saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VitalsActivity)context).setHeightId(height.getId());
                updateData(((HeightViewHolder)holder).heightEdittext.getText().toString(),
                        ((HeightViewHolder)holder).dateText.getText().toString());
                ((HeightViewHolder) holder).heightTextView.setText(
                        ((HeightViewHolder)holder).heightEdittext.getText().toString()
                );
                ((HeightViewHolder) holder).dateTextView.setText(
                        ((HeightViewHolder)holder).dateText.getText().toString()
                );
                hideEditMode(((HeightViewHolder)holder));
            }
        });

        ((HeightViewHolder) holder).cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditMode(((HeightViewHolder) holder));
            }
        });
    }

    private void updateData(String height, String date) {
        new Save().execute(height, date);
    }

    private void showEditMode(HeightViewHolder holder) {
        holder.editLayout.setVisibility(View.VISIBLE);
        holder.editButton.setVisibility(View.GONE);
        holder.heightTextView.setVisibility(View.GONE);
        holder.dateTextView.setVisibility(View.GONE);
        holder.heightEdittext.setVisibility(View.VISIBLE);
        holder.dateText.setVisibility(View.VISIBLE);
    }

    private void hideEditMode(HeightViewHolder holder) {
        holder.editLayout.setVisibility(View.GONE);
        holder.editButton.setVisibility(View.VISIBLE);
        holder.heightTextView.setVisibility(View.VISIBLE);
        holder.dateTextView.setVisibility(View.VISIBLE);
        holder.heightEdittext.setVisibility(View.GONE);
        holder.dateText.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return heights.size();
    }

    private class Save extends AsyncTask<String, Void, VitalsHeight> {
        @Override
        protected VitalsHeight doInBackground(String... params) {
            List<VitalsHeight> heights = new ArrayList<>();
            VitalsHeight height = new VitalsHeight();
            height.setHeight(params[0]);
            height.setDate(params[1]);
            heights.add(height);
            ((JvApplication) context.getApplicationContext()).getDbHelper().insertOrReplaceHeight(heights,true,
                    ((VitalsActivity)context).getHeightId());
            return height;
        }

        @Override
        protected void onPostExecute(VitalsHeight height) {
            getLatestData();
        }
    }

    private void getLatestData() {
        new Height().execute();
    }

    private class Height extends AsyncTask<Void, Void, List<VitalsHeight>> {
        @Override
        protected List<VitalsHeight> doInBackground(Void... params) {
            return ((JvApplication)context.getApplicationContext()).getDbHelper().readAllHeights();
        }

        @Override
        protected void onPostExecute(List<VitalsHeight> heightList) {
            heights.clear();
            heights.addAll(heightList);
            notifyDataSetChanged();
        }
    }
}
