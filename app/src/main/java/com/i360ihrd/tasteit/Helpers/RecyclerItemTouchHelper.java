package com.i360ihrd.tasteit.Helpers;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.View;

import com.i360ihrd.tasteit.R;
import com.i360ihrd.tasteit.ViewHolder.CartViewHolder;

import static android.graphics.Color.WHITE;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    private  Drawable deleteIcon;
    private  int intrinsicWidth;
    private  int intrinsicHeight;
    private ColorDrawable background;
    private  int backgroundColor;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs , RecyclerItemTouchHelperListener listener ,Context context) {
        super(dragDirs, swipeDirs);
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_black_24dp);
        intrinsicWidth = deleteIcon.getIntrinsicWidth();
        intrinsicHeight = deleteIcon.getIntrinsicHeight();
        background = new ColorDrawable();
        backgroundColor = Color.parseColor("#f44336");
        this.listener = listener;

    }


    @Override
    public
    boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public
    void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom() - itemView.getTop();
        this.background.setColor(this.backgroundColor);
        this.background.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        this.background.draw(c);
        int deleteIconTop = itemView.getTop() + (itemHeight - this.intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - this.intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - this.intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + this.intrinsicHeight;
        this.deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        int color = Color.parseColor("#FFFFFF");
        this.deleteIcon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}