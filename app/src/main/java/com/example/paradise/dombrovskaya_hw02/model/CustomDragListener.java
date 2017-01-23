package com.example.paradise.dombrovskaya_hw02.model;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paradise on 23.01.2017.
 */

public class CustomDragListener {

    public View.OnLongClickListener longClickListener(){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData clipData = ClipData.newPlainText("","");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(clipData,dragShadowBuilder,view, 0);
                view.setAlpha(0.0f);
                return true;
            }
        };
    }

    public View.OnDragListener onDragListener(){
        return new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                View currentView =(View) dragEvent.getLocalState();
                ViewGroup parent =(ViewGroup) currentView.getParent();
                int currentViewId = parent.indexOfChild(currentView);
                int nextViewId = parent.indexOfChild(view);
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(currentViewId!= nextViewId && currentViewId < nextViewId){
                            parent.removeViewAt(nextViewId);
                            parent.removeViewAt(currentViewId);
                            parent.addView(view,currentViewId);
                            parent.addView(currentView, nextViewId);
                        }else if (currentViewId > nextViewId){
                            parent.removeViewAt(currentViewId);
                            parent.removeViewAt(nextViewId);
                            parent.addView(currentView, nextViewId);
                            parent.addView(view,currentViewId);
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        currentView.setAlpha(1.0f);
                        break;
                    default:
                        break;
                }

                return true;
            }
        };
    }
}
