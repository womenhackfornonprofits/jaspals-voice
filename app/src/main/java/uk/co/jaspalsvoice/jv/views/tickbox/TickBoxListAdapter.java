package uk.co.jaspalsvoice.jv.views.tickbox;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 3/3/2016.
 */
public class TickBoxListAdapter extends RecyclerView.Adapter<TickBoxListAdapter.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_OTHER = 1;
    private boolean[] optionsArray;
    private boolean[] newOptionsArray;
    private String[] data;
    private TickBoxListCardView cardView;
    private EditText otherEditTextView;
    private String otherData;

    public TickBoxListAdapter(String[] data, boolean[] optionsArray, String otherData) {
        this.data = data;
        if (optionsArray.length > 0) {
            this.optionsArray = optionsArray;
        } else {
            this.optionsArray = new boolean[data.length];
            for (int position = 0; position < this.optionsArray.length; position++){
                this.optionsArray[position] = false;
            }
        }
        this.otherData = otherData;
        this.newOptionsArray = new boolean[data.length];
    }

    public void setCardView(TickBoxListCardView cardView) {
        this.cardView = cardView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView checkedTextView;

        public ViewHolder(View view) {
            super(view);
            checkedTextView = (CheckedTextView) view.findViewById(R.id.item);
        }
    }

    public class OtherViewHolder extends ViewHolder {
        public OtherViewHolder(View view) {
            super(view);
            otherEditTextView = (EditText) view.findViewById(R.id.other);
            otherEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cardView.showEditMode();
                    }
                }
            });
        }
    }

    @Override
    public TickBoxListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OTHER:
                View otherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickbox_other_item_row, parent, false);
                return new OtherViewHolder(otherView);
            case TYPE_ITEM:
            default:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickbox_item_row, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final TickBoxListAdapter.ViewHolder holder, final int position) {
        holder.checkedTextView.setText(data[position]);
        if (optionsArray != null && optionsArray.length > 0){
            holder.checkedTextView.setChecked(optionsArray[position]);
            newOptionsArray[position] = optionsArray[position];
        }

        if (holder instanceof OtherViewHolder) {
            final OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
            if (otherViewHolder.checkedTextView.isChecked()) {
                otherEditTextView.setVisibility(View.VISIBLE);
                otherEditTextView.setText(otherData);
            } else {
                otherEditTextView.setVisibility(View.INVISIBLE);
                otherEditTextView.setText("");
            }
            otherViewHolder.checkedTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckedTextView checkedView = (CheckedTextView) v;
                    checkedView.toggle();
                    otherEditTextView.setVisibility(checkedView.isChecked() ? View.VISIBLE : View.INVISIBLE);
                    cardView.showEditMode();
                    newOptionsArray[position] = checkedView.isChecked();
                    if (!otherViewHolder.checkedTextView.isChecked()) {
                        otherEditTextView.setText("");
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckedTextView checkedView = (CheckedTextView) v;
                    checkedView.toggle();
                    cardView.showEditMode();
                    newOptionsArray[position] = checkedView.isChecked();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_OTHER : TYPE_ITEM;
    }

    public boolean[] getOptionsArray(){
        optionsArray = newOptionsArray;
        newOptionsArray = new boolean[data.length];

        return optionsArray;
    }

    public String getOtherData() {
        if (otherEditTextView != null) {
            otherData = otherEditTextView.getText().toString().trim();
            return otherData;
        } else {
            return "";
        }
    }
}
