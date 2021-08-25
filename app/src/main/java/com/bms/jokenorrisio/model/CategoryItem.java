package com.bms.jokenorrisio.model;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.bms.jokenorrisio.R;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;
//aqui são definidas os propriedades do menu de categorias
//terá o nome da categoria + uma cor de background
public class CategoryItem extends Item<ViewHolder> {

    private final String categoryName;
    private final int bgColor;

    public CategoryItem(String categoryName, int bgColor){
        this.categoryName =  categoryName;
        this.bgColor = bgColor;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView txtcategory = viewHolder.itemView.findViewById(R.id.txt_category);
        txtcategory.setText(categoryName);
        viewHolder.itemView.setBackgroundColor(bgColor);
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int getLayout() {
        return R.layout.card_category;
    }
}
