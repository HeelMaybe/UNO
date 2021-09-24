package com.example.uno_test.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uno_test.R;
import com.example.uno_test.data.Game;
import com.google.firebase.firestore.FirebaseFirestore;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> Games;
    private IGameRow callback;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GameAdapter(List<Game> myDataset, IGameRow callback) {
        Games = myDataset;
        this.callback = callback;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public View root;
        public TextView tvGameName;
        public TextView tvCreatedAt;
        public GameViewHolder(View itemView){
            super(itemView);
            context = itemView.getContext();
            root = itemView.findViewById(R.id.lytRow);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }


    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_games, parent, false);
        GameViewHolder userViewHolder = new GameViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        PrettyTime prettyTime = new PrettyTime();
        Game game = Games.get(position);
        holder.tvGameName.setText(game.title);
        holder.tvCreatedAt.setText(prettyTime.format(game.createdAt));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onGameSelected(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Games.size();
    }


    public interface IGameRow {
        void onGameSelected(Game game);
    }
}
