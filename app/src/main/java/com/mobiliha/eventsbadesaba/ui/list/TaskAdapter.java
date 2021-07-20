package com.mobiliha.eventsbadesaba.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.databinding.AdapterTaskBinding;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {
    
    public interface OnItemSelectListener {
        void onSelect(Task task);
    }

    private final OnItemSelectListener listener;

    public TaskAdapter(OnItemSelectListener listener) {
        super(new DiffUtil.ItemCallback<Task>() {
            @Override
            public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return oldItem.getTaskId() == newItem.getTaskId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return oldItem.equals(newItem);
            }
        });

        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaskViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getCurrentList().get(position);
        holder.bind(task, listener);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final AdapterTaskBinding binding;

        public TaskViewHolder(@NonNull AdapterTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task, OnItemSelectListener listener) {
            binding.setTask(task);
            binding.setCallback(listener);
            binding.executePendingBindings();
        }

        public static TaskViewHolder from(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AdapterTaskBinding binding = AdapterTaskBinding.inflate(inflater, parent, false);
            return new TaskViewHolder(binding);
        }

    }

}
