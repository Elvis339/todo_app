package com.zone.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zone.todo.R;
import com.zone.todo.entities.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    static String TAG = TaskAdapter.class.getSimpleName();

    private static final String DATE_FORMAT = "yyyy/MM/DD";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    final private ItemClickListener mItemClickListener;
    final public Context mContext;

    private List<Task> mTasks;


    public TaskAdapter(Context context, ItemClickListener listener) {
        mItemClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        String date = dateFormat.format(task.getDate());


        holder.taskName.setText(task.getName());
        holder.taskAdditionalInfo.setVisibility(View.VISIBLE);
        holder.taskDate.setText(date);
    }

    public void setTasks(List<Task> taskEntries) {
        mTasks = taskEntries;
        notifyDataSetChanged();
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        }
        return mTasks.size();
    }


    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskName, taskDate;
        RelativeLayout taskAdditionalInfo;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.taskName);
            taskDate = view.findViewById(R.id.taskDate);
            taskAdditionalInfo = view.findViewById(R.id.taskAdditionalInfo);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int taskId = mTasks.get(getAdapterPosition()).getTaskId();
            mItemClickListener.onItemClickListener(taskId);
        }
    }
}
