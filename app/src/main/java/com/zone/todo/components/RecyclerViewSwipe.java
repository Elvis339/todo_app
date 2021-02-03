package com.zone.todo.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zone.todo.R;
import com.zone.todo.adapter.TaskAdapter;
import com.zone.todo.entities.Task;
import com.zone.todo.viewmodel.TaskViewModel;

public class RecyclerViewSwipe extends ItemTouchHelper.SimpleCallback {
    private final TaskAdapter mAdapter;
    private final TaskViewModel mTaskViewModel;
    private Drawable trashIcon, completeIcon;
    private ColorDrawable redBackground, greenBackground;

    public RecyclerViewSwipe(TaskAdapter adapter, TaskViewModel taskViewModel) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mTaskViewModel = taskViewModel;
        trashIcon = ContextCompat.getDrawable(mAdapter.mContext, R.drawable.ic_delete);
        completeIcon = ContextCompat.getDrawable(mAdapter.mContext, R.drawable.ic_check_mark);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Task task = mAdapter.getTasks().get(position);
        String toastText;

        if (direction == ItemTouchHelper.RIGHT) {
            mTaskViewModel.deleteTask(task);
            toastText = task.getName() + " successfully deleted";
        } else {
            mTaskViewModel.completeTaskById(task.getTaskId());
            toastText = task.getName() + " successfully completed";
        }
        Toast.makeText(mAdapter.mContext, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundOffset = 20;

        if (dX > 0) { // Swiping to the right
            trashIcon.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundOffset,
                    itemView.getBottom());

            redBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            completeIcon.setBounds(itemView.getRight() + ((int) dX) - backgroundOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

            greenBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            trashIcon.setBounds(0, 0, 0, 0);
            completeIcon.setBounds(0, 0, 0, 0);
            redBackground.setBounds(0, 0, 0, 0);
            greenBackground.setBounds(0, 0, 0, 0);
        }

        redBackground.draw(c);
        greenBackground.draw(c);
        trashIcon.draw(c);
        completeIcon.draw(c);
    }
}
