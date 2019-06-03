package com.hanks.ecommerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanks.ecommerce.R;
import com.hanks.ecommerce.interfaces.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName , txtProductDescription ;
    public ImageView imageView ;
    public ItemClickListener listener ;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
    }
 public void setItemClickListener(ItemClickListener listener)
    {
     this.listener = listener;

    }
    @Override
    public void onClick(View v)
    {
       listener.onClick( v , getAdapterPosition(), false);
    }
}
