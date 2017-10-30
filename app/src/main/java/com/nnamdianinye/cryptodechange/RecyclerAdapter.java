package com.nnamdianinye.cryptodechange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Data> dataList;
    private TrueOrFalse trueOrFalse;
    ItemClickListener listener;
    Context context;

    public RecyclerAdapter(Context context, List<Data> dataList, TrueOrFalse trueOrFalse, ItemClickListener listener) {
        this.dataList = dataList;
        this.trueOrFalse = trueOrFalse;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.view_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());

                Bundle bundle = new Bundle();
                bundle.putString("crypto", viewHolder.tvCrypto.getText().toString());
                bundle.putString("cryptoValue", viewHolder.tvCryptoValue.getText().toString());
                bundle.putString("base", viewHolder.tvBaseCurrency.getText().toString());
                bundle.putString("baseValue", viewHolder.tvBaseCurrencyValue.getText().toString());

                Intent intent = new Intent(context, ConvertActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        Data data = dataList.get(position);
        if (trueOrFalse.isBtc()) {
            holder.tvCrypto.setText(R.string.btc);
            holder.tvCryptoValue.setText(R.string.one);
        } else if (trueOrFalse.isEth()) {
            holder.tvCrypto.setText(R.string.eth);
            holder.tvCryptoValue.setText(R.string.one);
        }
        holder.tvBaseCurrency.setText(data.getKey());
        holder.tvBaseCurrencyValue.setText(data.getValue());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCrypto, tvCryptoValue, tvBaseCurrency, tvBaseCurrencyValue;
        ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);
            //textView = (TextView) itemView.findViewById(R.id.text_card);
            tvCrypto = (TextView) itemView.findViewById(R.id.crypto);
            tvCryptoValue = (TextView) itemView.findViewById(R.id.crypto_value);
            tvBaseCurrency = (TextView) itemView.findViewById(R.id.base_currency);
            tvBaseCurrencyValue = (TextView) itemView.findViewById(R.id.base_currency_value);
            remove = (ImageView) itemView.findViewById(R.id.removeButton);
        }
    }
}