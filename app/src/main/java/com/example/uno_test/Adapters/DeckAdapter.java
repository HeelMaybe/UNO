package com.example.uno_test.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        public CardView cardView;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            root = itemView.findViewById(R.id.lytColumn);
            tvCardValue = itemView.findViewById(R.id.tvCardNumber);
            cardView = itemView.findViewById(R.id.cardview);
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
        holder.tvCardValue.setText(type);
        if(card.getType().equals("SKIP")){
            holder.tvCardValue.setText(type);
        }else if(card.getType().equals("DRAW4")){
            holder.tvCardValue.setText("+4");
        }

        if(card.getColor().equals("RED")){
            holder.cardView.setCardBackgroundColor(Color.RED);
        }else if(card.getColor().equals("GREEN")){
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        }else if(card.getColor().equals("YELLOW")){
            holder.cardView.setCardBackgroundColor(Color.YELLOW);
        }else if(card.getColor().equals("BLUE")){
            holder.cardView.setCardBackgroundColor(Color.BLUE);
        }else if(card.getType().equals("DRAW4")){
            holder.cardView.setCardBackgroundColor(Color.BLACK);
        }

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
