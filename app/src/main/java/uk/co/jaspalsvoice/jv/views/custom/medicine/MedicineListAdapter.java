package uk.co.jaspalsvoice.jv.views.custom.medicine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.MedicinesActivity;
import uk.co.jaspalsvoice.jv.models.*;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 3/6/2016.
 */
public class MedicineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_OTHER = 1;
    private Context context;
    private int id;
    private String[] answers;
    private boolean isEditMode;

    private List<uk.co.jaspalsvoice.jv.models.Medicine> data;

    public MedicineListAdapter(Context context, List<uk.co.jaspalsvoice.jv.models.Medicine> data) {
        this.data = data;
        this.context = context;
        answers = context.getResources().getStringArray(R.array.yes_no_spinner_item);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView dosageView;
        TextView reasonView;
        TextView frequencyView;

        EditText nameEdit;
        EditText dosageEdit;
        EditText reasonEdit;
        EditText frequencyEdit;

        YesNoCardView renalCardView;
        YesNoCardView hepaticCardView;

        Button edit;
        boolean editMode;

        Context context;

        public ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.show_value_name);
            dosageView = (TextView) view.findViewById(R.id.show_value_dosage);
            reasonView = (TextView) view.findViewById(R.id.show_value_reason);
            frequencyView = (TextView) view.findViewById(R.id.show_value_frequency);

            nameEdit = (EditText) view.findViewById(R.id.edit_value_name);
            dosageEdit = (EditText) view.findViewById(R.id.edit_value_dosage);
            reasonEdit = (EditText) view.findViewById(R.id.edit_value_reason);
            frequencyEdit = (EditText) view.findViewById(R.id.edit_value_frequency);

            renalCardView = (YesNoCardView) view.findViewById(R.id.renalDosageCard);
            hepaticCardView = (YesNoCardView) view.findViewById(R.id.hepaticDosageCard);

            renalCardView.disableTitle(false);
            hepaticCardView.disableTitle(false);

            renalCardView.setTitle("Renal dosage required");
            renalCardView.setTitleId(R.string.renal_dosage);

            hepaticCardView.setTitle("Hepatic dosage required");
            hepaticCardView.setTitleId(R.string.hepatic_dosage);


            edit = (Button) view.findViewById(R.id.edit);

            context = MedicineListAdapter.this.context;

            showNonEditMode();

           /* edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMode = !editMode;

                    if (editMode) {
                        showEditMode();
                    } else {
                        ((MedicinesActivity)context).setId(getId());
                        showNonEditMode();
                    }
                }
            });*/
        }

        private void showNonEditMode() {
            nameEdit.setVisibility(View.GONE);
            nameView.setVisibility(View.VISIBLE);
            nameView.setText(nameEdit.getText());

            dosageEdit.setVisibility(View.GONE);
            dosageView.setVisibility(View.VISIBLE);
            dosageView.setText(dosageEdit.getText());

            reasonEdit.setVisibility(View.GONE);
            reasonView.setVisibility(View.VISIBLE);
            reasonView.setText(reasonEdit.getText());

            frequencyEdit.setVisibility(View.GONE);
            frequencyView.setVisibility(View.VISIBLE);
            frequencyView.setText(frequencyEdit.getText());

            renalCardView.showNonEditMode();
            renalCardView.disableTitle(true);

            hepaticCardView.showNonEditMode();
            hepaticCardView.disableTitle(true);
            edit.setText("Edit");
        }

        private void showEditMode() {
            nameEdit.setVisibility(View.VISIBLE);
            nameView.setVisibility(View.GONE);

            dosageEdit.setVisibility(View.VISIBLE);
            dosageView.setVisibility(View.GONE);

            reasonEdit.setVisibility(View.VISIBLE);
            reasonView.setVisibility(View.GONE);

            frequencyEdit.setVisibility(View.VISIBLE);
            frequencyView.setVisibility(View.GONE);

            renalCardView.showEditMode();
            hepaticCardView.showEditMode();

            edit.setText("Save");
        }
    }

   /* public static class OtherViewHolder extends RecyclerView.ViewHolder {
        Button addMedicine;

        public OtherViewHolder(View view) {
            super(view);
            addMedicine = (Button) view.findViewById(R.id.add_medicine);
            addMedicine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Show Dialog", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            /*case TYPE_OTHER:
                View otherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_medicine_view, parent, false);
                return new OtherViewHolder(otherView);*/
            case TYPE_ITEM:
            default:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_view, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final ViewHolder vHolder = ((ViewHolder) holder);
            final uk.co.jaspalsvoice.jv.models.Medicine medicine = data.get(position);

            vHolder.nameView.setText(medicine.getName());
            vHolder.dosageView.setText(medicine.getDosage());
            vHolder.reasonView.setText(medicine.getReason());
            vHolder.frequencyView.setText(medicine.getFrequency());

            vHolder.nameEdit.setText(medicine.getName());
            vHolder.dosageEdit.setText(medicine.getDosage());
            vHolder.reasonEdit.setText(medicine.getReason());
            vHolder.frequencyEdit.setText(medicine.getFrequency());

            vHolder.renalCardView.setText(medicine.getRenalDosage() == 0 ? answers[0] : answers[1]);
            vHolder.hepaticCardView.setText(medicine.getHepaticDosage() == 0 ? answers[0] : answers[1]);

            vHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vHolder.editMode = !vHolder.editMode;

                    if (vHolder.editMode) {
                        vHolder.showEditMode();
                    } else {
                        ((MedicinesActivity) context).setId(medicine.getId());
                        ((MedicinesActivity) context).setRenalDosageId(vHolder.renalCardView.getSelection());
                        ((MedicinesActivity) context).setHepaticDosageId(vHolder.hepaticCardView.getSelection());
                        saveMedicineData(vHolder.nameEdit.getText().toString(),
                                vHolder.dosageEdit.getText().toString(),
                                vHolder.reasonEdit.getText().toString(),
                                vHolder.frequencyEdit.getText().toString());
                        vHolder.showNonEditMode();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

  /*  @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_OTHER : TYPE_ITEM;
    }*/

    private void saveMedicineData(String... medicineData) {
        ((MedicinesActivity) context).saveMedicineData(medicineData);
    }

}
