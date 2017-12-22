package com.kfr2359.homefinancemobile.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kfr2359.homefinancemobile.R;
import com.kfr2359.homefinancemobile.logic.GenericEntity;
import com.unnamed.b.atv.model.TreeNode;

public class MoneyOpNodeViewHolder extends TreeNode.BaseNodeViewHolder<GenericEntity> {
    private ImageView arrowView;
    private TextView valueView;

    public MoneyOpNodeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, GenericEntity value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_node, null, false);

        arrowView = (ImageView) view.findViewById(R.id.node_arrow);
        valueView = (TextView) view.findViewById(R.id.node_value);

        arrowView.setImageResource(R.mipmap.ic_keyboard_arrow_right_black_24dp);
        valueView.setText(value.toString());

        return view;
    }

    public void toggle(boolean active) {
        arrowView.setImageResource(active ? R.mipmap.ic_keyboard_arrow_down_black_24dp : R.mipmap.ic_keyboard_arrow_right_black_24dp);
    }
}
