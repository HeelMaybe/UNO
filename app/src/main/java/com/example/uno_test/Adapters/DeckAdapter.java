package com.example.uno_test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uno_test.R;
import com.example.uno_test.data.Card;

import java.util.ArrayList;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    ArrayList<Card> cards = new ArrayList<>();
    private ICardRow callback;

    public DeckAdapter(ArrayList<Card> cards, ICardRow callback) {
        this.cards = cards;
        this.callback = callback;

    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        public View root;
        public TextView tvCardValue;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            root = itemView.findViewById(R.id.lytColumn);
            tvCardValue = itemView.findViewById(R.id.tvCardNumber);

        }
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cards, parent, false);
        DeckAdapter.DeckViewHolder deckViewHolder = new DeckViewHolder(view);
        return deckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Card card = cards.get(position);
        String type = card.getType();
        String color = card.getColor();

        //TODO: change background color when
        holder.tvCardValue.setText(color + " " + type);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCardSelected(card);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public interface ICardRow {
        void onCardSelected(Card card);
    }
}
