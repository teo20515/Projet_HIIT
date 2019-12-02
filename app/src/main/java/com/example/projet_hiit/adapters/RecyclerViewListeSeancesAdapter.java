package com.example.projet_hiit.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_hiit.R;
import com.example.projet_hiit.db.model.Seance;

import java.util.List;

public class RecyclerViewListeSeancesAdapter extends RecyclerView.Adapter<RecyclerViewListeSeancesAdapter.ViewHolder> {

    private List<Seance> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private ItemLongClickListener longClickListener;

    //Utilisé pour le menu lors d'un appui long sur une séance
    private int position;

    public RecyclerViewListeSeancesAdapter(Context context, List<Seance> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }


    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener{
        void onLongItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener,View.OnCreateContextMenuListener{

        LinearLayout seance;
        TextView dureePreparation;
        TextView nomTravail;
        TextView dureeTravail;
        TextView dureeRepos;
        TextView nbRepetitionsCycle;
        TextView dureeReposLong;
        TextView nbRepetitionsSequence;

        ViewHolder(View itemView){
            super(itemView);

            //Seance
            seance = itemView.findViewById(R.id.rvSeance);
            seance.setOnClickListener(this);
            seance.setOnLongClickListener(this);

            //Récupération des différentes zones de texte à remplir
            dureePreparation = itemView.findViewById(R.id.rv_dureePreparation);
            nomTravail = itemView.findViewById(R.id.rv_nomTravail);
            dureeTravail = itemView.findViewById(R.id.rv_dureeTravail);
            dureeRepos = itemView.findViewById(R.id.rv_dureeRepos);
            nbRepetitionsCycle = itemView.findViewById(R.id.rv_nbRepetitionsCycle);
            dureeReposLong = itemView.findViewById(R.id.rv_dureeReposLong);
            nbRepetitionsSequence = itemView.findViewById(R.id.rv_nbRepetitionsSequence);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view){
            if(clickListener != null){
                clickListener.onItemClick(view, getAdapterPosition());  //Renvoie la position dans la liste de la séance cliquée
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(longClickListener != null){
                setPosition(getAdapterPosition());// Prépare un attribu position qui sera accédé plus tard
                itemView.showContextMenu(); //Affiche le menu contextuel
                return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE,R.id.main_menu_supprimer,Menu.NONE,"Supprimer"); //Ajoute une option au menu (la seule dans l'état actuel)
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_main_listeseances, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Récupération des données d'une séance
        String tempsPreparation = String.valueOf(data.get(position).getPreparation());
        String nomTravail = data.get(position).getSequence().getCycle().getTravail().getNom();
        String dureeTravail = String.valueOf(data.get(position).getSequence().getCycle().getTravail().getDuree());
        String dureeRepos = String.valueOf(data.get(position).getSequence().getCycle().getRepos());
        String nbRepetitionsCycle = "x"+ data.get(position).getSequence().getCycle().getRepetitions();
        String dureeReposLong = String.valueOf(data.get(position).getSequence().getReposLong());
        String nbRepetitionsSequence = "x"+ data.get(position).getSequence().getRepetitions();

        //Affichage des données dans le RecyclerView
        holder.dureePreparation.setText(tempsPreparation);
        holder.nomTravail.setText(nomTravail);
        holder.dureeTravail.setText(dureeTravail);
        holder.dureeRepos.setText(dureeRepos);
        holder.nbRepetitionsCycle.setText(nbRepetitionsCycle);
        holder.dureeReposLong.setText(dureeReposLong);
        holder.nbRepetitionsSequence.setText(nbRepetitionsSequence);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public Seance getItem(int id){
        return this.data.get(id);
    }

    public void removeItem(int id){
        this.data.remove(id);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener){
        this.longClickListener = itemLongClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
