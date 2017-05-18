package es.dmoral.clothyscanner.ui.main;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.clothyscanner.R;
import es.dmoral.clothyscanner.data.model.Machine;

/**
 * Created by grender on 18/05/17.
 */

public class MachineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Machine> machines;
    private OnMachineClickListener onMachineClickListener;
    private OnMachineLongClickListener onMachineLongClickListener;

    public interface OnMachineClickListener {
        void onMachineClick(Machine machine);
    }

    public interface OnMachineLongClickListener {
        void onMachineLongClick(Machine machine);
    }

    public MachineAdapter(ArrayList<Machine> machines, OnMachineClickListener onMachineClickListener,
                          OnMachineLongClickListener onMachineLongClickListener) {
        this.machines = machines;
        this.onMachineClickListener = onMachineClickListener;
        this.onMachineLongClickListener = onMachineLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MachineViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MachineViewHolder) holder).machineImage.setImageDrawable(TextDrawable.builder()
                .buildRound(String.valueOf(machines.get(position).getFriendlyName().charAt(0)).toUpperCase(),
                    ColorGenerator.MATERIAL.getColor(machines.get(position).getFriendlyName())));
        ((MachineViewHolder) holder).machineName.setText(machines.get(position).getFriendlyName());
        ((MachineViewHolder) holder).machineIp.setText(machines.get(position).getIp());
    }

    @Override
    public int getItemCount() {
        return machines == null ? 0 : machines.size();
    }

    public void addMachine(Machine machine) {
        machines.add(machine);
        notifyItemInserted(machines.size() - 1);
    }

    public void removeMachine(Machine machine) {
        int oldIndex = machines.indexOf(machine);
        machines.remove(oldIndex);
        notifyItemRemoved(oldIndex);
    }

    public boolean alreadyAdded(Machine machine) {
        return machines.contains(machine);
    }

    class MachineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.machine_image) ImageView machineImage;
        @BindView(R.id.machine_name) TextView machineName;
        @BindView(R.id.machine_ip) TextView machineIp;

        MachineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onMachineClickListener.onMachineClick(machines.get(getAdapterPosition()));
                }
            }, 265);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.contextual_machine_menu, Menu.NONE, R.string.delete_machine_msg);
        }

        @Override
        public boolean onLongClick(View view) {
            onMachineLongClickListener.onMachineLongClick(machines.get(getAdapterPosition()));
            return false;
        }
    }
}
